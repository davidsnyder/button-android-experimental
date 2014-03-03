package io.button;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseACL;
import io.button.models.*;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import io.button.dagger.AndroidModule;
import io.button.dagger.ButtonModule;
import io.button.dagger.InjectableApplication;

public class ButtonApplication extends Application implements InjectableApplication {
    private ObjectGraph objectGraph;

    public void onCreate() {
        super.onCreate();

        String parseApplicationId = getResources().getString(R.string.parse_application_id);
        String parseClientKey = getResources().getString(R.string.parse_client_key);

        // We initialize our application with Parse
        Parse.initialize(this, parseApplicationId, parseClientKey);

        // Register Parse subclasses
        ParseObject.registerSubclass(io.button.models.Button.class);
        ParseObject.registerSubclass(io.button.models.Post.class);

        // FIXME: Restrict write/read access
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        // Initalize our object graph for Dagger
        initDependencyInjection();
    }

    /**
     * Used by Android components to inject themselves in the graph
     *
     * @param o
     */
    public void inject(Object o) {
        objectGraph.inject(o);
    }

    /**
     * Initializes our object graph
     */
    private void initDependencyInjection() {
        objectGraph = ObjectGraph.create(getModules().toArray());
        objectGraph.injectStatics();
        objectGraph.inject(this);
    }

    /**
     * @return Return a list of object which are actually the modules that would
     *         be included in the Dagger object graph.
     */
    private List<Object> getModules() {
        return Arrays.<Object> asList(new Object[]{
                new ButtonModule(),
                new AndroidModule(this)
        });
    }
}
