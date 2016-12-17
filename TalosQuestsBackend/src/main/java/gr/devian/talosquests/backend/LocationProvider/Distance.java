package gr.devian.talosquests.backend.LocationProvider;

import java.io.Serializable;

/**
 * Created by Nikolas on 3/12/2016.
 */
public class Distance implements Serializable {
    public long inMeters;
    public String humanReadable;

    public Distance() {
    }

    public String toString() {
        return this.humanReadable;
    }
    public Distance(com.google.maps.model.Distance dist) {
        inMeters = dist.inMeters;
        humanReadable = dist.humanReadable;
    }
}