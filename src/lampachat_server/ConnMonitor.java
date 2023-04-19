package lampachat_server;

import java.io.IOException;

public class ConnMonitor implements Runnable {

    private Thread self;
    private SendClientThread st;
    private ReadClientThread rt;
    private String trig;

    public ConnMonitor(SendClientThread s) {
        // System.out.println("ConnMon Construct");
        self = new Thread(this);
        st = s;
        trig = "s";
        self.start();
        //System.out.println("ConnMon Construct 2");
    }

    public ConnMonitor(ReadClientThread r) {
        // System.out.println("ConnMon Construct");
        self = new Thread(this);
        rt = r;
        trig = "r";
        self.start();
        //System.out.println("ConnMon Construct 2");
    }

    @Override
    public void run() {
        // System.out.println("ConnMon START");
        switch (trig) {
            case "s":
                        try {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                }
                System.out.println("ConnMont st readobj");
                st.isAlive();
                System.out.println("try st end");
            } catch (IOException ex) {
                System.out.println("ConnMon st FALL");
                st.exit();
            }
            break;
            case "r":
                        try {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                }
                System.out.println("ConnMont rt readobj");
                rt.isAlive();
                System.out.println("try tr end");
            } catch (IOException ex) {
                System.out.println("ConnMon rt FALL");
                rt.exit();
            }
            break;
            default:
                System.out.println("Error SWITCH argument (ConnMonitor)");
        }
        
        System.out.println("ConnMon END");
    }

}
