package lampachat_server;

import protocol.Protocol_v1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadClientThread implements Runnable {

    private Thread self;
    private Socket sock;
    private final long id;
    private static long clientCount;
    private ObjectInputStream readerObj;
    private ObjectOutput writer;
    private Protocol_v1 a;
    private long userID;
    private boolean ConnMon = true;
    private Options opt;

    public ReadClientThread(Socket sock, Options opt) {
        this.sock = sock;
        this.opt = opt;
        id = clientCount++;
        self = new Thread(this);
    }

    public void setupAndRun() throws IOException {
        readerObj = new ObjectInputStream(sock.getInputStream());
        writer = new ObjectOutputStream(sock.getOutputStream());
        System.out.println("th read");
        self.start();
    }

    public boolean isAlive() throws IOException {
        return readerObj.readBoolean();
    }

    public void exit() {
        ConnMon = false;
    }

    public boolean checkChar(String login, String password) {
        String[] stringToSearch = {";", "\"", " ", "+", "\\"};
        try {
            for (String s : stringToSearch) {
                if (login.contains(s) || password.contains(s)) {
                    System.out.println("fail pass- ");
                    return false;
                }
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        database db = new database();
        while (!self.isInterrupted()) {
            System.out.print("");
            try {
                Protocol_v1 a = ((Protocol_v1) readerObj.readObject());
                if (a.getService_message()) {
                    //------LOGIN
                    if (a.getMessage().equals("login")) {
                        if (checkChar(a.getFrom_user(), a.getTo_user())) {
                            userID = db.getLoginID(a.getFrom_user(), a.getTo_user());
                            if (userID < 0) {
                                System.out.println("id < 0");
                                self.interrupt();
                            }
                        }
                    }
                    //------SINGIN
                    if (a.getMessage().equals("singIn")) {
                        userID = db.getLoginID(a.getFrom_user(), a.getTo_user());
                        if (userID < 0) {
                            System.out.println("id < 0");
                            writer.writeObject(new Protocol_v1(0, "", "", "login is not correct"));
                            writer.flush();
                            self.interrupt();
                        } else {
                            String friendsInServer = "";
                            for (int i = 1; i <= db.getFriendCount(a.getFrom_user()); i++) {
                                friendsInServer = friendsInServer + " " + db.getFriendNameByID(a.getFrom_user(), i);
                            }
                            // writer.writeObject(new Protocol_v1(0, friendsInServer, "", "friends", true));
                            new MessageSendHistory(a.getFrom_user(), this);
                            writer.writeObject(new Protocol_v1(0, friendsInServer, "", "login is correct"));
                            writer.flush();
                        }
                    }
                    //------SINGUP
                    if (a.getMessage().equals("singUp")) {
                        //check allow registration
                        if (opt.isAllow_registration()) {
                            userID = db.getUserID(a.getFrom_user());
                            if (userID < 0) {
                                System.out.println("id < 0");
                                db.addNewUser(a.getFrom_user(), a.getTo_user());
                                userID = db.getLoginID(a.getFrom_user(), a.getTo_user());
                                writer.writeObject(new Protocol_v1(0, "", "", "You are registered!"));
                                writer.flush();
                            } else {
                                writer.writeObject(new Protocol_v1(0, "", "", "Username is busy"));
                                writer.flush();
                                self.interrupt();
                            }
                        } else {
                            writer.writeObject(new Protocol_v1(0, "", "", "Registration in BLOCKED\n"
                                    + "Please, contact to Administrator"));
                            writer.flush();
                        }
                    }
                    //------OTHER SERVICE FLAG
                    if (a.getMessage().equals("some else")) {
                        //code
                    }
                } else {
                    try {
                        Thread.sleep(20000);//20 sec pause beth trying (aka ddos secure)
                    } catch (InterruptedException ex) {
                    }
                    continue;
                }
                break;//for exit from loop
            } catch (ClassNotFoundException | IOException ex) {
                System.out.println(ex);
                self.interrupt();
            }
        }
        System.out.println("connected READ id=" + userID);
        if (userID > 0) {
            try {
                a = ((Protocol_v1) readerObj.readObject());
                System.out.println("in message");
                if (!a.getService_message()) {
                    //------ADD MESSAGE TO SERVER
                    System.out.println("In Message (" + userID + ") - to: " + a.getTo_user() + "; from: " + a.getFrom_user() + "; text: " + a.getMessage());
                    a.setTime(db.getTime());
                    LampaChat_server.bufferArray.add(a);
                } else {
                    //------SERVICE SECTION
                    System.out.println("In SERVICE Mes. (" + userID + ") - to: " + "From-" + a.getFrom_user() + " To-" + a.getTo_user() + " Message" + a.getMessage());
                    switch (a.getMessage()) {
                        case "getUsers":
                            System.out.println("getUsers---");
                            try {
                                writer.writeObject(new Protocol_v1(0, "", "", db.getAllUsersIsNotHide()));
                                writer.flush();
                            } catch (SQLException ex) {
                                System.out.println("err getUsers");
                            }
                            break;
                        case "add Contact":
                            System.out.println("add Contact---");
                            if (db.addfriend(a.getFrom_user(), a.getTo_user())) {
                                writer.writeObject(new Protocol_v1(0, "", "", "Contact added"));
                                writer.flush();
                            } else {
                                writer.writeObject(new Protocol_v1(0, "", "", "Error contact adding"));
                                writer.flush();
                            }
                            break;
                        case "setPassword":
                            System.out.println("setPassword " + a.getTo_user());
                            db.setPassword(a.getFrom_user(), a.getTo_user());
                            if (db.getLoginID(a.getFrom_user(), a.getTo_user()) > 0) {
                                writer.writeObject(new Protocol_v1(0, "", "", "ok"));
                                writer.flush();
                            } else {
                                writer.writeObject(new Protocol_v1(0, "", "", "Error password updating"));
                                writer.flush();
                            }
                            break;
                        case "SetHideMode":
                            System.out.println("SetHideMode");
                            if (a.getTo_user().equals("on")) {
                                db.setHideMode(a.getFrom_user(), true);
                            } else if (a.getTo_user().equals("off")) {
                                db.setHideMode(a.getFrom_user(), false);
                            }
                            writer.writeObject(new Protocol_v1(0, "", "", "ok"));
                            writer.flush();
                            break;
                        case "getHideMode":
                            System.out.println("getHideMode");
                            if (db.getHideMode(a.getFrom_user())) {
                                writer.writeObject(new Protocol_v1(0, "", "", "hide_mode isEnabled"));
                                writer.flush();
                            } else {
                                writer.writeObject(new Protocol_v1(0, "", "", "hide_mode isDisabled"));
                                writer.flush();
                            }
                            break;
                        default:
                        //throw new AssertionError();
                    }/*
                if (a.getMessage().equals("getUsers")) {
                    System.out.println("getUsers---");
                    try {
                        writer.writeObject(new Protocol_v1(0, "", "", db.getAllUsers()));
                    } catch (SQLException ex) {
                        System.out.println("err getUsers");
                    }
                }*/
                    //------addNewFriend
                    /* if (a.getMessage().equals("add Contact")) {
                    System.out.println("add Contact---");
                    if (db.addfriend(a.getFrom_user(), a.getTo_user())) {
                        writer.writeObject(new Protocol_v1(0, "", "", "Contact added"));
                    } else {
                        writer.writeObject(new Protocol_v1(0, "", "", "Error contact adding"));
                    }
                }*/
                    //------OTHER SERVICE FLAG AFTER CHEK LOGIN/PASSWORD
                    if (a.getMessage().equals("some else")) {
                        //code
                    }
                }
                System.out.println("ReadThread end - " + this.hashCode());
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("ReadThread Error");
            }
        }
        System.out.println("ReadThread end 2 - " + this.hashCode());
    }
}
