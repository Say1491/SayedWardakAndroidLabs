package algonquin.cst2335.ward0252;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.ward0252.databinding.ActivityMainBinding;
import algonquin.cst2335.ward0252.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    ActivitySecondBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        binding.editTextPhone.setText(prefs.getString("phoneNumber",""));



        File file = new File( getFilesDir(), "Picture.png");

        if(file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            binding.IMV.setImageBitmap(theImage);
        }

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        binding.textView3.setText("Welcome back: " + emailAddress);

        binding.button2.setOnClickListener(clk -> {
            String phoneNumber = binding.editTextPhone.getText().toString();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel: " + phoneNumber));
            editor.putString("PhoneNumber",phoneNumber);
            editor.apply();

            startActivity(call);
        });

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap thumbnail = data.getParcelableExtra("data");
                        binding.IMV.setImageBitmap(thumbnail);

                        FileOutputStream fOut = null;
                        try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);

                            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);

                            fOut.flush();

                            fOut.close();

                        }

                        catch (IOException e)

                        { e.printStackTrace();

                        }
                    }

                }
        );

        binding.button3.setOnClickListener(clk -> {

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                cameraResult.launch(cameraIntent);
            else
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 20);
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if((requestCode == 20) && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startActivity( new Intent(MediaStore.ACTION_IMAGE_CAPTURE) );
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phonenumber = prefs.getString("PhoneNumber",binding.editTextPhone.getText().toString());
        binding.editTextPhone.setText(phonenumber);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber",binding.editTextPhone.getText().toString());
        editor.apply();




    }
}