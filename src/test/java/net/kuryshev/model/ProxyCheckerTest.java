package net.kuryshev.model;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ProxyCheckerTest {

    @Test
    @Ignore
    public void isOkNull() {
        Assert.assertTrue(ProxyChecker.isOk(null));
    }
}