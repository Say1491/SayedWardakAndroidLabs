package algonquin.cst2335.ward0252;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The main activity will hold information for the app
 * It will show Textview, EditText, and Button for password check
 *
 * @author Sayed
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Variable set up for text view, the message in the center of the screen
     */
    private TextView tv = null;
    /**
     * Variable set up for edit text, this will be where the password would be put in
     */
    private EditText et = null;
    /**
     * Variable set up for the button, the button is right under where user enters their password
     */
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.Password);
        btn = findViewById(R.id.Button);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();

            checkPasswordComplexity(password);
        });

    }

    /**
     * This function will check password has proper requirements: Uppercase/Lowercase, number, and special symbol.
     *
     * @param password pw The String object that we are checking
     * @return Returns true if all conditions have been met
     */
    boolean checkPasswordComplexity(String password) {

        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;
        int duration = Toast.LENGTH_SHORT;

        for (int i = 0; i < password.length(); i++) {
            char xyz = password.charAt(i);
            if (Character.isUpperCase(xyz)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(xyz)) {
                foundLowerCase = true;
            } else if (Character.isDigit(xyz)) {
                foundNumber = true;
            } else if (isSpecialChar(xyz)) {
                foundSpecial = true;
            }
        }


        if (!foundUpperCase) {
            tv.setText(R.string.failMessage);
            Toast.makeText(this, "You are missing a uppercase letter", duration).show();
            return false;
        } else if (!foundLowerCase) {
            tv.setText(R.string.failMessage);
            Toast.makeText(this, "You are missing a lowercase letter", duration).show();
            return false;
        } else if (!foundNumber) {
            tv.setText(R.string.failMessage);
            Toast.makeText(this, "You are missing a number", duration).show();
            return false;
        } else if (!foundSpecial) {
            tv.setText(R.string.failMessage);
            Toast.makeText(this, "You are missing special characters", duration).show();
        } else {
            tv.setText(R.string.successMessage);
        }
        return true;
    }

    /**
     * Will check if the character is a special character
     *
     * @param c The character to be checked at
     * @return will return true if character is using a special character, will be false if it isn't.
     */
    boolean isSpecialChar(char c) {
        switch (c) {
            case '#':
            case '?':
            case '*':
            case '$':
            case '%':
            case '^':
            case '&':
            case '!':
            case '@':
                return true;
            default:
                return false;
        }
    }
}