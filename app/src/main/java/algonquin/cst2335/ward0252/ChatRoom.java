package algonquin.cst2335.ward0252;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import algonquin.cst2335.ward0252.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ward0252.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<String> messages = new ArrayList<>();

    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendButton.setOnClickListener(click ->{
            messages.add(binding.inputText.getText().toString());
            myAdapter.notifyItemInserted(messages.size()-1);
            myAdapter.notifyItemRemoved(messages.size()-1);
            myAdapter.notifyDataSetChanged();


            binding.inputText.setText("");
        });


        class MyRowHolder extends RecyclerView.ViewHolder{
            TextView messageTxt;
            TextView timeTxt;
            public MyRowHolder (@NonNull View itemView){
                super(itemView);
                messageTxt = itemView.findViewById(R.id.theMessage);
                timeTxt = itemView.findViewById(R.id.theTime);
            }
        }

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                return new MyRowHolder( binding.getRoot());
            }

            public int getItemViewType(int position){
                return 0;
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageTxt.setText("");
                holder.timeTxt.setText("");
                String obj = messages.get(position);
                holder.messageTxt.setText(obj);

            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

    }
}