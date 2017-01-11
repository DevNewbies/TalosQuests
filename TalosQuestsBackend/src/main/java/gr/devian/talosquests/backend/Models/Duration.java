package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Views.View;

import java.io.Serializable;

/**
 * Created by Nikolas on 3/12/2016.
 */
public class Duration  implements Serializable {
    @JsonView(View.Simple.class)
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