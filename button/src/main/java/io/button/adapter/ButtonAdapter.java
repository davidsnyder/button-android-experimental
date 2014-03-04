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

import io.button.models.Button;
import io.button.R;

public class ButtonAdapter extends ParseQueryAdapter<Button> {

    public ButtonAdapter(Context context, ParseQueryAdapter.QueryFactory<Button> buttonQueryFactory) {
        super(context, buttonQueryFactory);
    }

    @Override
    public View getItemView(Button button, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_buttons, null);
        }

        super.getItemView(button, v, parent);

        ParseImageView buttonImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile photoFile = button.getParseFile("photo");
        if (photoFile != null) {
            buttonImage.setParseFile(photoFile);
            buttonImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }

        TextView idTextView = (TextView) v.findViewById(R.id.button_id);
        idTextView.setText(button.getObjectId());
//        TextView ratingTextView = (TextView) v
//                .findViewById(R.id.favorite_meal_rating);
//        ratingTextView.setText(button.getRating());
        return v;
    }

}