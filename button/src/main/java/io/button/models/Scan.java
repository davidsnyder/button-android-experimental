package io.button.models;

import io.button.models.*;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseGeoPoint;

@ParseClassName("Scan")
public class Scan extends ParseObject {

    public Scan() {
    }

    public Scan(Button button, ParseUser user) {
       put("button", button);
       put("user", user);
    }

    public void setLocation(ParseGeoPoint location) {
        put("location", location);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }
}