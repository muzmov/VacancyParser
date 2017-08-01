package net.kuryshev.utils;

public class ClassUtils {

    public static String getClassName() {
        try {
            throw new Exception();
        } catch (Exception e) {
            return e.getStackTrace()[1].getClassName();
        }
    }
}
