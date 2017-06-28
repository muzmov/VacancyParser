package net.kuryshev.Utils;

import org.junit.Assert;
import org.junit.Test;

import static net.kuryshev.Utils.ClassUtils.getClassName;
import static org.junit.Assert.*;

/**
 * Created by 1 on 27.06.2017.
 */
public class ClassUtilsTest {
    @Test
    public void getClassNameTest() throws Exception {
        Assert.assertEquals(getClassName(), this.getClass().getName());
    }

}