package com.example.msappstest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String MOVIES_TABLE = "MOVIES_TABLE";
    public static final String TITLE_COLUMN = "TITLE";
    public static final String RELEASED_YEAR_COLUMN = "RELEASED_YEAR";
    public static final String RATING_COLUMN = "RATING";
    public static final String GENRE_COLUMN = "GENRE";
    public static final String IMAGE_URL_COLUMN = "IMAGE_URL";

    public DatabaseHelper(@Nullable Context context)
    {
        super(context, "movies.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MOVIES_TABLE + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_COLUMN + " TEXT, " + RELEASED_YEAR_COLUMN + " INTEGER, " + RATING_COLUMN + " REAL, " + GENRE_COLUMN + " TEXT, " + IMAGE_URL_COLUMN + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean isExist(Movie movie)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MOVIES_TABLE, null);

        if(cursor.moveToFirst())
        {
            do{
                if(movie.getTitle().equals(cursor.getString(1))) // title
                {
                    if(movie.getReleaseYear() == cursor.getInt(2)) //releasedYear
                    {
                        if(movie.getRating() == cursor.getDouble(3)) // rating
                        {
                            if(movie.getGenre().equals(cursor.getString(4))) // genre
                            {
                                if (movie.getImageURL().equals(cursor.getString(5))) //image
                                {
                                    cursor.close();
                                    sqLiteDatabase.close();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }while (cursor.moveToNext());

        }

        cursor.close();
        sqLiteDatabase.close();

        return false;
    }

    public boolean add_movie(Movie movie)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TITLE_COLUMN, movie.getTitle());
        contentValues.put(RELEASED_YEAR_COLUMN, movie.getReleaseYear());
        contentValues.put(RATING_COLUMN, movie.getRating());
        contentValues.put(GENRE_COLUMN, movie.getGenre());
        contentValues.put(IMAGE_URL_COLUMN, movie.getImageURL());

        long result = sqLiteDatabase.insert(MOVIES_TABLE, null, contentValues);

        sqLiteDatabase.close();

        return result != -1;
    }

    public List<Movie> getMovies()
    {
        List<Movie> moviesList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MOVIES_TABLE + " ORDER BY " + RELEASED_YEAR_COLUMN + " DESC", null);

        if(cursor.moveToFirst())
        {
            do{
                String title = cursor.getString(1);
                int releasedYear = cursor.getInt(2);
                double rating = cursor.getDouble(3);
                String genre = cursor.getString(4);
                String imageUrl = cursor.getString(5);

                Movie movie = new Movie(title, imageUrl, rating, releasedYear, genre);

                moviesList.add(movie);


            }while (cursor.moveToNext());

        }

        cursor.close();
        sqLiteDatabase.close();

        return moviesList;
    }
}
