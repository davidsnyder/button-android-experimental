package io.button.models;

import io.button.models.*;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public Post() {
        // A default constructor is required.
    }

    public String getPostBody() {
        return getString("postBody");
    }

    public void setPostBody(String postBody) {
        put("postBody", postBody);
    }

    public ParseUser getOwner() {
        return null;
    }

    public void setOwner(ParseUser user) {
        //    put getParseObject("buttonClaim").getParseUser("user", user);
    }

    public ParseUser getCreator() {
        return null;
    }

    public void setCreator(ParseUser user) {

    }

    public ParseFile getPhotoFile() {
        return getParseFile("image");
    }

    public void setPhotoFile(ParseFile file) {
        put("image", file);
    }

    public void setButton(io.button.models.Button button) {

    }

    public io.button.models.Button getButton() {
        return null;
    }

}
