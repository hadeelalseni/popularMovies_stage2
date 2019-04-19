package hadeel.com.popularmovies1;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class Repository {
    private Database database;
    public Repository(Context context){
        database = Room.databaseBuilder(context, Database.class, "Favoraites_DB").build();

    }
    public void insertFav(String name){
        Favorite favorite = new Favorite();
        //favorite.setId(id);
        favorite.setName(name);
        insertFav(favorite);
    }

    public void insertFav(final Favorite favorite) {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                database.favoriteDao().insertFav(favorite);
                return null;
            }
        }.execute();
    }
    public LiveData<List<Favorite>> getFavorites(){
        return database.favoriteDao().getAll();
    }
}
