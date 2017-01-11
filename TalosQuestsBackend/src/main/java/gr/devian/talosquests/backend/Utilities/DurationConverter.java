package gr.devian.talosquests.backend.Utilities;

import gr.devian.talosquests.backend.Models.Duration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by Nikolas on 17/12/2016.
 */
@Converter
public class DurationConverter extends JsonConverter<Duration> implements AttributeConverter<Duration,String> {
    public DurationConverter() {
        super(Duration.class);
    }

}
