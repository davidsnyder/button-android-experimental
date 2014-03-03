package io.button.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import android.util.Log;

@ParseClassName("Button")
public class Button extends ParseObject {

    public Button() {
        // A default constructor is required.
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public boolean hasOwner() {
        return getOwner() != null;
    }

    public ParseUser getOwner() {
        ParseObject buttonClaim = getParseObject("buttonClaim");
        if (buttonClaim != null) {
            return buttonClaim.getParseUser("user");
        } else { return null; }
    }

    public void setOwner(ParseUser user) {
        if (!this.hasOwner()) {
            ParseObject buttonClaim = new ParseObject("ButtonClaim");
            buttonClaim.put("user", user);
            this.put("buttonClaim", buttonClaim);
        }
    }

    public int getColor() {
        return getInt("color");
    }

    public ParseFile getPhotoFile() {
        return getParseFile("image");
    }

    public void setPhotoFile(ParseFile file) {
        put("image", file);
    }

    public void wasScanned() {
        //create a new Bump/Scan
    }

}
