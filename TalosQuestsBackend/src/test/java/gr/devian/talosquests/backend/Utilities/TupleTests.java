package gr.devian.talosquests.backend.Utilities;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Nikolas on 20/12/2016.
 */
public class TupleTests {
    @Test
    public void TestTuple() {
        Tuple<Integer,Integer> tuple1 = new Tuple(1,2);
        Tuple<Integer,Integer> tuple2 = new Tuple(1,2);
        Tuple<Integer,Integer> tuple3 = new Tuple(2,2);

        int hash = tuple1.hashCode();

        assertEquals((Integer)tuple1.left,(Integer)1);
        assertEquals((Integer)tuple1.right,(Integer)2);

        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertTrue(tuple1.equals(tuple1));
        assertEquals(tuple1.toString(),tuple2.toString());

        assertFalse(tuple1.equals(null));
        assertFalse(tuple1.equals(5));

    }
}
