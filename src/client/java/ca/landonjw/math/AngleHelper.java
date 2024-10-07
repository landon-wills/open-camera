package ca.landonjw.math;

public class AngleHelper {

    public static float degrees(float radians) {
        return (float) (radians * (180 / Math.PI));
    }

    public static float radians(float degrees) {
        return (float) (degrees / (180 / Math.PI));
    }

}
