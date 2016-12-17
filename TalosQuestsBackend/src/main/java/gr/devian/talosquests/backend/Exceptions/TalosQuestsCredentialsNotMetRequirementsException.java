package gr.devian.talosquests.backend.Exceptions;

/**
 * Created by Nikolas on 7/12/2016.
 */
public class TalosQuestsCredentialsNotMetRequirementsException extends TalosQuestsException {
    private String field;
    private String pattern;

    public String getField() {
        return field;
    }

    public TalosQuestsCredentialsNotMetRequirementsException(String field, String pattern) {
        super("Credential " + field + " Not Met Requirements");
        this.field = field;
        this.pattern = pattern;

    }

    public String getPattern() {
        return pattern;
    }
}
