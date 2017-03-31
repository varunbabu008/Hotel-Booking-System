package Server.Entity;

public class Order {
    private int oid;
    private String hid;
    private String username;

    public int getOid() {
        return oid;
    }
    public void setOid(int oid) {
        this.oid = oid;
    }
    public String getHid() {
        return hid;
    }
    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }


}
