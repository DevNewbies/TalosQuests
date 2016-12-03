package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Models.ResponseModel;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nikolas on 14/11/2016.
 */
@ControllerAdvice
@RestController
public class ExceptionHandlerController implements ErrorController {

    public static final String DEFAULT_ERROR_VIEW = "error";
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel<String>> defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception{
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.CreateFailModel(e.getMessage(),500));
    }

    @RequestMapping(value = "/error")
    public ResponseEntity<ResponseModel<String>> error() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseModel.CreateFailModel("Method not Found",404));
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}