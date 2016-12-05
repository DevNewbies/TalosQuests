package gr.devian.talosquests.backend.LocationProvider;

import java.io.Serializable;


/**
 * A place on Earth, represented by a Latitude/Longitude pair.
 */

public class LatLng extends com.google.maps.model.LatLng implements Serializable {


    public LatLng(com.google.maps.model.LatLng latlng) {
        super(latlng.lat, latlng.lng);
    }


    public static com.google.maps.model.LatLng getLatLng(LatLng latlng) {
        return new com.google.maps.model.LatLng(latlng.lat, latlng.lng);
    }
}