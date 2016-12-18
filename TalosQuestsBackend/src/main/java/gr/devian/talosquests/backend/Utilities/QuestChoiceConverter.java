package gr.devian.talosquests.backend.Utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import gr.devian.talosquests.backend.Models.QuestChoice;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Nikolas on 17/12/2016.
 */
@Converter
public class QuestChoiceConverter extends JsonConverter<QuestChoice> implements AttributeConverter<QuestChoice, String> {

    public QuestChoiceConverter() {
        super(QuestChoice.class);
    }

}