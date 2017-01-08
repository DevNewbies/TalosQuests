package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Nikolas on 6/1/2017.
 */
public class TalosQuestsLocationsNotAvailableException extends TalosQuestsException {
    public TalosQuestsLocationsNotAvailableException() {
        super("No available quests found for your Location.");
    }
}
