package net.kuryshev.model;

import org.junit.Assert;
import org.junit.Test;

public class ProxyCheckerTest {

    @Test
    public void isOkNull() {
        Assert.assertTrue(ProxyChecker.isOk(null));
    }
}