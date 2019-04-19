package hadeel.com.popularmovies1;

import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

public class DetailActivity extends AppCompatActivity {

    private ImageView poster;
    private TextView title;
    private TextView date;
    private TextView vote;
    private TextView overview;
    private TextView movieReview;
    private String id;
    private ReviewAdapter adapter;
    private Button watchVidBtn;
    private ImageView star;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        poster = (ImageView) findViewById(R.id.moviePoster);
        title = (TextView) findViewById(R.id.movieTitle);
        date = (TextView) findViewById(R.id.movieDate);
        vote = (TextView) findViewById(R.id.movieVote);
        overview = (TextView) findViewById(R.id.movieOverview);
        movieReview = (TextView) findViewById(R.id.movieReview);
        watchVidBtn = (Button) findViewById(R.id.watchVidBtn);
        star = (ImageView) findViewById(R.id.star);

        Intent intent = getIntent();
        final Movie movie = (Movie) intent.getExtras().getSerializable("MovieDetails");

        if (movie != null) {
            Picasso.with(this)
                    .load(movie.getPoster())
                    .into(poster);
            title.setText(movie.getTitle());
            date.setText(movie.getReleaseDate());
            vote.setText(Double.toString(movie.getVoteAverage()));
            overview.setText(movie.getPlotSynopsis());
            id = movie.getId();


            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    star.setBackgroundResource(R.drawable.ic_star_black_24dp);
                    Repository repository = new Repository(DetailActivity.this);
                    repository.insertFav(movie.getTitle());


                }
            });


            String reviewURL = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=d90f7dfb03a5c10cbd61fc6ce5f62df0";
            String videoURL = "http://api.themoviedb.org/3/movie/"+ id + "/videos?api_key=d90f7dfb03a5c10cbd61fc6ce5f62df0";
            new AsyncTask().execute(reviewURL, videoURL);

        }
    }

    class AsyncTask extends android.os.AsyncTask<String, Void, Wrapper> {

        private String makeConnection(URL url) {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
        protected void onPreExecute() {
            super.onPreExecute();
            //result.setText("onPreExecute()");
        }

        @Override
        protected Wrapper doInBackground(String... strings) {
            URL reviewURL = null;
            URL videoURL = null;
            try {
                reviewURL = new URL(strings[0]);
                videoURL = new URL(strings[1]);
                System.out.println("reviewURL in background: " + reviewURL);
                System.out.println("videoURL in background: " + videoURL);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //try {

/*                HttpURLConnection connection = (HttpURLConnection) reviewURL.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String urlString = bufferedReader.readLine();

                String vid = bufferedReader.readLine();
                bufferedReader.close();
                connection.disconnect();

*/

            String reviewURLs = makeConnection(reviewURL);
            String videoURLs = makeConnection(videoURL);

            System.out.println("return reviewURLs: " + reviewURLs);
            System.out.println("return VID videoURLs: " + videoURLs);
            Wrapper wrapper = new Wrapper(reviewURLs, videoURLs);
            return wrapper;
            /*} catch (IOException e) {
                System.out.println("Catch here");
                e.printStackTrace();
            }
            return null;*/

    }
        @Override
        protected void onPostExecute(final Wrapper wrapper) {
            super.onPostExecute(wrapper);
            System.out.println("WRAPPER R:         "+wrapper.getReviewURL());
            System.out.println("WRAPPER V:         "+wrapper.getVideoURL());
            watchVidBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String vid_id = null;
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(wrapper.getVideoURL());
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            vid_id = object.optString("id");
                        }

                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + vid_id));
                        System.out.println("vid_id: "+vid_id);
                        try {
                            startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            startActivity(webIntent);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            });
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(wrapper.getReviewURL());
                ArrayList<Review> reviews = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String author = object.optString("author");
                    String content = object.optString("content");
                    final Review review = new Review(author, content);
                    reviews.add(review);

                }

                RecyclerView recyclerView = findViewById(R.id.recyclerViewReview);
                adapter = new ReviewAdapter(reviews, DetailActivity.this);
                recyclerView.setAdapter(adapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailActivity.this, 1);
                recyclerView.setLayoutManager(gridLayoutManager);

                //movieReview.setText((CharSequence) reviews);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class Wrapper{
        private String reviewURL;
        private String videoURL;


        public Wrapper(String reviewURL, String videoURL) {
            this.reviewURL = reviewURL;
            this.videoURL = videoURL;
        }

        public String getReviewURL() {
            return reviewURL;
        }

        public void setReviewURL(String reviewURL) {
            this.reviewURL = reviewURL;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public void setVideoURL(String videoURL) {
            this.videoURL = videoURL;
        }
    }
}
