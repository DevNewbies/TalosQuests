package gr.devian.talosquests.backend.Utilities;

import gr.devian.talosquests.backend.Models.QuestChoice;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
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
