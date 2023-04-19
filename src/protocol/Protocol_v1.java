package protocol;

import java.io.Serializable;

public class Protocol_v1 implements Serializable {

    private long id;
    private String from_user;
    private String to_user;
    private String message;
    private boolean sended;
    private boolean service_message;
    private String time;

    public Protocol_v1(long id, String from_user, String to_user, String message) {
        this(id, from_user, to_user, message, false, false,"");
    }
        public Protocol_v1(long id, String from_user, String to_user, String message,String time) {
        this(id, from_user, to_user, message, false, false,time);
    }
     public Protocol_v1(long id, String from_user, String to_user, String message,boolean service_message) {
        this(id, from_user, to_user, message, false, service_message,"");
    }
      public Protocol_v1(long id, String from_user, String to_user, String message,boolean service_message,String time) {
        this(id, from_user, to_user, message, false, service_message,time);
    }
    public Protocol_v1(long id,boolean service_message, String message){
        this(id, "", "", message, false, service_message,"");
    }

    private Protocol_v1(long id, String from_user, String to_user, String message, boolean sended, boolean service_message,String time) {
        this.id = id;
        this.from_user = from_user;
        this.to_user = to_user;
        this.message = message;
        this.sended = sended;
        this.service_message = service_message;
        this.time=time;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSended(boolean sended) {
        this.sended = sended;
    }

    public boolean getSended() {
        return sended;
    }

    public boolean getService_message() {
        return service_message;
    }

    public String getFrom_user() {
        return from_user;
    }

    public String getTo_user() {
        return to_user;
    }
    
     public String getTime() {
        return time;
    }
        public void setTime(String time) {
        this.time = time;
    }
    

}
