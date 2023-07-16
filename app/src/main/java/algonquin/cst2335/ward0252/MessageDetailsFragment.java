package algonquin.cst2335.ward0252;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.ward0252.databinding.MessageDetailsBinding;

public class MessageDetailsFragment extends Fragment {

    ChatRoom.ChatMessage thisMessage;
    public MessageDetailsFragment(ChatRoom.ChatMessage toShow){
        thisMessage = toShow;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        MessageDetailsBinding binding = MessageDetailsBinding.inflate(inflater);
        binding.MessageGoesHere.setText(thisMessage.message);
        binding.TimeGoesHere.setText(thisMessage.timeSent);
        binding.DatabaseIDGoesHere.setText(Long.toString(thisMessage.id) );

        return binding.getRoot();
    }
}
