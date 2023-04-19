package lampachat_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReaderStarter implements Runnable {

    private Thread self;
    private int port;
    private Options opt;

    public ReaderStarter(int port,Options opt) {
        this.opt=opt;
        this.self = new Thread(this);
        this.port = port;
        self.start();
    }

    @Override
    public void run() {
        //Options opt = new Options();
        //opt.readOptions();
        boolean trig = true;
        while (trig) {
            System.out.println("ReaderStarter START (ReadClientThread)");
            try {
                System.out.println("ReaderStarter loop");
                ServerSocket server = new ServerSocket(port);
                while (true) {
                    System.out.println("ReaderStarter wait");
                    Socket sock = server.accept();
                    new ReadClientThread(sock, opt).setupAndRun();
                }
            } catch (IOException ex) {
                System.out.println("ReaderStarter ERROR");
            }
        }
        System.out.println("ReaderStarter END");
    }

    public void setupAndRun() {
        self.start();
    }
}
