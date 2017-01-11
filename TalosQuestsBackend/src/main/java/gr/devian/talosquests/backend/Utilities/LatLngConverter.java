package gr.devian.talosquests.backend.Utilities;

import gr.devian.talosquests.backend.Models.LatLng;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by Nikolas on 17/12/2016.
 */
@Converter
public class LatLngConverter extends JsonConverter<LatLng> implements AttributeConverter<LatLng, String> {
    public LatLngConverter() {
        super(LatLng.class);
    }
}
