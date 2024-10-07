package ca.landonjw;

import org.joml.Quaternionf;

import java.util.function.Supplier;

public interface Rollable {

    Quaternionf getOrientation();

    void updateOrientation(Supplier<Quaternionf> update);

}