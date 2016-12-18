package gr.devian.talosquests.backend.Utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.QuestChoice;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

/**
 * Created by Nikolas on 17/12/2016.
 */
@Converter
public class LatLngConverter extends JsonConverter<LatLng> implements AttributeConverter<LatLng, String> {
    public LatLngConverter() {
        super(LatLng.class);
    }
}
