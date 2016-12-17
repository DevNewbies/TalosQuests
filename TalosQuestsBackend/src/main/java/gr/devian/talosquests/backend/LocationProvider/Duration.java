package gr.devian.talosquests.backend.LocationProvider;

import java.io.Serializable;

/**
 * Created by Nikolas on 3/12/2016.
 */
public class Duration extends com.google.maps.model.Duration implements Serializable {
    public Duration(com.google.maps.model.Duration dur) {
        inSeconds = dur.inSeconds;
        humanReadable = dur.humanReadable;
    }
}