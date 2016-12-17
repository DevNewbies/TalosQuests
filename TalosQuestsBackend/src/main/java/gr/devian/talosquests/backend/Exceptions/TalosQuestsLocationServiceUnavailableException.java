package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Nikolas on 15/12/2016.
 */
public class TalosQuestsLocationServiceUnavailableException extends TalosQuestsException {
    public TalosQuestsLocationServiceUnavailableException() {
        super("Location Service is unavailable at the moment. Your Request cannot be completed.");
    }
}
