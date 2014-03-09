package io.button.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.parse.*;
import io.button.R;
import io.button.models.*;

public class ButtonLinkAdapter extends ParseQueryAdapter<ButtonLink> {

    public ButtonLinkAdapter(Context context, ParseQueryAdapter.QueryFactory<ButtonLink> buttonLinkQueryFactory) {
        super(context, buttonLinkQueryFactory);
    }

    @Override
    public View getItemView(ButtonLink buttonLink, View v, ViewGroup parent) {

        Button button = (Button)buttonLink.getParseObject("button");
        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_buttons, null);
        }

        super.getItemView(buttonLink, v, parent);

        v.setTag(buttonLink.getObjectId());

//        ParseImageView buttonImage = (ParseImageView) v.findViewById(R.id.icon);
//        ParseFile photoFile = button.getParseFile("photo");
//        if (photoFile != null) {
//            buttonImage.setParseFile(photoFile);
//            buttonImage.loadInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    // nothing to do
//                }
//            });
//        }

//        ToggleButton followToggle = (ToggleButton) v.findViewById(R.id.toggle_follow);
//
//        followToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // The toggle is enabled
//                } else {
//                    // The toggle is disabled
//                }
//            }
//        });

        TextView idTextView = (TextView) v.findViewById(R.id.button_id);
        idTextView.setText(button.getObjectId());

//        TextView ratingTextView = (TextView) v
//                .findViewById(R.id.favorite_meal_rating);
//        ratingTextView.setText(button.getRating());
        return v;
    }

}