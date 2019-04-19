package hadeel.com.popularmovies1;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Movie> moviesList;
    Context context;

    ImageView posterClick;


    public Adapter(Context context, ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
         Movie movie = moviesList.get(i);
        View oneItem = LayoutInflater.from(context)
                .inflate(R.layout.one_item_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(oneItem);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Choosed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context ,  DetailActivity.class);
                intent.putExtra("MovieDetails",moviesList.get(i));
                context.startActivity(intent);
            }
        });

        Picasso.with(context)
                .load(moviesList.get(i).getPoster())
                .into(viewHolder.poster);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            poster = itemView.findViewById(R.id.one_item_layout_img);



        }
    }
}
