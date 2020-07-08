package com.example.msappstest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
{
    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies)
    {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {



        final String title = movies.get(position).getTitle();
        final String imageURL = movies.get(position).getImageURL();
        final double rating = movies.get(position).getRating();
        final int releaseYear = movies.get(position).getReleaseYear();
        final String genre = movies.get(position).getGenre();

        String text = title + " (" + releaseYear + ")";
        holder.titleAndYearET.setText(text);
        Picasso.get().load(imageURL).into(holder.posterIV);

        holder.rootLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent movieDetailsIntent = new Intent(context, MovieDetailsActivity.class);
                movieDetailsIntent.putExtra("title", title);
                movieDetailsIntent.putExtra("imageURL", imageURL);
                movieDetailsIntent.putExtra("rating", rating);
                movieDetailsIntent.putExtra("releaseYear", releaseYear);
                movieDetailsIntent.putExtra("genre", genre);

                context.startActivity(movieDetailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder
    {
        ImageView posterIV;
        TextView titleAndYearET;
        LinearLayout rootLinearLayout;

        public MovieViewHolder(@NonNull View itemView)
        {
            super(itemView);
            posterIV = itemView.findViewById(R.id.poster_image_view);
            titleAndYearET = itemView.findViewById(R.id.titleAndYear_TV);
            rootLinearLayout = itemView.findViewById(R.id.rootLinearLayout);

        }
    }

}
