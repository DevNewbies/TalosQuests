package gr.devian.talosquests.backend;

/**
 * Created by Nikolas on 14/11/2016.
 */
public class ErrorHandlerSchema {
    private Exception exc;

    public ErrorHandlerSchema(Exception e) {
        exc =e;
    }

    public String getException()  {
        return exc.toString();
    }
}
