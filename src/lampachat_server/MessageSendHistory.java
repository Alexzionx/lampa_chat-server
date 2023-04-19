package lampachat_server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import protocol.Protocol_v1;

public class MessageSendHistory implements Runnable {

    private Thread self;
    private long userID;
    private String userName;
    private ReadClientThread rt;

    public MessageSendHistory(String userName, ReadClientThread rt) {
        self = new Thread(this);
        this.userName = userName;
        this.rt = rt;
        self.start();
    }

    @Override
    public void run() {
        database db = new database();
        for (int i = 1; i <= db.getFriendCount(userName); i++) {
            String friend = db.getFriendNameByID(userName, i);
            for (int j = 1; j <= db.getCountMessages(userName, friend); j++) {
                db.setNotSendedMessagebyId(userName, friend, j);
                System.out.println("Count messAGE - " + db.getCountMessages(userName, friend));
            }
        }
    }
}
