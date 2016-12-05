package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Nikolas on 4/12/2016.
 */
public class TalosQuestsGameNotInProgressException extends TalosQuestsException {
    public TalosQuestsGameNotInProgressException() {
        super("There is not any game in progress.");
    }
}
