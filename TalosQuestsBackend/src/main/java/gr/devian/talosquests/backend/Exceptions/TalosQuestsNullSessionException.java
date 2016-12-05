package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Xrysa on 5/12/2016.
 */
public class TalosQuestsNullSessionException extends TalosQuestsException {
    public TalosQuestsNullSessionException() {
        super("Session cannot be null");
    }
}
