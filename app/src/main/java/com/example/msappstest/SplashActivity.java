package com.example.msappstest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity
{
    final String JSON_URL = "https://api.androidhive.info/json/movies.json";

    public static final String SHARED_PREFS_NAME = "login data";
    public static final String DOWNLOADED_KEY = "data downloaded successfully";

    RequestQueue requestQueue;
    StringBuffer movieDetails;
    boolean error_occurred;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    Intent movie_list_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        movie_list_intent = new Intent(SplashActivity.this, MovieListActivity.class);

        if(!(sharedPreferences.getBoolean(DOWNLOADED_KEY, false))) // checking if the movies data has already downloaded to the SQLite DB
            parse_JSON_to_SQLite();
        else
        {
            startActivity(movie_list_intent);
        }
    }


    void parse_JSON_to_SQLite()
    {
        requestQueue = Volley.newRequestQueue(this);
        movieDetails = new StringBuffer();
        error_occurred = false;
        databaseHelper = new DatabaseHelper(SplashActivity.this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        for(int i = 0; i < response.length(); i++)
                        {
                            Movie movie;
                            try
                            {
                                JSONObject jsonObjMovie = response.getJSONObject(i);

                                String title = jsonObjMovie.getString("title");
                                String imageUrl = jsonObjMovie.getString("image");
                                String genreStr;
                                double rating = jsonObjMovie.getDouble("rating");
                                int releaseYear = jsonObjMovie.getInt("releaseYear");

                                JSONArray genres = jsonObjMovie.getJSONArray("genre");

                                ArrayList<String> genresList = new ArrayList<>();
                                for(int j = 0; j < genres.length(); j++)
                                {
                                    genresList.add(genres.getString(j));
                                }

                                genreStr = genresList.toString().substring(1, genresList.toString().length()-1); // removes the "[" and and the "]" from the genres list

                                movie = new Movie(title, imageUrl, rating, releaseYear, genreStr);

                                if(! databaseHelper.add_movie(movie))
                                    error_occurred = true;


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("SplashActivity Errors", "JSON error");
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                Log.i("SplashActivity Errors", "Probably SQLite error");
                            }
                        }

                        if(!error_occurred)
                        {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(DOWNLOADED_KEY, true);
                            editor.apply();
                        }

                        databaseHelper.close();

                        startActivity(movie_list_intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                Log.i("SplashActivity Errors", "JSON response error");
            }
        });

        requestQueue.add(jsonArrayRequest);
    }
}