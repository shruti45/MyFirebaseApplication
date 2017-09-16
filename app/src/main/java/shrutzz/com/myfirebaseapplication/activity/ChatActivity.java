package shrutzz.com.myfirebaseapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shrutzz.com.myfirebaseapplication.R;
import shrutzz.com.myfirebaseapplication.adapter.chatMessageAdapter;
import shrutzz.com.myfirebaseapplication.model.Conversation;
import shrutzz.com.myfirebaseapplication.model.Message;

/**
 * Created by shrutz on 6/27/17.
 */

public class ChatActivity extends AppCompatActivity {
    Activity activity;
    FirebaseDatabase database;
    LinearLayoutManager linearLayoutManager;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    Conversation conversation;

    @Bind(R.id.recyclerChat)
    RecyclerView recyclerChat;
    @Bind(R.id.send_btn)
    ImageView send_btn;
    @Bind(R.id.textChatMsg)
    EditText textChatMsg;

    String mesgcontent;
    chatMessageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity=ChatActivity.this;
        ButterKnife.bind(activity);

        database=FirebaseDatabase.getInstance();

        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerChat.setLayoutManager(linearLayoutManager);

        //to show the last mesg when added.
        linearLayoutManager.setStackFromEnd(true);

         //creating arraylist in this class.just to hold all the mesg and display in a list.
        conversation=new Conversation();

        //to read data from firebase database and show in the list
        FirebaseDatabase.getInstance().getReference().child("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null){

                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                    Message newMessage = new Message();
                    newMessage.text = (String) mapMessage.get("text");
                    newMessage.timestamp= (long) mapMessage.get("timestamp");
                    newMessage.sender_name= (String) mapMessage.get("sender_name");
                    conversation.getListMessageData().add(newMessage);

                    //set adapter.
                    adapter = new chatMessageAdapter(ChatActivity.this, conversation);
                    recyclerChat.setAdapter(adapter);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.send_btn)
    void sendMessage(){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        long time_msg = System.currentTimeMillis();
        System.out.println("time_date :"+time_msg);

        //to get the current firebase login username
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String currentFirebaseUserName =firebaseUser.getEmail().substring(0, firebaseUser.getEmail().indexOf("@"));
        System.out.println("userName :"+currentFirebaseUserName);


        mesgcontent=textChatMsg.getText().toString();
        if (mesgcontent.length() > 0){
            textChatMsg.setText("");
            Message newmessage=new Message();
            newmessage.text=mesgcontent;
            newmessage.sender_name=currentFirebaseUserName;
            newmessage.timestamp=time_msg;

            //generation random number of currenttimemilliseconds.
            String randomnum = String.valueOf(System.currentTimeMillis());

            //to add new mesg to the database.
            database.getReference().child("message/" + randomnum).setValue(newmessage);


        }
    }
}
