package lampachat_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import protocol.Protocol_v1;

public class database extends org.sqlite.JDBC {

    private static final String DRIVER_NAME = "org.sqlite.JDBC";
    private static final String DATABASE_FILE = "base/database.db";
    //private static final String DATABASE_FILESQL = "jdbc:sqlite:base/database.db";
    private static final String DATABASE_FILESQL = "jdbc:sqlite:" + DATABASE_FILE;

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_FILESQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createDefaultTables() {
        //System.out.println("createDefaultTables");
        String s = "CREATE TABLE if NOT EXISTS users (id integer PRIMARY KEY,login text NOT NULL,password text NOT NULL,hide_mode integer DEFAULT 0)";
        //String s2 = "CREATE TABLE if NOT EXISTS registration_rule (id integer PRIMARY KEY,block registration integer DEFAULT 0)";
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(s);
            // stmt.executeUpdate(s2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNewUser(String login, String pass) {
        System.out.println("addNewUser");
        String sql = "INSERT INTO users (login,password) VALUES(?,?)";
        String sql2 = "CREATE TABLE " + login + "_friendList (id integer PRIMARY KEY,friend text NOT NULL)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql); Statement stmt = conn.createStatement()) {
            pstmt.setString(1, login);
            pstmt.setString(2, pass);
            pstmt.executeUpdate();
            stmt.executeUpdate(sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public long getLoginID(String login, String pass) {
        System.out.println("getLoginID");
        String s = "SELECT id FROM users WHERE login=\"" + login + "\" AND password=\"" + pass + "\"";
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(s);
            long res = rs.getInt("id");
            System.out.println("find- " + res);
            return res;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public long getUserID(String login) {
        System.out.println("getUserID");
        String s = "SELECT id FROM users WHERE login=\"" + login + "\"";
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(s);
            long res = rs.getInt(1);
            System.out.println("find- " + res);
            return res;
        } catch (SQLException e) {
            System.out.println("->getUserID error - " + e.getMessage());
        }
        return -1;
    }

    public String getAllUsers() throws SQLException {
        System.out.println("getAllUsers");
        String result = "";
        String s = "SELECT login FROM users";
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            System.out.println("sql code= " + s);
            ResultSet rs = stmt.executeQuery(s);
            System.out.println("rs=" + rs.toString());
            while (rs.next()) {
                result = result + rs.getString(1) + "\n";
            }
            return result;
        }
    }

    public String getAllUsersIsNotHide() throws SQLException {
        System.out.println("getAllUsersIsNotHide");
        String result = "";
        String s = "SELECT login FROM users WHERE hide_mode=0";
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            System.out.println("sql code= " + s);
            ResultSet rs = stmt.executeQuery(s);
            System.out.println("rs=" + rs.toString());
            while (rs.next()) {
                result = result + rs.getString(1) + "\n";
            }
            return result;
        }
    }

