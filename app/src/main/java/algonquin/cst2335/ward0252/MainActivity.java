package algonquin.cst2335.ward0252;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import algonquin.cst2335.ward0252.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";
    protected ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.w("MainActivity", "in onCreate() - Loading Widgets");
        Log.d(TAG, "in onCreate() - Loading Widgets");

        binding.loginButton.setOnClickListener(clk -> {
            Log.e(TAG, "You clicked the button");

            Intent nextPage = new Intent(this, SecondActivity.class);

            String typed = binding.emailText.getText().toString();
            nextPage.putExtra("EmailAddress",typed);

            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.w("MainActivity","in onStart()-Application is visible");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.w("MainActivity","in onResume()-Application is now responding to user Input");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.w("MainActivity","In onPause()-Application no longer responds to user Input");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.w("MainActivity","In onStop()-Application no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.w("MainActivity","In onDestroy()-Application memory used is freed");
    }
}