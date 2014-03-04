package io.button.models;

import io.button.models.*;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Follow")
public class Follow extends ParseObject {

    public Follow() {
    }

    public Follow(Button button, ParseUser user) {
        put("button", button);
        put("user", user);
    }

}