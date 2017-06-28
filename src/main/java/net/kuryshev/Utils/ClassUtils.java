package net.kuryshev.Utils;

/**
 * Created by 1 on 27.06.2017.
 */
public class ClassUtils {

    public static String getClassName() {
        try {
            throw new Exception();
        } catch (Exception e) {
            return e.getStackTrace()[1].getClassName();
        }
    }
}
