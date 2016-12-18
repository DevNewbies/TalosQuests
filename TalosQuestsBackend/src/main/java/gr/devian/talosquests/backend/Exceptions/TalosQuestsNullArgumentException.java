package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Xrysa on 18/12/2016.
 */
public class TalosQuestsNullArgumentException extends TalosQuestsException {
    public TalosQuestsNullArgumentException(String field) {
        super("A Required argument ("+field+") was null. Process cannot be continued.");
    }
}
