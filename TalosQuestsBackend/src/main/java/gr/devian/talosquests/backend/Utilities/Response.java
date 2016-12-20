package gr.devian.talosquests.backend.Utilities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import org.springframework.data.annotation.Transient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Nikolas on 20/11/2016.
 */
public class Response<T> {
    private HttpStatus state;
    private String message;
    private T response;

    @Transient
    @JsonIgnore
    public HttpStatus getStateObj() {
        return state;
    }

    public void setStateObj(HttpStatus state) {
        this.state = state;
    }

    public int getState() {
        return state.value();
    }

    public void setState(int state) {
        this.state = HttpStatus.valueOf(state);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public static <E> ResponseEntity<Object> success(E data, String message) {
        return Response.success(data, HttpStatus.OK, message);
    }

    public static <E> ResponseEntity<Object> success(E data, int State, String message) {
        return Response.success(data, HttpStatus.valueOf(State), message);
    }

    public static <E> ResponseEntity<Object> success(E data) {
        return Response.success(data, 200);
    }

    public static <E> ResponseEntity<Object> success(E data, int State) {
        return Response.success(data, HttpStatus.valueOf(State), "");
    }

    public static <E> ResponseEntity<Object> success(E data, HttpStatus State) {
        return Response.success(data, State, "");
    }

    public static <E> ResponseEntity<Object> success(E data, HttpStatus State, String message) {
        Response<E> resp = new Response<E>();
        resp.setMessage(Strings.isNullOrEmpty(message) ? "Success." : message);
        resp.setState(State.value());
        resp.setResponse(data);
        return new ResponseEntity<>(resp, State);
    }

    public static <E> ResponseEntity<Object> fail(String Message, int State) {
        return Response.fail(Message, HttpStatus.valueOf(State));
    }

    public static <E> ResponseEntity<Object> fail(String Message, HttpStatus State) {
        Response<E> resp = new Response<E>();
        resp.setMessage(Message);
        resp.setState(State.value());
        resp.setResponse(null);
        return new ResponseEntity(resp, State);
    }

}
