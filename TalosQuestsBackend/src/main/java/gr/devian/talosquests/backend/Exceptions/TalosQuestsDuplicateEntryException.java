package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Nikolas on 16/1/2017.
 */
public class TalosQuestsDuplicateEntryException extends TalosQuestsException {
    public TalosQuestsDuplicateEntryException(String field) {
        super("Entry with this " + field + " already exists!");
    }
}
