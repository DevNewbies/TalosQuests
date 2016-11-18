package gr.devian.talosquests.backend;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Nikolas on 13/11/2016.
 */

@RestController
@RequestMapping("/ServiceInfo")
public class ServiceInfoController {

    @Autowired(required = true)
    private HttpServletRequest request;


    @RequestMapping(value="" , method = RequestMethod.GET)
    public ServiceInfo getServiceInfo() {
        return new ServiceInfo(request.getRemoteAddr());
    }
}
