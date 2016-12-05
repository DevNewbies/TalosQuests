package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Models.ResponseModel;
import org.apache.catalina.connector.Response;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Set;

/**
 * Created by Nikolas on 14/11/2016.
 */
@ControllerAdvice
@RestController
public class ExceptionHandlerController extends ResponseEntityExceptionHandler implements ErrorController {

    public static final String DEFAULT_ERROR_VIEW = "error";


    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(final Exception ex,
                                                        final WebRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(ex.getMessage(), 500));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>(ResponseModel.CreateFailModel("Bad Request", 400), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(ex.getMessage(), 500));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(ex.getMessage(), 500));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(ex.getMessage(), 500));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(ex.getMessage(), 500));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String var = ex.getParameterName();
        if (var.equals("token")) {
            return new ResponseEntity<Object>(ResponseModel.CreateFailModel("You need to provide a Token to access this private resource.", 401), HttpStatus.UNAUTHORIZED);

        } else if (var.equals("password")) {
            return new ResponseEntity<Object>(ResponseModel.CreateFailModel("You need to provide a Password to access this private resource.", 401), HttpStatus.UNAUTHORIZED);

        } else {
            System.out.println(var);
            return new ResponseEntity<Object>(ResponseModel.CreateFailModel("Missing " + var + " Parameter.", 401), HttpStatus.BAD_REQUEST);

        }
    }
    /*@Override
    public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String var = ex.getVariableName();

        if (var.equals("token")) {
            System.out.println("Token");
            return new ResponseEntity<Object>(ResponseModel.CreateFailModel("You need to provide a Token to access this private resource.", 401), HttpStatus.UNAUTHORIZED);

        } else {
            System.out.println(var);
            return new ResponseEntity<Object>(ResponseModel.CreateFailModel("Missing " + var + " Parameter.", 401), HttpStatus.BAD_REQUEST);

        }
    }*/

    @RequestMapping(value = "/error")
    public ResponseEntity<ResponseModel<String>> error() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseModel.CreateFailModel("Method not Found", 404));
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
