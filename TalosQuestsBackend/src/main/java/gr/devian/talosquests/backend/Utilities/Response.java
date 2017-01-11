package gr.devian.talosquests.backend.Utilities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Nikolas on 20/11/2016.
 */
public class Response<T> {
    @JsonView(View.Simple.class)
    private HttpStatus state;

    @JsonView(View.Simple.class)
    private String message;

    @JsonView(View.Simple.class)
    private Date timestamp;

    @JsonView(View.Simple.class)
    private T response;

    @JsonView(View.Internal.class)
    private Class<? extends View.Simple> view;

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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T object) {
        response = object;
    }

    @JsonIgnore
    public JsonNode getFilteredResponse() {
        ObjectMapper mapper = new ObjectMapper();
        MappingJacksonValue value = new MappingJacksonValue(this);
        value.setSerializationView(view);
        try {
            String temp = mapper.writerWithView(view).writeValueAsString(this);
            JsonNode node = mapper.readTree(temp);
            return node;
        } catch (Exception e) {
            return null;
        }
    }

    public Class<? extends View.Simple> getView() {
        return view;
    }


    @JsonView(View.Extended.class)
    public String getViewName() {
        return view.getSimpleName();
    }

    public void setView(Class<? extends View.Simple> view) {
        this.view = view;
    }

    /* Success Without View */

    public static <E> ResponseEntity<Object> success(E data, String message) {
        return Response.success(data, View.Simple.class, HttpStatus.OK, message);
    }

    public static <E> ResponseEntity<Object> success(E data, int State, String message) {
        return Response.success(data, View.Simple.class, HttpStatus.valueOf(State), message);
    }

    public static <E> ResponseEntity<Object> success(E data) {
        return Response.success(data, 200);
    }

    public static <E> ResponseEntity<Object> success(E data, int State) {
        return Response.success(data, View.Simple.class, HttpStatus.valueOf(State), "");
    }

    public static <E> ResponseEntity<Object> success(E data, HttpStatus State) {
        return Response.success(data, View.Simple.class, State, "Success");
    }

    public static <E> ResponseEntity<Object> success(E data, HttpStatus State, String message) {
        return Response.success(data, View.Simple.class, State, message);
    }

    /* Success With View */

    public static <E> ResponseEntity<Object> success(E data, Class<? extends View.Simple> view, String message) {
        return Response.success(data, view, HttpStatus.OK, message);
    }

    public static <E> ResponseEntity<Object> success(E data, Class<? extends View.Simple> view, int State, String message) {
        return Response.success(data, view, HttpStatus.valueOf(State), message);
    }

    public static <E> ResponseEntity<Object> success(E data, Class<? extends View.Simple> view) {
        return Response.success(data, view, 200);
    }

    public static <E> ResponseEntity<Object> success(E data, Class<? extends View.Simple> view, int State) {
        return Response.success(data, view, HttpStatus.valueOf(State), "");
    }

    public static <E> ResponseEntity<Object> success(E data, Class<? extends View.Simple> view, HttpStatus State) {
        return Response.success(data, view, State, "Success");
    }

    public static <E> ResponseEntity<Object> success(E data, Class<? extends View.Simple> view, HttpStatus State, String message) {
        Response<E> resp = new Response<E>();
        resp.setMessage(Strings.isNullOrEmpty(message) ? "Success." : message);
        resp.setState(State.value());
        resp.setView(view);
        resp.setResponse(data);
        resp.setTimestamp(new Date());
        return new ResponseEntity<>(resp.getFilteredResponse(), State);
    }

    /* Fail Without View */

    public static <E> ResponseEntity<Object> fail(String Message, HttpStatus State) {
        return Response.fail(Message, View.Simple.class, State);
    }

    public static <E> ResponseEntity<Object> fail(String Message, int State) {
        return Response.fail(Message, View.Simple.class, HttpStatus.valueOf(State));
    }

     /* Fail With View */

    public static <E> ResponseEntity<Object> fail(String Message, Class<? extends View.Simple> view, int State) {
        return Response.fail(Message, view, HttpStatus.valueOf(State));
    }

    public static <E> ResponseEntity<Object> fail(String Message, Class<? extends View.Simple> view, HttpStatus State) {
        Response<E> resp = new Response<E>();
        resp.setMessage(Message);
        resp.setView(view);
        resp.setState(State.value());
        resp.setResponse(null);
        resp.setTimestamp(new Date());
        return new ResponseEntity(resp.getFilteredResponse(), State);
    }

}
