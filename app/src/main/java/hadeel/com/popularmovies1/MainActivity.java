package hadeel.com.popularmovies1;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Movie> movies;
    private Adapter adapter;
    TextView result;
    String url = "https://api.themoviedb.org/3/movie/popular?api_key=d90f7dfb03a5c10cbd61fc6ce5f62df0&page=1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.result);
        new AsyncTask().execute(url);

    }

    class AsyncTask extends android.os.AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            result.setText("onPreExecute()");
        }

        @Override
        protected String doInBackground(String... strings) {
            result.setText("doInBackground");
            URL url_ = null;
            try{
                url_ = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                HttpURLConnection connection = (HttpURLConnection) url_.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String urlString = bufferedReader.readLine();
                bufferedReader.close();
                connection.disconnect();
                return urlString;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String urlString) {
            super.onPostExecute(urlString);
            JSONObject jsonObject = null;
            try{
                jsonObject = new JSONObject(urlString);
                ArrayList<Movie> movies = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for(int i =0 ; i<jsonArray.length(); i++ ) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.optString("title");
                    String poster = object.optString("poster_path");
                    String date = object.optString("release_date");
                    double vote = object.optDouble("vote_average");
                    String overview = object.optString("overview");
                    String id = object.optString("id");
                    final Movie movie = new Movie(title, poster, date, vote, overview, id);
                    movies.add(movie);

                }

                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                adapter = new Adapter(MainActivity.this, movies);
                recyclerView.setAdapter(adapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                recyclerView.setLayoutManager(gridLayoutManager);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.checkPopular: {
                new AsyncTask().execute("https://api.themoviedb.org/3/movie/popular?api_key=d90f7dfb03a5c10cbd61fc6ce5f62df0&page=1");
                break;
            }
            case R.id.checkTopRated: {
                new AsyncTask().execute("https://api.themoviedb.org/3/movie/top_rated?api_key=d90f7dfb03a5c10cbd61fc6ce5f62df0&page=1");
                break;
            }
            case R.id.checkFav: {
                Repository repository = new Repository(getApplicationContext());
                repository.getFavorites().observe(MainActivity.this, new Observer<List<Favorite>>() {
                    @Override
                    public void onChanged(@Nullable List<Favorite> favorites) {
                        for(Favorite favorite : favorites){
                            new AsyncTask().execute("https://api.themoviedb.org/3/search/movie?api_key=d90f7dfb03a5c10cbd61fc6ce5f62df0&query="+favorite.getName());
                            System.out.println("FAFFFAFAFFFF: "+ favorite.getName());
                            break;
                        }

                    }
                });
            }


        }
        return true;
    }
}
