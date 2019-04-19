package hadeel.com.popularmovies1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> reviews;
    Context context;


    public ReviewAdapter(ArrayList<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        Review review = reviews.get(i);
        View oneItem = LayoutInflater.from(context)
                .inflate(R.layout.one_review_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(oneItem);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Review review= reviews.get(i);
        String author_ = review.getAuthor();
        String content_ = review.getContent();
        viewHolder.author.setText(author_);
        viewHolder.content.setText(content_);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView author;
        TextView content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            content = itemView.findViewById(R.id.content);
        }
    }
}
