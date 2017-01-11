package gr.devian.talosquests.backend.Utilities;

import com.fasterxml.jackson.databind.node.ObjectNode;
import gr.devian.talosquests.backend.Views.View;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Nikolas on 11/1/2017.
 */
public class ResponseTests {
    @Test
    public void  coverageImprovementResponseSuccess() {

        ResponseEntity<Object> response = Response.success(true);
        response = Response.success(true, View.Simple.class);
        response = Response.success(true, View.Simple.class, 200);
        response = Response.success(true, View.Simple.class, HttpStatus.OK);
        response = Response.success(true, View.Simple.class, HttpStatus.OK, "Ok");
        response = Response.success(true, View.Simple.class, 200, "Ok");
        response = Response.success(true, View.Simple.class, "Ok");
        response = Response.success(true, 201);
        response = Response.success(true, HttpStatus.CREATED);
        response = Response.success(true, "Ok");
        response = Response.success(true, HttpStatus.OK, "Ok");

        response = Response.success(new temp());
    }

    private class temp {

    }

    @Test
    public void coverageImprovementResponseFail() {
        ResponseEntity<Object> response = Response.fail("Error", HttpStatus.FORBIDDEN);
        response = Response.fail("Error", 404);
        response = Response.fail("Error", View.Internal.class, 404);
        response = Response.fail("Error", View.Internal.class, HttpStatus.BAD_REQUEST);

    }
}
