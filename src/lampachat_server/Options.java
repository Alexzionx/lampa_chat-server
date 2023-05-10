package lampachat_server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Options {

    private Properties p = new Properties();
    //default opt
    private boolean allow_registration = true;
    private static int port1 = 30305;
    private static int port2 = 30306;

    public void readOptions() {
        try {
            p.load(new FileInputStream(new File("config/options.properties")));
            if (p.getProperty("allow_registration") != null) {
                allow_registration = Boolean.parseBoolean(p.getProperty("allow_registration"));
            }
            port1 = Integer.parseInt(p.getProperty("server_port_1"));
            port2 = Integer.parseInt(p.getProperty("server_port_2"));
            //Output Properties in console
            System.out.println("---Properties (options.properties)\n"
                    + "---allow_registration=" + allow_registration + "\n"
                    + "---server_port_1=" + port1 + "\n"
                    + "---server_port_2=" + port2 + "\n"
                    + "---");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Options.class.getName()).log(Level.INFO, "Config file (./config/options.properties) NOT found");
            Logger.getLogger(Options.class.getName()).log(Level.INFO, "Creating default config file \"options.properties\".....");
            Logger.getLogger(Options.class.getName()).log(Level.INFO, ex.getMessage());
            setDefaultConfigFile();
        } catch (IOException ex) {
            Logger.getLogger(Options.class.getName()).log(Level.INFO, ex.getMessage());
        }
    }

    public void createDirectoryStructure() {
        try {
            String baseFolder = "base";

            Files.createDirectory(Paths.get(baseFolder));
        } catch (FileAlreadyExistsException ex) {
        } catch (IOException ex) {
            Logger.getLogger(Options.class.getName()).log(Level.INFO, ex.getMessage());
        }
        try {
            String configFolder = "config";
            Files.createDirectory(Paths.get(configFolder));
        } catch (FileAlreadyExistsException ex) {
        } catch (IOException ex) {
            Logger.getLogger(Options.class.getName()).log(Level.INFO, ex.getMessage());
        }
    }

    public void setDefaultConfigFile() {
        try {
            BufferedWriter writer = null;
            String str = "# ---Properties file for Lampa_chat SERVER---\n"
                    + "# If you want back to use default settings, just rename or delete this file and restart server\n"
                    + "#\n"
                    + "# Deny or Allow registration for new Users [allow_registration = true] or false\n"
                    + "# Default server port1=30305\n"
                    + "# Default server port2=30306\n"
                    + "# both ports must be open for the server to work\n"
                    + "#\n"
                    + "#\n"
                    + "allow_registration = " + allow_registration + "\n"
                    + "server_port_1 = " + port1 + "\n"
                    + "server_port_2 = " + port2;
            writer = new BufferedWriter(new FileWriter("config/options.properties"));
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Options.class.getName()).log(Level.INFO, ex.getMessage());
        }
    }

    public boolean isAllow_registration() {
        return allow_registration;
    }

    public int getPort1() {
        return port1;
    }

    public int getPort2() {
        return port2;
    }

}
