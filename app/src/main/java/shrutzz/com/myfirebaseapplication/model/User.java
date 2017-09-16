package shrutzz.com.myfirebaseapplication.model;

/**
 * Created by Shrutz on 7/1/17.
 */

public class User {

    public String name;
    public String email;
    Message message;

    public User() {
        message=new Message();
        message.text="";
        message.timestamp=0;
        message.sender_name="";
    }
}
