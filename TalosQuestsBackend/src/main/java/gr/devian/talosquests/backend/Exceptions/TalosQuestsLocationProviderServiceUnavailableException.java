package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Nikolas on 4/12/2016.
 */
public class TalosQuestsLocationProviderServiceUnavailableException extends TalosQuestsException {
    public TalosQuestsLocationProviderServiceUnavailableException() {
        super("LocationProvider Provider is unavailable.");
    }
}
