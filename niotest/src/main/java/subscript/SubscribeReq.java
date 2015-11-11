package subscript;



import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/11 0011.
 */
@SuppressWarnings("all")
public class SubscribeReq implements Serializable{
    /**
     * Ä¬ÈÏµÄÐòÁÐºÅ
     */
    private static final long serialVersionUID =1L;
    public int subReqID;

    public int getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String userName;
    private String productName;
    private String phoneName;
    private String address;

    @Override
    public String toString(){
        return "SubscribeReq="+userName;
    }
}
