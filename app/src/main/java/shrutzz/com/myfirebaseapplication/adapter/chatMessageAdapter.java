package shrutzz.com.myfirebaseapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import shrutzz.com.myfirebaseapplication.R;
import shrutzz.com.myfirebaseapplication.activity.ChatActivity;
import shrutzz.com.myfirebaseapplication.model.Conversation;

/**
 * Created by shrutz on 6/29/17.
 */

public class chatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    Conversation conversation;


    public chatMessageAdapter(Context context, Conversation conversation) {
        this.context = context;
        this.conversation=conversation;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ChatActivity.VIEW_TYPE_FRIEND_MESSAGE){
            View view = LayoutInflater.from(context).inflate(R.layout.left_chat_item, parent, false);
            return new ItemMessageFriendHolder(view);
        }else if (viewType == ChatActivity.VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.right_chat_item, parent, false);
            return new ItemMessageUserHolder(view);
        }
        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemMessageFriendHolder){
            ((ItemMessageFriendHolder) holder).left_usr_Content.setText(conversation.getListMessageData().get(position).text);
        }else if (holder instanceof ItemMessageUserHolder){

            //set the date
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(conversation.getListMessageData().get(position).timestamp);  //here your time in miliseconds

            //it will give in 24 hours format.
//            String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + cl.get(Calendar.MONTH) + "/" + cl.get(Calendar.YEAR);
//            String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE);

            //get time in 12 hours format.
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); //if u want to display seconds also hh:mm:ss a
            String formattedtime = sdf.format(conversation.getListMessageData().get(position).timestamp);

            //set right item data.
            ((ItemMessageUserHolder) holder).right_usr_Content.setText(conversation.getListMessageData().get(position).text);
            ((ItemMessageUserHolder) holder).right_usr_date.setText(formattedtime);

            ((ItemMessageUserHolder) holder).right_usr_name.setText(conversation.getListMessageData().get(position).sender_name);
        }

    }

    @Override
    public int getItemCount() {
        return conversation.getListMessageData().size();
    }

    private class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
        public TextView left_usr_Content,left_usr_name,left_usr_date;

        public ItemMessageFriendHolder(View view) {
            super(view);
            left_usr_Content = (TextView) view.findViewById(R.id.textContentFriend);
            left_usr_name=(TextView)view.findViewById(R.id.textname);
            left_usr_date=(TextView)view.findViewById(R.id.textdate);
        }
    }

    private class ItemMessageUserHolder extends RecyclerView.ViewHolder {

        public TextView right_usr_Content,right_usr_name,right_usr_date;
        public ItemMessageUserHolder(View view) {
            super(view);

            right_usr_Content=(TextView) view.findViewById(R.id.textContentUser);
            right_usr_name=(TextView) view.findViewById(R.id.textname);
            right_usr_date=(TextView) view.findViewById(R.id.textdate);


        }
    }
}