    public boolean addfriend(String userName, String nameNewFriend) {
        for (int i = 1; i < getFriendCount(userName); i++) {
            if (nameNewFriend.equals(getFriendNameByID(userName, i))) {
                return false;
            }
        }
        System.out.println("addfriend");
        String s = "INSERT INTO -userName-_friendList (friend) VALUES(\"-nameNewFriend-\")";
        s = s.replaceAll("-userName-", userName);
        s = s.replaceAll("-nameNewFriend-", nameNewFriend);
        String s1 = "CREATE TABLE -nameto-_-namefrom- (id integer PRIMARY KEY,to_user text NOT NULL,from_user text NOT NULL,message text NOT NULL,sended integer DEFAULT 0,time text NOT NULL)";
        s1 = s1.replaceAll("-nameto-", userName);
        s1 = s1.replaceAll("-namefrom-", nameNewFriend);
        String s2 = "CREATE TABLE -nameto-_-namefrom- (id integer PRIMARY KEY,to_user text NOT NULL,from_user text NOT NULL,message text NOT NULL,sended integer DEFAULT 0,time text NOT NULL)";
        s2 = s2.replaceAll("-nameto-", nameNewFriend);
        s2 = s2.replaceAll("-namefrom-", userName);
        String s3 = "INSERT INTO -userName-_friendList (friend) VALUES(\"-nameNewFriend-\")";
        s3 = s3.replaceAll("-userName-", nameNewFriend);
        s3 = s3.replaceAll("-nameNewFriend-", userName);
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(s);
            stmt.executeUpdate(s1);
            stmt.executeUpdate(s2);
            stmt.executeUpdate(s3);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void addMessage(Protocol_v1 p) {
        System.out.println("addMessage");
        String s = "INSERT INTO -To_user-_-From_user- (from_user,to_user,message,time) VALUES(\"-From_user-\",\"-To_user-\",\"-Message-\",\"-Time-\")";
        s = s.replaceAll("-From_user-", p.getFrom_user());
        s = s.replaceAll("-To_user-", p.getTo_user());
        s = s.replaceAll("-Message-", p.getMessage());
        s = s.replaceAll("-Time-", p.getTime());
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            addMessageToWhoSend(p);//add message to who sended
            stmt.executeUpdate(s);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addMessageToWhoSend(Protocol_v1 p) {
        System.out.println("addMessageToWhoSend");
        String s = "INSERT INTO -From_user-_-To_user- (from_user,to_user,message,time) VALUES(\"-From_user-\",\"-To_user-\",\"-Message-\",\"-Time-\")";
        s = s.replaceAll("-From_user-", p.getFrom_user());
        s = s.replaceAll("-To_user-", p.getTo_user());
        s = s.replaceAll("-Message-", p.getMessage());
        s = s.replaceAll("-Time-", p.getTime());
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(s);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Protocol_v1> getNotSendedMessagetoProtocol(String usernameTo, String usernameFrom, long idMessage) {
        System.out.println("getNotSendedMessagetoProtocol");
        String s = "SELECT * FROM -usernameTo-_-usernameFrom- WHERE id>-idMessage- AND sended=0";
        s = s.replaceAll("-usernameTo-", usernameTo);
        s = s.replaceAll("-usernameFrom-", usernameFrom);
        s = s.replaceAll("-idMessage-", String.valueOf(idMessage));
        List<Protocol_v1> list = new ArrayList<>();
        System.out.println(s);
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(s);
            while (rs.next()) {
                long id = rs.getInt("id");
                String from_user = rs.getString("from_user");
                String to_user = rs.getString("to_user");
                String message = rs.getString("message");
                String time = rs.getString("time");
                System.out.printf("%s. %s - %s (%s) \n", from_user, to_user, message, time);
                list.add(new Protocol_v1(id, from_user, to_user, message, time));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public void setSendedMessagebyId(String usernameTo, String usernameFrom, long idMessage) {
        System.out.println("setSendedMessagebyId");
        String s = "UPDATE -usernameTo-_-usernameFrom- set sended=1 WHERE id=-id-";
        s = s.replaceAll("-usernameTo-", usernameTo);
        s = s.replaceAll("-usernameFrom-", usernameFrom);
        s = s.replaceAll("-id-", String.valueOf(idMessage));
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            System.out.println(s);
            stmt.executeUpdate(s);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setNotSendedMessagebyId(String usernameTo, String usernameFrom, long idMessage) {
        System.out.println("setNotSendedMessagebyId");
        String s = "UPDATE -usernameTo-_-usernameFrom- set sended=0 WHERE id=-id-";
        s = s.replaceAll("-usernameTo-", usernameTo);
        s = s.replaceAll("-usernameFrom-", usernameFrom);
        s = s.replaceAll("-id-", String.valueOf(idMessage));
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            System.out.println(s);
            stmt.executeUpdate(s);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getFriendCount(String login) {
        System.out.println("getFriendCount");
        String s = "SELECT max(id) FROM -LOGIN-_friendList";
        s = s.replaceAll("-LOGIN-", login);
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            System.out.println(s);
            ResultSet rs = stmt.executeQuery(s);
            int res = rs.getInt(1);
            System.out.println("friendCount- " + res);
            return res;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public boolean getHideMode(String login) {
        System.out.println("getHideMode");
        String s = "SELECT hide_mode FROM users WHERE login=\"-LOGIN-\"";
        s = s.replaceAll("-LOGIN-", login);
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            System.out.println(s);
            ResultSet rs = stmt.executeQuery(s);
            int res = rs.getInt(1);
            System.out.println("getHideMode- " + res);
            if (res == 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void setHideMode(String login, boolean hide) {
        System.out.println("setHideMode");
        String s = "UPDATE users set hide_mode=-HIDE- WHERE login=\"-LOGIN-\"";
        s = s.replaceAll("-LOGIN-", login);
        if (hide) {
            s = s.replaceAll("-HIDE-", "1");
        } else {
            s = s.replaceAll("-HIDE-", "0");
        }
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            System.out.println(s);
            stmt.executeUpdate(s);
            System.out.println("set HIDE- " + s);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setPassword(String login, String pass) {
        System.out.println("setPassword");
        String s = "UPDATE users set password=\"-PASS-\" WHERE login=\"-LOGIN-\"";
        s = s.replaceAll("-LOGIN-", login);
        s = s.replaceAll("-PASS-", pass);
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            System.out.println(s);
            stmt.executeUpdate(s);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getCountNotSendedMessages(String usernameTo, String usernameFrom) {
        System.out.println("getCountNotSendedMessages");
        //String s = "SELECT * FROM " + usernameTo + "_" + usernameFrom;
        String s = "SELECT max(id) FROM -usernameTo-_-usernameFrom- WHERE sended=0";
        s = s.replaceAll("-usernameTo-", usernameTo);
        s = s.replaceAll("-usernameFrom-", usernameFrom);
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(s);
            int res = rs.getInt(1);
            System.out.println("CountNotSendedMessages- " + res);
            return res;
        } catch (SQLException e) {
            System.out.println("ERROR getCountNotSendedMessages - " + e.getMessage());
        }
        return -1;
    }

    public int getCountMessages(String usernameTo, String usernameFrom) {
        System.out.println("getCountMessages");
        //String s = "SELECT * FROM " + usernameTo + "_" + usernameFrom;
        String s = "SELECT max(id) FROM -usernameTo-_-usernameFrom-";
        s = s.replaceAll("-usernameTo-", usernameTo);
        s = s.replaceAll("-usernameFrom-", usernameFrom);
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(s);
            int res = rs.getInt(1);
            System.out.println("CountMessages- " + res);
            return res;
        } catch (SQLException e) {
            System.out.println("ERROR getCountMessages - " + e.getMessage());
        }
        return -1;
    }

    public String getFriendNameByID(String userName, int id) {
        System.out.println("getFriendNameByID");
        //  String s = "SELECT friend FROM " + userName + "_friendList where id=" + id;
        String s = "SELECT friend FROM -USER-_friendList where id=-ID-";
        s = s.replaceAll("-USER-", userName);
        s = s.replaceAll("-ID-", String.valueOf(id));
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(s);
            //  String res = rs.getString(2);
            String res = rs.getString("friend");
            System.out.println("friendname- " + res);
            return res;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public String getTime() {
        return ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .format(DateTimeFormatter.ofPattern("MM.dd.yyy, hh.mm.ss"));
    }

}
/*
CREATE TABLE if NOT EXISTS users (id integer PRIMARY KEY,login text NOT NULL,password text NOT NULL)
CREATE TABLE users (id integer PRIMARY KEY,login text NOT NULL,password text NOT NULL)

CREATE TABLE name_friendList (id integer PRIMARY KEY,friend text NOT NULL)
CREATE TABLE nameto_namefrom (id integer PRIMARY KEY,from_user text NOT NULL,message text NOT NULL,sended integer DEFAULT 0)
CREATE TABLE nameto_namefrom (id integer PRIMARY KEY,from_user text NOT NULL,to_user text NOT NULL,message text NOT NULL,sended integer DEFAULT 0)
INSERT INTO users (login,password) VALUES("alex","passlopopo")
INSERT INTO nameto_namefrom (from_user,message) VALUES("alexName","textMassage")
UPDATE nameto_namefrom set sended=1 WHERE id=1
SELECT id FROM users WHERE login="alex"
SELECT id FROM users WHERE login="alex" AND password="re"
SELECT max(id) FROM name_friendList
SELECT * FROM nameto_namefrom WHERE id>1
CREATE TABLE if NOT EXISTS testTable1 (emp_id INT)

 */
