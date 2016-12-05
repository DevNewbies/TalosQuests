package gr.devian.talosquests.backend.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Nikolas on 5/12/2016.
 */
public abstract class BaseController {
    /**
     * The Logger for this class.
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());


}
