package lampachat_server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Collector implements Runnable {

    private Thread self;

    public Collector() {
        self = new Thread(this);
        self.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Collector.class.getName()).log(Level.INFO, ex.getMessage());
            }
            System.out.println("Active Thread Count: " + Thread.activeCount());
        }
    }

}
