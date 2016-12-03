package gr.devian.talosquests.backend.Models;

/**
 * Created by Nikolas on 20/11/2016.
 */
public class ResponseModel<T> {
    private int state;
    private String message;
    private T response;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public static <E> ResponseModel<E> CreateSuccessModel(E data) {
        ResponseModel<E> resp = new ResponseModel<E>();
        resp.setMessage("success");
        resp.setState(200);
        resp.setResponse(data);
        return resp;
    }
    public static <E> ResponseModel<E> CreateSuccessModel(E data, int State) {
        ResponseModel<E> resp = new ResponseModel<E>();
        resp.setMessage("success");
        resp.setState(State);
        resp.setResponse(data);
        return resp;
    }
    public static <E> ResponseModel<E> CreateFailModel(String Message, int State) {
        ResponseModel<E> resp = new ResponseModel<E>();
        resp.setMessage(Message);
        resp.setState(State);
        resp.setResponse(null);
        return resp;
    }
}
