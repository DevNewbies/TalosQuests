package gr.devian.talosquests.backend.LocationProvider;

import java.io.Serializable;

/**
 * Created by Nikolas on 3/12/2016.
 */
public class Duration  implements Serializable {
    public long inSeconds;

    public Duration(com.google.maps.model.Duration dur) {
        inSeconds = dur.inSeconds;
    }
    public Duration(long secs) {
        inSeconds = secs;
    }
    public Duration() {
        inSeconds = 0;
    }
}