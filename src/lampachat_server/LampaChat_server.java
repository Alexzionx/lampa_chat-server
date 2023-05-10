package lampachat_server;

import protocol.Protocol_v1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class LampaChat_server {

    private static int port_in;
    private static int port_out;
    static boolean collectorwait = true;
    static List<Protocol_v1> bufferArray;
    static List<Long> usersOnlinelist;
    static Map<Long, bufferArray> bufMap;

    public void start() {

        bufferArray = new ArrayList<>();
        usersOnlinelist = new ArrayList<>();
        bufMap = new HashMap<>();
        Options opt = new Options();
        opt.createDirectoryStructure();
        opt.readOptions();
        
        Database db = new Database();
        db.createDefaultTables();

        port_in = opt.getPort1();
        port_out = opt.getPort2();
        System.out.printf("\n ________________\n|__SERVER START__|\n|--Port1=%d--\n|--Port2=%d--\n|________________|\n", port_in, port_out);

        ReaderStarter readerStarter = new ReaderStarter(port_in, opt);
        SendStarter writerStarter = new SendStarter(port_out);
        Collector collector = new Collector();
        MessageManager manager = new MessageManager();

    }

    public static class bufferArray extends ArrayList<Protocol_v1> {
    }
}
