package gr.devian.talosquests.backend;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nikolas on 13/11/2016.
 */
@RestController

public class ServiceInfoController {

    @Autowired(required = true)
    private HttpServletRequest request;

    @RequestMapping("/ServiceInfo")
    public ServiceInfo ServiceInfo() {
        return new ServiceInfo(request.getRemoteAddr());
    }
}
