package gr.devian.talosquests.backend.Controllers.Management;

import gr.devian.talosquests.backend.Controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by Nikolas on 9/1/2017.
 */
@RestController
@RequestMapping("/Admin")
public abstract class AdminController extends BaseController {
    protected RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public void EndpointDocController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

}
