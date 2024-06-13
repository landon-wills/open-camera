package ca.landonjw;

import org.joml.Quaternionf;

public interface Rollable {

    Quaternionf getOrientation();

    void setOrientation(Quaternionf orientation);

}
