package net.kuryshev.controller.di;

import net.kuryshev.controller.di.testclasses.ClassFieldImpl1;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by 1 on 27.06.2017.
 */
public class ApplicationContextTest {
    @Test
    public void getContext_EmptyProperties_Test() throws IOException {
        ApplicationContext appCtx = new ApplicationContext();
        appCtx.init("src/test/resources/empty.properties");
        Assert.assertNull(appCtx.getBean("randomName"));
    }

    @Test
    public void getContext_CorrectProperties_Test() throws IOException {
        ApplicationContext appCtx = new ApplicationContext();
        appCtx.init("src/test/resources/correct.properties");
        Assert.assertEquals(appCtx.getBean("fieldA1").getClass(), ClassFieldImpl1.class);
        Assert.assertEquals(appCtx.getBean("fieldA2").getClass(), ClassFieldImpl1.class);
        Assert.assertEquals(appCtx.getBean("fieldA3").getClass(), ClassFieldImpl1.class);
        Assert.assertEquals(appCtx.getBean("fieldA4").getClass(), ClassFieldImpl1.class);
        Assert.assertEquals(appCtx.getBean("fieldB1").getClass(), ClassFieldImpl1.class);
        Assert.assertEquals(appCtx.getBean("fieldB2").getClass(), ClassFieldImpl1.class);
        Assert.assertEquals(appCtx.getBean("fieldB3").getClass(), ClassFieldImpl1.class);
        Assert.assertEquals(appCtx.getBean("fieldB4").getClass(), ClassFieldImpl1.class);

    }

    @Test(expected = IOException.class)
    public void getContext_NoPropertiesFile_Test() throws IOException{
        ApplicationContext appCtx = new ApplicationContext();
        appCtx.init("src/test/resources/emptyy.properties");
    }
}