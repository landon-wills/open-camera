package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.joml.Matrix3f;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements Rollable {

    @Shadow @Final private static Logger LOGGER;
    @Unique private Matrix3f orientation = new Matrix3f();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (this.shouldRoll()) {
            // Proof of concept for yaw movement
            if (this.getMainHandItem().is(Items.STICK)) {
                this.rotateYaw(2);
            }
            else if (this.getMainHandItem().is(Items.BLAZE_ROD)) {
                this.rotateYaw(-2);
            }
        }
    }

    @Override
    public void absMoveTo(double x, double y, double z, float yaw, float pitch) {
        if (shouldRoll()) {
            this.absMoveTo(x, y, z);
            this.setYRot(yaw % 360.0f);
            this.setXRot(pitch % 360.0f);
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }

    @Override
    public Matrix3f getOrientation() {
        return orientation;
    }

    @Override
    public Rollable updateOrientation(Function<Matrix3f, Matrix3f> update) {
        if (!shouldRoll()) return this;

        if (this.orientation == null) {
            this.orientation = new Matrix3f();
            this.rotate(getYRot() - 180, getXRot(), 0);
        }
        this.orientation = update.apply(this.orientation);
        setYRot(this.getYaw());
        setXRot(this.getPitch());
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        return this;
    }

    @Override
    public boolean shouldRoll() {
        return this.getMainHandItem().is(Items.DIAMOND);
    }

    @Override
    public void clearRotation() {
        this.orientation = null;
    }
}
