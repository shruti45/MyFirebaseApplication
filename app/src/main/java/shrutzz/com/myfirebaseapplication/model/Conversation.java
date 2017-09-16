package shrutzz.com.myfirebaseapplication.model;

import java.util.ArrayList;

/**
 * Created by developer on 6/29/17.
 */

public class Conversation {

    ArrayList<Message> listMessageData;

    public Conversation() {
        this.listMessageData = new ArrayList<>();
    }

    public ArrayList<Message> getListMessageData(){
        return  listMessageData;
    }
}
