package gr.devian.talosquests.backend.LocationProvider;

import gr.devian.talosquests.backend.Models.Quest;

import java.io.Serializable;

/**
 * Created by Nikolas on 5/12/2016.
 */
public class Location implements Serializable {
    private Duration duration;
    private Distance distance;
    private LatLng location;

    public Location(Duration duration, Distance distance, LatLng location) {
        this.duration = duration;
        this.distance = distance;
        this.location = location;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}

