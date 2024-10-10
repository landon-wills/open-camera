package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;


@Mixin(Camera.class)
public class CameraMixin implements Rollable {

    @Shadow private Entity entity;
    @Shadow @Final private Quaternionf rotation;
    @Shadow @Final private Vector3f forwards;
    @Shadow @Final private Vector3f up;
    @Shadow @Final private Vector3f left;
    @Shadow private float xRot;
    @Shadow private float yRot;
    @Unique Matrix3f orientation = new Matrix3f();

    @Override
    public Matrix3f getOrientation() {
        return orientation;
    }

    @Override
    public void updateOrientation(Supplier<Matrix3f> update) {
        this.orientation = update.get();
    }

    @Inject(method = "setRotation", at = @At("HEAD"), cancellable = true)
    public void open_camera$setRotation(float f, float g, CallbackInfo ci) {
        if (this.entity instanceof Rollable rollable) {
            this.orientation = rollable.getOrientation();

            this.rotation.set(this.orientation.normal(new Matrix3f()).getNormalizedRotation(new Quaternionf()));
            this.forwards.set(getForwardVector());
            this.up.set(getUpVector());
            this.left.set(getLeftVector());

            ci.cancel();
        }
    }

}
