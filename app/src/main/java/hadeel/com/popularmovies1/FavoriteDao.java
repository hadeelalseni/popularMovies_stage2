package hadeel.com.popularmovies1;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM Favorite")
    LiveData <List<Favorite>> getAll();

    @Insert
    Long insertFav(Favorite favorite);

}
