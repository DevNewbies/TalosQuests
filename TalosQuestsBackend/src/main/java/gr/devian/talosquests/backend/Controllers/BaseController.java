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
    protected org.apache.catalina.servlet4preview.http.HttpServletRequest request;

    @Autowired
    protected ServiceInfo serviceInfo;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


}
