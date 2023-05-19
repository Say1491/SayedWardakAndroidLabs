package algonquin.cst2335.ward0252;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // looks for something with the id "textview", return that object
       TextView textview = findViewById(R.id.textview); //same as getElementByID in JavaScript
       Button b = findViewById(R.id.mybutton); // search XML for button ID
        EditText theEdit = findViewById(R.id.myEditText);
       // anonymous class, an interface (declare functions but no method bodies)
       b.setOnClickListener(new View.OnClickListener() {
           // Provide the missing function:
           @Override
           public void onClick(View v) {
              String editString = theEdit.getText().toString();// return whats in the Edit
               // Change what is in the Textview
               textview.setText("Your edit text has: " + editString);
           }
       });


    }
}