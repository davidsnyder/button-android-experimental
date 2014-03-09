package io.button.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Button")
public class Button extends ParseObject {

    public Button() {
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
        return getParseUser("owner");
    }

    public void setOwner(ParseUser user) {
        put("owner", user);
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

}
