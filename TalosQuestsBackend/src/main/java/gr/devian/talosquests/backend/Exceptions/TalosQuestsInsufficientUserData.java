package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Nikolas on 7/12/2016.
 */
public class TalosQuestsInsufficientUserData extends TalosQuestsException {
    public TalosQuestsInsufficientUserData() {
        super("One or more fields were empty while expected to had value.");
    }
}
