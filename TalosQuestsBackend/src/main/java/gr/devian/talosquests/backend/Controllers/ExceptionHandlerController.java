package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Utilities.Response;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by Nikolas on 14/11/2016.
 */

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(final Exception ex,
                                                        final WebRequest req) {
        return Response.fail(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder sb = new StringBuilder();
        for (String s : ex.getSupportedMethods()) {
            sb.append(s + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return Response.fail("Bad Request - Method Not Supported for Path: " + request.getContextPath() + ". Supported Methods: " + sb.toString() + ".", HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return Response.fail("Path " + headers.getLocation() + " is not registered in the service", HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return Response.fail(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return Response.fail("Bad Request - Path Variable Type Mismatch.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String var = ex.getParameterName();
        if (var.equals("token"))
            return Response.fail("You need to provide a Token to access this private resource.", HttpStatus.UNAUTHORIZED);
        else if (var.equals("password"))
            return Response.fail("You need to provide a Password to access this private resource.", HttpStatus.UNAUTHORIZED);
        else
            return Response.fail("Missing Parameter: " + var + ".", HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return Response.fail("Path " + ex.getRequestURL() + " is not registered in the service", HttpStatus.NOT_FOUND);
    }
}
