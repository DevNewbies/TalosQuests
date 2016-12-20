package gr.devian.talosquests.backend.LocationProvider;

import java.io.Serializable;

/**
 * Created by Nikolas on 3/12/2016.
 */
public class Duration  implements Serializable {
    public long inSeconds;
    public String humanReadable;

    public Duration(com.google.maps.model.Duration dur) {
        inSeconds = dur.inSeconds;
        humanReadable = dur.humanReadable;
    }
}