package gr.devian.talosquests.backend.Controllers;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Models.ServiceInfo;
import gr.devian.talosquests.backend.Services.GameService;
import gr.devian.talosquests.backend.Services.LocationService;
import gr.devian.talosquests.backend.Services.QuestService;
import gr.devian.talosquests.backend.Services.UserService;
import gr.devian.talosquests.backend.Views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Nikolas on 5/12/2016.
 */
public abstract class BaseController {
    @Autowired
    protected UserService userService;

    @Autowired
    protected GameService gameService;

    @Autowired
    protected LocationService locationService;

    @Autowired
    protected QuestService questService;

    @Autowired(required = true)
    protected HttpServletRequest request;

    @Autowired
    protected ServiceInfo serviceInfo;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};

    protected String getClientIpAddress() {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

}
