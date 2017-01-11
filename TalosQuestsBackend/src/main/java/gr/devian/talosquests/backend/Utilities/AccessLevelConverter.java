package gr.devian.talosquests.backend.Utilities;

import gr.devian.talosquests.backend.Models.AccessLevel;

import javax.persistence.AttributeConverter;

/**
 * Created by Nikolas on 9/1/2017.
 */
public class AccessLevelConverter extends JsonConverter<AccessLevel> implements AttributeConverter<AccessLevel, String> {
    public AccessLevelConverter() {
        super(AccessLevel.class);
    }
}
