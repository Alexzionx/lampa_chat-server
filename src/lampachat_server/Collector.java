package lampachat_server;

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
            }

            System.out.println("Active Thread Count: " + Thread.activeCount());
        }
    }

}
