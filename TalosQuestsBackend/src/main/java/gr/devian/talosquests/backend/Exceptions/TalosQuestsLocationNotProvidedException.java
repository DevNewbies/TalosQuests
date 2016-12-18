package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Xrysa on 18/12/2016.
 */
public class TalosQuestsLocationNotProvidedException extends TalosQuestsException {
    public TalosQuestsLocationNotProvidedException() {
        super("User have null Location. Location must be provided in order to create a game for the user.");
    }
}
