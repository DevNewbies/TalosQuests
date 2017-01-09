package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Xrysa on 18/12/2016.
 */
public class TalosQuestsAccessViolationException extends TalosQuestsException {
    public TalosQuestsAccessViolationException() {
        super("Tried to access a resource without permission.");
    }
    public TalosQuestsAccessViolationException(String message) {
        super(message);
    }
}
