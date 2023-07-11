package algonquin.cst2335.ward0252;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatRoom.ChatMessage.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {
    abstract ChatMessageDAO getDao();
}
