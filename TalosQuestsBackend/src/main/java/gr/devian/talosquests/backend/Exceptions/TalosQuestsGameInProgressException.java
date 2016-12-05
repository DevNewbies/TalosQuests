package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Nikolas on 4/12/2016.
 */
public class TalosQuestsGameInProgressException extends TalosQuestsException {
    public TalosQuestsGameInProgressException() {
        super("There a game in progress.");
    }
}
