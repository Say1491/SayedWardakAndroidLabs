package algonquin.cst2335.ward0252;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import algonquin.cst2335.ward0252.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected String cityname;
    protected RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        queue = Volley.newRequestQueue(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(  binding.getRoot()  );

        binding.forecastButton.setOnClickListener(click -> {

            cityname = binding.cityTextField.getText().toString();

            String url = "https://api.openweathermap.org/data/2.5/weather?q="
                    + URLEncoder.encode(cityname)
                    + "&appid=7e943c97096a9784391a981c4d878b22&units=Metric";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response -> {
                        JSONObject main = null;
                        try {
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);

                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");

                            JSONObject mainObject = response.getJSONObject("main");

                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");

                            String pictureURL = "https://openweathermap.org/img/w/" + iconName + ".png";

                            ImageRequest imgReq = new ImageRequest(pictureURL, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {

                                    binding.icon.setImageBitmap(bitmap);
                                    Bitmap image = bitmap;
                                    try {
                                        image.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput(iconName + ".png",
                                                Activity.MODE_PRIVATE));
                                    } catch (FileNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
                                    int i = 0;
                                }
                            }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                    (error) -> {
                                        int i = 0;
                                    });

                            queue.add(imgReq);

                            runOnUiThread(() -> {
                                binding.temp.setText("The current temperature is: " + current +"°");
                                binding.temp.setVisibility(View.VISIBLE);

                                binding.minTemp.setText("The min temperature is: " + min +"°");
                                binding.minTemp.setVisibility(View.VISIBLE);

                                binding.maxTemp.setText("The min temperature is: " + max +"°");
                                binding.maxTemp.setVisibility(View.VISIBLE);

                                binding.humitidy.setText("The humidity is: " + humidity + "%");
                                binding.humitidy.setVisibility(View.VISIBLE);

                                binding.icon.setVisibility(View.VISIBLE);

                                binding.description.setText(description);
                                binding.description.setVisibility(View.VISIBLE);

                            });

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }),
                    (error) -> {
                        int i = 0;
                    });
            queue.add(request);
        });
    }
}