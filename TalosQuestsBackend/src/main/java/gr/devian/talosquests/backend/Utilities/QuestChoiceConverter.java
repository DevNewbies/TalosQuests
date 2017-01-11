package gr.devian.talosquests.backend.Utilities;

import gr.devian.talosquests.backend.Models.QuestChoice;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by Nikolas on 17/12/2016.
 */
@Converter
public class QuestChoiceConverter extends JsonConverter<QuestChoice> implements AttributeConverter<QuestChoice, String> {

    public QuestChoiceConverter() {
        super(QuestChoice.class);
    }

}