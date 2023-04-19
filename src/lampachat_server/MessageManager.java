package lampachat_server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageManager implements Runnable {

    Thread self;

    public MessageManager() {
        self = new Thread(this);
        self.start();
    }

    @Override
    public void run() {
        database db = new database();
        while (true) {
            while (!LampaChat_server.bufferArray.isEmpty()) {
                System.out.println("MessageManager message yes");
                if (!LampaChat_server.bufferArray.get(0).getService_message()) {
                    System.out.println("MessageManager ADD message");
                    db.addMessage(LampaChat_server.bufferArray.get(0));
                } else {
                    //CODE for service section
                }
                System.out.println("MessageManager RM message from buffer");
                LampaChat_server.bufferArray.remove(0);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
