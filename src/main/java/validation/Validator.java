package validation;

import messaging.PointWithScale;

public class Validator {

    public static Boolean check(PointWithScale point) {
        int x = point.x;
        int y = point.y;
        int R = point.r;
        if (x >= 0 && y >= 0) return x <= R && y <= R / 2;
        if (x >= 0 && y < 0) return (x + y) * 2 <= R * 3;
        if (x < 0 && y < 0) return x * x + y * y <= R * R;
        return false;
    }

    private static Boolean validateParam(int param, int min, int max) {
        return param > min && param < max;
    }

    public static boolean validate(PointWithScale point) {
        try {
            Validatable annotationX = point.getClass().getDeclaredField("x").getAnnotation(Validatable.class);
            Validatable annotationY = point.getClass().getDeclaredField("y").getAnnotation(Validatable.class);
            Validatable annotationR = point.getClass().getDeclaredField("r").getAnnotation(Validatable.class);
            return validateParam(point.x, annotationX.min(), annotationX.max()) &&
                    validateParam(point.y, annotationY.min(), annotationY.max()) &&
                    validateParam(point.r, annotationR.min(), annotationR.max());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
