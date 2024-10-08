package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
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
    @Unique Quaternionf orientation = new Quaternionf();

    @Override
    public Quaternionf getOrientation() {
        return orientation;
    }

    @Override
    public void updateOrientation(Supplier<Quaternionf> update) {
        this.orientation = update.get();
    }

    @Inject(method = "setRotation", at = @At("HEAD"), cancellable = true)
    public void open_camera$setRotation(float f, float g, CallbackInfo ci) {
        if (this.entity instanceof Rollable rollable) {
            this.orientation = rollable.getOrientation();
            this.rotation.set(this.orientation.conjugate(new Quaternionf()));
            this.forwards.set(0.0F, 0.0F, 1.0F).rotate(this.rotation);
            this.up.set(0.0F, 1.0F, 0.0F).rotate(this.rotation);
            this.left.set(1.0F, 0.0F, 0.0F).rotate(this.rotation);
            ci.cancel();
        }
    }

}
