package gr.devian.talosquests.backend.LocationProvider;

import gr.devian.talosquests.backend.Models.Quest;

import java.io.Serializable;

/**
 * Created by Nikolas on 5/12/2016.
 */
public class Location implements Serializable {
    private Duration duration;
    private Distance distance;

    public Location(Duration duration, Distance distance) {
        this.duration = duration;
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public Distance getDistance() {
        return distance;
    }


}

