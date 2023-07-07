package algonquin.cst2335.ward0252;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.ward0252.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ward0252.databinding.ReceiveMessageBinding;
import algonquin.cst2335.ward0252.databinding.SentMessageBinding;


public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    private ArrayList<ChatMessage> messages;
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView recyclerView;
    private EditText textInput;
    private Button btn;
    private Button rbtn;

    public static class ChatMessage{
        String message;
        String timeSent;
        boolean isSentButton;

        ChatMessage(String m, String t, boolean sent){
            message = m;
            timeSent = t;
            isSentButton = sent;
        }
        public String getMessage(){
            return message;
        }
        public String getTimeSent(){
            return timeSent;
        }
        public  Boolean isSentButton(){
            return isSentButton;
        }
    }

    private String getCurrentTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyy hh:mm:ss a");
        return sdf.format(new Date());
    }


    @Override
    @SuppressLint("NotifyDataSetChange")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        btn = binding.sendButton;
        rbtn = binding.receiveButton;
        textInput = binding.textMessage;
        recyclerView = binding.recycleView;

        if(messages == null){
            chatModel.messages.postValue(messages = new ArrayList<>());
        }

        btn.setOnClickListener(click ->{
            boolean isSentButton = true;
            messages.add(new ChatMessage(textInput.getText().toString(),getCurrentTimeStamp(),isSentButton));
            myAdapter.notifyDataSetChanged();
            textInput.setText("");
            recyclerView.scrollToPosition(messages.size() - 1);
        });

        rbtn.setOnClickListener(click ->{
            boolean isSentButton = false;
            messages.add(new ChatMessage(textInput.getText().toString(),getCurrentTimeStamp(),isSentButton));
            myAdapter.notifyDataSetChanged();
            textInput.setText("");
            recyclerView.scrollToPosition(messages.size() - 1);
        });

        recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType==1) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
                else{
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }
            public int getItemViewType(int position){
                ChatMessage message = messages.get(position);
                if(message.isSentButton()){
                    return 1;
                }
                else{
                    return 2;
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageTxt.setText(obj.getMessage());
                holder.timeTxt.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView messageTxt;
        TextView timeTxt;
        public MyRowHolder (@NonNull View itemView){
            super(itemView);
            messageTxt = itemView.findViewById(R.id.theMessage);
            timeTxt = itemView.findViewById(R.id.theTime);
        }
    }
}