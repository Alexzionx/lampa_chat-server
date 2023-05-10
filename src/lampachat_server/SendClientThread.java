package lampachat_server;

import protocol.Protocol_v1;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SendClientThread implements Runnable {

    private Thread self;
    private Socket sock;
    private long userID;
    private String userName;
    private List<Protocol_v1> MessagesList;

    private ObjectOutput writer;
    public ObjectInput readerObj;
    private boolean ConnMon = true;

    public SendClientThread(Socket sock) {
        this.sock = sock;
        self = new Thread(this);
        MessagesList = new ArrayList<>();
    }

    public void setupAndRun() throws IOException {

        writer = new ObjectOutputStream(sock.getOutputStream());
        readerObj = new ObjectInputStream(sock.getInputStream());
        self.start();

    }

    public boolean isAlive() throws IOException {
        return readerObj.readBoolean();
    }

    public void addMessageToArray(Protocol_v1 mess) {
        MessagesList.add(mess);
    }

    public void exit() {
        ConnMon = false;
    }

    @Override
    public void run() {
        boolean trig = false;
        Database db = new Database();
        int tryCount = 10;
        while (tryCount > 0) {
            System.out.print("");
            try {
                Protocol_v1 a = ((Protocol_v1) readerObj.readObject());
                userName = a.getFrom_user();
                if (a.getService_message()) {
                    if (a.getMessage().equals("login")) {
                        userID = db.getLoginID(a.getFrom_user(), a.getTo_user());
                        System.out.println("USER ID=(" + userID + ")");
                        if (userID > 0) {
                            trig = true;
                            break;
                        } else {
                            System.out.println("id < 0");
                            trig = false;
                            writer.writeObject(new Protocol_v1(0, "", "", "wrong login or password", true));
                            writer.flush();
                        }
                    }
                }
                break;
            } catch (ClassNotFoundException | IOException ex) {
                System.out.println("SendClientThread > " + ex);
                tryCount--;
            }
        }
        System.out.println("connected SendThread - user(" + userName + ") userID=" + userID);
        System.out.println(" trig=" + trig);
        if (trig) {
            LampaChat_server.usersOnlinelist.add(userID);
            try {
                new ConnMonitor(this);
                new MessageNotSendedMonitor(userName, this);
                while (ConnMon) {
                    while (!MessagesList.isEmpty()) {
                        System.out.println("SendThread - MessagesList size(" + MessagesList.size() + ") user(" + userName + ")");
                        writer.writeObject(MessagesList.get(0));
                        writer.flush();
                        if (MessagesList.get(0).getTo_user().equals(userName)) {
                            db.setSendedMessagebyId(MessagesList.get(0).getTo_user(), MessagesList.get(0).getFrom_user(), MessagesList.get(0).getId());
                        } else {
                            db.setSendedMessagebyId(MessagesList.get(0).getFrom_user(), MessagesList.get(0).getTo_user(), MessagesList.get(0).getId());
                        }
                        MessagesList.remove(0);
                        continue;
                    }
                    System.out.println("SendThread - MessagesList empty(" + userName + ")");
                    try {
                        Thread.sleep(1000);
                        System.out.println("SendThread pause - user(" + userName + ")");
                    } catch (InterruptedException ex) {
                    }
                }
            } catch (IOException ex) {
                System.out.print("SendThread ERROR - user(" + userName + ")");
            }
            LampaChat_server.usersOnlinelist.remove(userID);
        }
        System.out.println("SendThread is END - user(" + userName + ")");
    }
}
