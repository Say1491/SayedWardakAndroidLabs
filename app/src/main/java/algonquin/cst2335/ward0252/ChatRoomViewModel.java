package algonquin.cst2335.ward0252;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import algonquin.cst2335.ward0252.ChatRoom;
import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatRoom.ChatMessage>> messages = new MutableLiveData<>();
    public MutableLiveData<ChatRoom.ChatMessage> selectedMessage = new MutableLiveData<>();
}
