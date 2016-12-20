package gr.devian.talosquests.backend.Utilities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Nikolas on 20/12/2016.
 */

public class SecurityToolsTests {

    @Test
    public void testEncodeWhenEncodingInstanceIsMD5() {
        new SecurityTools();
        assertNotNull(SecurityTools.MD5("test"));
        assertEquals(SecurityTools.MD5("test"),SecurityTools.Encode("test","MD5"));
    }

    @Test
    public void testEncodeWhenEncodingInstanceIsInvalid() {
        assertNull(SecurityTools.Encode("test","test"));
    }


}
