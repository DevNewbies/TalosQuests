package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Views.View;

import java.io.Serializable;

/**
 * Created by Nikolas on 3/12/2016.
 */
public class Distance implements Serializable {
    @JsonView(View.Simple.class)
    public long inMeters;

    @JsonView(View.Simple.class)
    public String humanReadable;
    
    public Distance(com.google.maps.model.Distance dist) {
        inMeters = dist.inMeters;
        humanReadable = dist.humanReadable;
    }
}