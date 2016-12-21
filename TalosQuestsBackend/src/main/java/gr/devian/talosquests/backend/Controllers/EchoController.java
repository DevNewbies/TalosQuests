package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.Utilities.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Nikolas on 20/12/2016.
 */
@RestController
public class EchoController extends BaseController {
    @RequestMapping(path = "/Echo/{echoPath}", method = RequestMethod.GET)
    public ResponseEntity<Object> echo(@RequestParam(value = "echoParam", required = true) String echoParam, @PathVariable("echoPath") String echoPath) {
        return Response.success(new Echo(echoPath, echoParam));
    }

    private class Echo {
        private String path;
        private String param;

        public Echo(String path, String param) {
            this.path = path;
            this.param = param;
        }

        public String getPath() {
            return path;
        }

        public String getParam() {
            return param;
        }
    }
}
