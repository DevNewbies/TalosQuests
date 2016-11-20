package gr.devian.talosquests.backend;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Created by Nikolas on 14/11/2016.
 */
@ControllerAdvice
@RestController
public class ExceptionHandlerController implements ErrorController {

    public static final String DEFAULT_ERROR_VIEW = "error";
    @ExceptionHandler(Exception.class)
    public ErrorHandlerSchema defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception{
        ErrorHandlerSchema t = new ErrorHandlerSchema(e);

        return t;
    }

    @RequestMapping(value = "/error")
    public ErrorHandlerSchema error() {
        ErrorHandlerSchema t = new ErrorHandlerSchema(new NoHandlerFoundException("a","a",null));

        return t;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}