package ca.landonjw;

import org.joml.Matrix3f;
import org.joml.Quaternionf;

import java.util.function.Supplier;

public interface Rollable {

    Matrix3f getOrientation();

    void updateOrientation(Supplier<Matrix3f> update);

}