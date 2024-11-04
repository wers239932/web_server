package validation;

public class Validator {
    @Validate(minX = -3, maxX = 5, minY = -5, maxY = -3, minR = 1, maxR = 5)
    public static boolean validate(int x, int y, int R) throws NoSuchMethodException {
        Validate annotation = Validator.class.getMethod("validate", int.class, int.class, int.class).getAnnotation(Validate.class);
        int minX = annotation.minX();
        int minY = annotation.minY();
        int minR = annotation.minR();
        int maxX = annotation.maxX();
        int maxY = annotation.maxY();
        int maxR = annotation.maxR();
        return !(x<minX || x>maxX || y<minY || y>maxY || R<minR || R>maxR);
    }

    public static Boolean check(Integer x, Integer y, Integer R) {
        if (x >= 0 && y >= 0) return x <= R && y <= R / 2;
        if (x >= 0 && y < 0) return (x + y) * 2 <= R * 3;
        if (x < 0 && y < 0) return x * x + y * y <= R * R;
        return false;
    }
}
