package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin implements Rollable {

    @Shadow private Entity entity;
    @Unique Quaternionf orientation = new Quaternionf();

    @Override
    public Quaternionf getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Quaternionf orientation) {
        this.orientation = orientation;
    }

    @Inject(method = "setRotation", at = @At("HEAD"))
    public void open_camera$setRotation(float f, float g, CallbackInfo ci) {
        if (this.entity instanceof Rollable rollable) {
            this.orientation = rollable.getOrientation();
        }
    }

    @Inject(method = "setup", at = @At("TAIL"))
    public void open_camera$setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
//        if (bl && bl2) {
//            this.roll = -roll;
//        }
    }

}
