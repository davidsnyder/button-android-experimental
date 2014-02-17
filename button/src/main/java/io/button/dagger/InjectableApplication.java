package io.button.dagger;

public interface InjectableApplication {
    /**
     * Used by Android components to inject themselves in the graph
     *
     * @param o
     */
    public void inject(Object o);
}
