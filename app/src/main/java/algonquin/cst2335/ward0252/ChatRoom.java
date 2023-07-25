package algonquin.cst2335.ward0252;

import static androidx.core.view.accessibility.AccessibilityEventCompat.setAction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.ward0252.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ward0252.databinding.MessageDetailsBinding;
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

    ChatMessageDAO myDAO;

    @Entity
    public static class ChatMessage{

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name="ID")
        long id;
        @ColumnInfo(name="message")
        String message;
        @ColumnInfo(name="timeSent")
        String timeSent;
        @ColumnInfo(name="isSentButton")
        boolean isSentButton;

        public ChatMessage() {} // empty constructor
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

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),MessageDatabase.class,"MyChatMessageDatabase").build();
        myDAO = db.getDao();

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.myToolbar);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        chatModel.selectedMessage.observe(this,(newMessage) ->{
            if(newMessage != null) {
                MessageDetailsFragment detailsFragment = new MessageDetailsFragment(newMessage);

                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                tx.addToBackStack("Go Back One");
                tx.add(R.id.fragmentLocation, detailsFragment);
                tx.commit();
            }
        });

        messages = chatModel.messages.getValue();
        btn = binding.sendButton;
        rbtn = binding.receiveButton;
        textInput = binding.textMessage;
        recyclerView = binding.recycleView;

        if(messages == null){
            chatModel.messages.postValue(messages = new ArrayList<>());
        }

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(()->{
            List<ChatMessage> fromDatabase = myDAO.getAllMessages();

            messages.addAll(fromDatabase);

            runOnUiThread(()->{
                recyclerView.setAdapter(myAdapter);
            });
        });

        btn.setOnClickListener(click ->{

            boolean isSentButton = true;
            ChatMessage newMessage = new ChatMessage(textInput.getText().toString(),getCurrentTimeStamp(),isSentButton);
            messages.add(newMessage);
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(()->{
                newMessage.id = myDAO.insertMessage(newMessage);

            });
            myAdapter.notifyDataSetChanged();
            textInput.setText("");
            recyclerView.scrollToPosition(messages.size() - 1);

        });

        rbtn.setOnClickListener(click ->{
            boolean isSentButton = false;
            ChatMessage reMessage = new ChatMessage(textInput.getText().toString(),getCurrentTimeStamp(),isSentButton);
            messages.add(reMessage);
            Executor thread2 = Executors.newSingleThreadExecutor();
            thread2.execute(()->{
                reMessage.id = myDAO.insertMessage(reMessage);

            });
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

    private void setSupportActionBar(Toolbar myToolbar) {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    ChatMessage selected = chatModel.selectedMessage.getValue();

        if(item.getItemId() == R.id.item_1){
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
            builder.setTitle("Question:");
            builder.setMessage("Do you want to delete the message" + selected.getMessage());
            builder.setNegativeButton("No", (dialog,cl) ->{
                Log.d("DELETE", "User said no");
            });

            builder.setPositiveButton("Yes", (dialog,cl) ->{
                Log.d("DELETE", "User said yes");
                int whichRowClicked = messages.indexOf(selected);
                ChatMessage cm = messages.get(whichRowClicked);
                Executor thread3 = Executors.newSingleThreadExecutor();
                thread3.execute(()->{
                    myDAO.deleteMessage(cm);
                    messages.remove(whichRowClicked);

                    runOnUiThread(()->{
                        myAdapter.notifyDataSetChanged();
                    });

                    Snackbar.make(btn,"You deleted message #" + whichRowClicked, Snackbar.LENGTH_LONG)
                            .setAction("Undo",(click)->{
                                Executor thrd = Executors.newSingleThreadExecutor();
                                thrd.execute(()->{myDAO.insertMessage(cm);});
                                messages.add(whichRowClicked,cm);
                                runOnUiThread(()->{myAdapter.notifyDataSetChanged();});
                            })
                            .show();
                });

            });

            builder.create().show();

        } else if(item.getItemId() == R.id.item_2){
            Toast.makeText(this, "Version 1.0, Created by Sayed Wardak", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return true;
    }



    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView messageTxt;
        TextView timeTxt;
        public MyRowHolder (@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(clk ->{
            int index = getAbsoluteAdapterPosition();
            chatModel.selectedMessage.postValue(messages.get(index));
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setTitle("Question:");
                builder.setMessage("Do you want to delete the message" + messageTxt.getText());
                builder.setNegativeButton("No", (dialog,cl) ->{
                    Log.d("DELETE", "User said no");
                });

                builder.setPositiveButton("Yes", (dialog,cl) ->{
                    Log.d("DELETE", "User said yes");
                    int whichRowClicked = getAbsoluteAdapterPosition();
                    ChatMessage cm = messages.get(whichRowClicked);
                    Executor thread3 = Executors.newSingleThreadExecutor();
                    thread3.execute(()->{
                        myDAO.deleteMessage(cm);
                        messages.remove(whichRowClicked);

                        runOnUiThread(()->{
                            myAdapter.notifyDataSetChanged();
                        });

                        Snackbar.make(btn,"You deleted message #" + whichRowClicked, Snackbar.LENGTH_LONG)
                                .setAction("Undo",(click)->{
                                    Executor thrd = Executors.newSingleThreadExecutor();
                                    thrd.execute(()->{myDAO.insertMessage(cm);});
                                    messages.add(whichRowClicked,cm);
                                    runOnUiThread(()->{myAdapter.notifyDataSetChanged();});
                                })
                                .show();
                    });

                });

                builder.create().show();
                */
            });

            messageTxt = itemView.findViewById(R.id.theMessage);
            timeTxt = itemView.findViewById(R.id.theTime);
        }

    }
}