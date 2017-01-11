package gr.devian.talosquests.backend.Controllers.Management;

import gr.devian.talosquests.backend.Utilities.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.ArrayList;

/**
 * Created by Nikolas on 10/1/2017.
 */
@RestController
public class ServiceController extends AdminController {
    @RequestMapping("/endpoints")
    public ResponseEntity<Object> ListEndPoints() {
        return Response.success(handlerMapping.getHandlerMethods().keySet());
    }

}
