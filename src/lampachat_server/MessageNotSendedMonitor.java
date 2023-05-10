package lampachat_server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocol.Protocol_v1;

public class MessageNotSendedMonitor implements Runnable {

    private Thread self;
    private long userID;
    private String userName;
    private SendClientThread st;

    public MessageNotSendedMonitor(String userName, SendClientThread st) {
        self = new Thread(this);
        this.userName = userName;
        this.st = st;
        self.start();
    }

    @Override
    public void run() {
        Database db = new Database();
        userID = db.getUserID(userName);
        Map<String, Long> countMessages = new HashMap<>();
        // for (Long long1 : LampaChat_server.usersOnlinelist) {
        //   System.out.println("MesNotSendMonitor Online-=-"+long1+" (" + userName + ")");}
        System.out.println("contains - " + userID + " ? - " + LampaChat_server.usersOnlinelist.contains(userID));
        while (LampaChat_server.usersOnlinelist.contains(userID)) {
            int count = db.getFriendCount(userName);
            for (int i = 1; i <= count; i++) {
                String friend = db.getFriendNameByID(userName, i);
                if (!countMessages.containsKey(friend)) {
                    countMessages.put(friend, 0L);
                    System.out.println("put (countMessages) - " + friend);
                }
                if (countMessages.get(friend) < db.getCountNotSendedMessages(userName, friend)) {
                    System.out.println("if 2");
                    List<Protocol_v1> list = new ArrayList<>();
                    list = db.getNotSendedMessagetoProtocol(userName, friend, countMessages.get(friend));
                    System.out.println("list size = " + list.size());
                    for (Protocol_v1 pr : list) {
                        System.out.println("for 3");
                        st.addMessageToArray(pr);
                        countMessages.replace(friend, pr.getId());
                        System.out.println("new countmessage for " + friend + " = " + countMessages.get(friend));
                    }
                }
            }
            try {
                System.out.println("pause (" + userName + ")");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageNotSendedMonitor.class.getName()).log(Level.INFO, "MesNotSendMonitor ERROR(" + userName + ")" + ex.getMessage());
            }
        }
        System.out.println("MesNotSendMonitor END(" + userName + ")");
//END thread
    }

}
