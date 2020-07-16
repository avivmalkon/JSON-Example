package com.example.msappstest;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanningActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    private ZXingScannerView scannerView;
    Intent movieListIntent;
    int new_item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        scannerView = findViewById(R.id.zxscan);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        addMovieToDatabase(rawResult.getText());
        movieListIntent = new Intent(ScanningActivity.this, MovieListActivity.class);
        movieListIntent.putExtra("new_item_id", new_item_id);
        startActivity(movieListIntent);
    }

    private void addMovieToDatabase(String text)
    {
        try {
            JSONObject jsonObject = new JSONObject(text);

            String title = jsonObject.getString("title");
            String imageUrl = jsonObject.getString("image");
            String genreStr;
            double rating = jsonObject.getDouble("rating");
            int releaseYear = jsonObject.getInt("releaseYear");

            JSONArray genres = jsonObject.getJSONArray("genre");

            ArrayList<String> genresList = new ArrayList<>();
            for(int i = 0; i < genres.length(); i++)
            {
                genresList.add(genres.getString(i));
            }

            genreStr = genresList.toString().substring(1, genresList.toString().length()-1); // removes the "[" and and the "]" from the genres list

            Movie movie = new Movie(title, imageUrl, rating, releaseYear, genreStr);

            DatabaseHelper databaseHelper = new DatabaseHelper(ScanningActivity.this);

            if(databaseHelper.isExist(movie))
            {
                Intent backToListIntent = new Intent(ScanningActivity.this, MovieListActivity.class);
                backToListIntent.putExtra("duplicate_movie", true);
                startActivity(backToListIntent);
                finish(); // prevents bugs
            }
            else
            {
                new_item_id = databaseHelper.add_movie(movie);
                if(new_item_id == -1)
                    Log.i("ScanningActivity Errors", "Error at adding movie to DataBase");
                databaseHelper.close();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        // external library for camera permission:
        Dexter.withContext(ScanningActivity.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener()
        {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
            {
                scannerView.setResultHandler(ScanningActivity.this);
                scannerView.startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse)
            {
                Intent backToListIntent = new Intent(ScanningActivity.this, MovieListActivity.class);
                backToListIntent.putExtra("camera_denied", true);
                startActivity(backToListIntent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken)
            {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}