package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Models.ServiceInfo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nikolas on 13/11/2016.
 */

@RestController
@RequestMapping("/")
public class ServiceInfoController {

    @Autowired(required = true)
    private HttpServletRequest request;


    @RequestMapping(method = RequestMethod.GET)
    public ServiceInfo getServiceInfo() {
        return new ServiceInfo(request.getHeader("X-Forwarded-For"));
    }
}
