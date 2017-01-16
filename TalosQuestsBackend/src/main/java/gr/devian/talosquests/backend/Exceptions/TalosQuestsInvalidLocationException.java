package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Nikolas on 16/1/2017.
 */
public class TalosQuestsInvalidLocationException extends TalosQuestsException {
    public TalosQuestsInvalidLocationException() {
        super("Location provided is invalid");
    }
}
