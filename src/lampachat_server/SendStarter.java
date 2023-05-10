package lampachat_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendStarter implements Runnable {

    private Thread self;
    private int port;

    public SendStarter(int port) {
        this.self = new Thread(this);
        this.port = port;
        self.start();
    }

    @Override
    public void run() {
        boolean trig = true;
        while (trig) {
            System.out.println("SendStarter START (SendClientThread)");
            try {
                System.out.println("WriterStarter loop");
                ServerSocket server = new ServerSocket(port);
                while (true) {
                    System.out.println("SendStarter wait");
                    Socket sock = server.accept();
                    new SendClientThread(sock).setupAndRun();
                }
            } catch (IOException ex) {
                Logger.getLogger(SendStarter.class.getName()).log(Level.INFO, ex.getMessage());
            }
        }
        System.out.println("SendStarter END");
    }

    public void setupAndRun() {
        self.start();
    }
}
