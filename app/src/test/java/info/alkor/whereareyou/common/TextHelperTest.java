package info.alkor.whereareyou.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Text helper tests.
 * Created by Marlena on 2017-12-30.
 */
public class TextHelperTest {
    @Test
    public void formatPhone() throws Exception {
        TextHelper helper = new TextHelper();
        assertEquals("+48 123 456 789", helper.formatPhone("+48123456789"));
    }
}