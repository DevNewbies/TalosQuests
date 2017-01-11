package gr.devian.talosquests.backend.Utilities;

import gr.devian.talosquests.backend.Models.AccessLevel;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Nikolas on 12/1/2017.
 */
public class AccessLevelTests {

    @Test
    public void CoverageImprovementsAccessLevel() {
        AccessLevel accessLevel1 = new AccessLevel();
        accessLevel1.setName("test");
        accessLevel1.setCanManageService(true);

        assertEquals(accessLevel1.getName(),"test");
        assertTrue(accessLevel1.getCanManageService());
    }
}
