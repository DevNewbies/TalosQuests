package gr.devian.talosquests.backend.Utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.devian.talosquests.backend.LocationProvider.Duration;
import gr.devian.talosquests.backend.Models.QuestChoice;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nikolas on 17/12/2016.
 */
@Converter
public class QuestChoiceCollectionConverter extends JsonConverter<ArrayList<QuestChoice>> implements AttributeConverter<ArrayList<QuestChoice>, String> {

    public QuestChoiceCollectionConverter() {
        super((Class<ArrayList<QuestChoice>>) new ArrayList<QuestChoice>().getClass());
    }
}
