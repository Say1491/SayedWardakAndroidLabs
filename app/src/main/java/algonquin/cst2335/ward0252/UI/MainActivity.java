package algonquin.cst2335.ward0252.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.ward0252.databinding.ActivityMainBinding;
import algonquin.cst2335.ward0252.data.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // calls parent onCreate()

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot()); //loads XML on Screen

        variableBinding.mybutton.setOnClickListener(click -> {
                    model.editString.postValue(variableBinding.myEditText.getText().toString());
               });

               model.editString.observe(this, s -> {
                    variableBinding.textview.setText("Your edit text has: "+  s);
         });

        model.isSelected.observe(this, selected -> {
            variableBinding.myCheckbox.setChecked(selected);
            variableBinding.myRButton.setChecked(selected);
            variableBinding.mySwitch.setChecked(selected);
        });

        TextView textView = variableBinding.textview; //findViewByID(R.id.textview);
        Button mybutton = variableBinding.mybutton;// impossible to be null
        EditText myEditText = variableBinding.myEditText;
        CheckBox myCheckBox = variableBinding.myCheckbox;
        Switch mySwitch = variableBinding.mySwitch;
        RadioButton myRButton = variableBinding.myRButton;
        ImageView image = variableBinding.image;
        ImageButton myimagebutton = variableBinding.myimagebutton;

        variableBinding.myCheckbox.setOnCheckedChangeListener( (btn,isChecked) -> {
            model.isSelected.postValue(isChecked);
            showToast("CheckBox Clicked");
        });

        //If there is only one line of code between { }
        variableBinding.mySwitch.setOnCheckedChangeListener( (btn,isChecked) -> {
            model.isSelected.postValue(isChecked);
            showToast("Switch Clicked");
        });

        variableBinding.myRButton.setOnCheckedChangeListener( (btn,isChecked) -> {
            model.isSelected.postValue(isChecked);
           showToast("RadioButton Clicked");
        });

        variableBinding.myimagebutton.setOnClickListener( i ->{
            showToast("The Width = " + i.getWidth() + " and the Height = " + i.getHeight());
        });

        }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}

