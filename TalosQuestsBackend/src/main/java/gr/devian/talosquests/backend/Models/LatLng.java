package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Views.View;

import java.io.Serializable;


/**
 * A place on Earth, represented by a Latitude/Longitude pair.
 */

public class LatLng implements Serializable {

    @JsonView(View.Simple.class)
    private double lat;

    @JsonView(View.Simple.class)
    private double lng;

    public LatLng(com.google.maps.model.LatLng latlng) {
        lat = latlng.lat;
        lng = latlng.lng;
    }

    public LatLng() {
        this(new com.google.maps.model.LatLng(0,0));
    }
    public LatLng(Double lat,Double lng) {
        this.lat=lat;
        this.lng=lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public static com.google.maps.model.LatLng getLatLng(LatLng latlng) {
        return new com.google.maps.model.LatLng(latlng.lat, latlng.lng);
    }

    @Override
    public String toString() {
        return getLatLng(this).toString();
    }


}