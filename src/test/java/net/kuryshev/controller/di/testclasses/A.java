package net.kuryshev.controller.di.testclasses;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.controller.di.Inject;

/**
 * Created by 1 on 27.06.2017.
 */
public class A extends DependencyInjectionServlet {

    @Inject("fieldA1")
    private ClassField fieldA1;
    @Inject("fieldA2")
    protected ClassField fieldA2;
    @Inject("fieldA3")
    public ClassField fieldA3;
    @Inject("fieldA4")
    ClassField fieldA4;

    public ClassField getFieldA1() {
        return fieldA1;
    }

    public void setFieldA1(ClassField fieldA1) {
        this.fieldA1 = fieldA1;
    }

    public ClassField getFieldA2() {
        return fieldA2;
    }

    public void setFieldA2(ClassField fieldA2) {
        this.fieldA2 = fieldA2;
    }

    public ClassField getFieldA3() {
        return fieldA3;
    }

    public void setFieldA3(ClassField fieldA3) {
        this.fieldA3 = fieldA3;
    }

    public ClassField getFieldA4() {
        return fieldA4;
    }

    public void setFieldA4(ClassField fieldA4) {
        this.fieldA4 = fieldA4;
    }
}
