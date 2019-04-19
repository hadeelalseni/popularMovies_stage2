package hadeel.com.popularmovies1;


import android.arch.persistence.room.RoomDatabase;

//@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
@android.arch.persistence.room.Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();

}
