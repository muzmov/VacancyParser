package net.kuryshev.controller.di;

import net.kuryshev.controller.di.testclasses.A;
import net.kuryshev.controller.di.testclasses.B;
import net.kuryshev.controller.di.testclasses.ClassFieldImpl1;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by 1 on 27.06.2017.
 */
public class DependencyInjectionServletTest {
    @Test
    public void init_WithInheritance_Test() throws Exception {
        B b = new B();
        Class clazz = B.class.getSuperclass().getSuperclass();
        Field f = clazz.getDeclaredField("PROPERTIES_FILENAME");
        f.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        f.set(null, "src/test/resources/correct.properties");
        b.init();
        Assert.assertNotNull(b.getFieldB1());
        Assert.assertNotNull(b.getFieldB2());
        Assert.assertNotNull(b.getFieldB3());
        Assert.assertNotNull(b.getFieldB4());
        Assert.assertNotNull(b.getFieldA1());
        Assert.assertNotNull(b.getFieldA2());
        Assert.assertNotNull(b.getFieldA3());
        Assert.assertNotNull(b.getFieldA4());
    }

    @Test
    public void init_WithoutInheritance_Test() throws Exception {
        A a = new A();
        Class clazz = B.class.getSuperclass().getSuperclass();
        Field f = clazz.getDeclaredField("PROPERTIES_FILENAME");
        f.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        f.set(null, "src/test/resources/correct.properties");
        a.init();
        Assert.assertNotNull(a.getFieldA1());
        Assert.assertNotNull(a.getFieldA2());
        Assert.assertNotNull(a.getFieldA3());
        Assert.assertNotNull(a.getFieldA4());
        Assert.assertEquals(a.getFieldA1().getClass(), ClassFieldImpl1.class);
    }
}