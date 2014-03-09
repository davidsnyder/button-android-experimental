package io.button.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import io.button.models.*;

@ParseClassName("ButtonLink")
public class ButtonLink extends ParseObject {

    public ButtonLink() {
    }

    public ButtonLink(Button button, ParseUser user) {
        put("button", button);
        put("user", user);
    }

    public void setFollow(boolean follow) {
        put("follow", follow);
    }

    public io.button.models.Button getButton() {
        return (io.button.models.Button)getParseObject("button");
    }

}