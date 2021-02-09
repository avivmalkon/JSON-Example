package com.example.jsonexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

public class MovieListActivity extends AppCompatActivity
{
    MovieAdapter movieAdapter;
    //Intent intentForExtras;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        DatabaseHelper databaseHelper = new DatabaseHelper(MovieListActivity.this);
        List<Movie> movies = databaseHelper.getMovies();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        movieAdapter = new MovieAdapter(MovieListActivity.this, movies);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanIntent = new Intent(MovieListActivity.this, ScanningActivity.class);
                startActivity(scanIntent);
                onStop();
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Intent exitIntent = new Intent(Intent.ACTION_MAIN);
        exitIntent.addCategory(Intent.CATEGORY_HOME);
        exitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(exitIntent);

        super.onBackPressed();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Intent intentForExtras = getIntent();
        Bundle bundle = intentForExtras.getExtras();

        if(bundle != null)
        {
            if(intentForExtras.getBooleanExtra("camera_denied", false))
            {
                Snackbar.make(coordinatorLayout, R.string.camera_permissions_denied, Snackbar.LENGTH_LONG).show();
                intentForExtras.removeExtra("camera_denied");
            }
            else
            {
                if(intentForExtras.getBooleanExtra("duplicate_movie", false))
                {
                    Snackbar.make(coordinatorLayout, R.string.movie_already_exist, Snackbar.LENGTH_LONG).show();
                    intentForExtras.removeExtra("duplicate_movie");
                }
                

                int position = intentForExtras.getIntExtra("new_item_id", -1);

                if(position != -1)
                {
                    // new movie added

                    movieAdapter.notifyItemInserted(position);

                    intentForExtras.removeExtra("new_item_id");
                }
            }
        }
    }
}