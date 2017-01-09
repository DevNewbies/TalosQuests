package gr.devian.talosquests.backend.Controllers;

import com.google.common.base.Strings;
import gr.devian.talosquests.backend.Models.ServiceInfo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nikolas on 13/11/2016.
 */

@RestController
@RequestMapping("/")
public class ServiceInfoController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public ServiceInfo getServiceInfo() {
        String remoteAddr = "";
        if (Strings.isNullOrEmpty(request.getHeader("X-Forwarded-For"))) {
            remoteAddr = request.getRemoteAddr();
        }
        else {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }
        serviceInfo.setRemoteAddr(remoteAddr);
        return serviceInfo;
    }

    @RequestMapping(path = "/Error/500")
    public ResponseEntity<Object> fail() throws Exception {
        throw new Exception("Random Error");
    }
}
