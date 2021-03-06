package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Views.View;

import java.io.Serializable;

/**
 * Created by Nikolas on 5/12/2016.
 */
public class Location implements Serializable {
    @JsonView(View.Simple.class)
    private Duration duration;

    @JsonView(View.Simple.class)
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

