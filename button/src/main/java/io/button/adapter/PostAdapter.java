package io.button.adapter;

import java.util.Arrays;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import io.button.models.*;
import io.button.R;

public class PostAdapter extends ParseQueryAdapter<Post> {

    public PostAdapter(Context context, ParseQueryAdapter.QueryFactory<Post> postQueryFactory) {
        super(context, postQueryFactory);
    }

    @Override
    public View getItemView(Post post, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_posts, null);
        }

        super.getItemView(post, v, parent);

        ParseImageView postImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile photoFile = post.getParseFile("image");
        if (photoFile != null) {
            postImage.setParseFile(photoFile);
            postImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }

        TextView idTextView = (TextView) v.findViewById(R.id.post_id);
        idTextView.setText(post.getObjectId());
//        TextView titleTextView = (TextView) v.findViewById(R.id.text1);
//        titleTextView.setText(post.getTitle());
//        TextView ratingTextView = (TextView) v
//                .findViewById(R.id.favorite_meal_rating);
//        ratingTextView.setText(post.getRating());
        return v;
    }

}