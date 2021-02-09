package com.example.jsonexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ImageView posterIV = findViewById(R.id.poster_image_view);

        TextView title_tv = findViewById(R.id.title_tv);
        TextView releaseYear_tv = findViewById(R.id.releaseYear_tv);
        TextView rating_tv = findViewById(R.id.rating_tv);
        TextView genre_tv = findViewById(R.id.genre_tv);

        Intent intentForExtras = getIntent();
        Bundle bundle = intentForExtras.getExtras();

        if(bundle != null)
        {
            title_tv.setText(bundle.getString("title"));
            releaseYear_tv.setText(String.valueOf(bundle.getInt("releaseYear")));
            rating_tv.setText(String.valueOf(bundle.getDouble("rating")));
            genre_tv.setText(bundle.getString("genre"));

            Picasso.get().load(bundle.getString("imageURL")).fit().centerInside().into(posterIV);
        }

    }

    @Override
    public void onBackPressed()
    {
        Intent backToListIntent = new Intent(this, MovieListActivity.class);  // optional, but to be sure
        startActivity(backToListIntent);
        super.onBackPressed();
    }
}


