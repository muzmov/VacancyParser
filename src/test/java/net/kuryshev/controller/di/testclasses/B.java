package net.kuryshev.controller.di.testclasses;

import net.kuryshev.controller.di.Inject;

/**
 * Created by 1 on 27.06.2017.
 */
public class B extends A {

    @Inject("fieldB1")
    private ClassField fieldB1;
    @Inject("fieldB2")
    protected ClassField fieldB2;
    @Inject("fieldB3")
    public ClassField fieldB3;
    @Inject("fieldB4")
    ClassField fieldB4;

    public ClassField getFieldB1() {
        return fieldB1;
    }

    public void setFieldB1(ClassField fieldB1) {
        this.fieldB1 = fieldB1;
    }

    public ClassField getFieldB2() {
        return fieldB2;
    }

    public void setFieldB2(ClassField fieldB2) {
        this.fieldB2 = fieldB2;
    }

    public ClassField getFieldB3() {
        return fieldB3;
    }

    public void setFieldB3(ClassField fieldB3) {
        this.fieldB3 = fieldB3;
    }

    public ClassField getFieldB4() {
        return fieldB4;
    }

    public void setFieldB4(ClassField fieldB4) {
        this.fieldB4 = fieldB4;
    }
}
