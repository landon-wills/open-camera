package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import com.mojang.math.Axis;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements Rollable {

    @Unique private Quaternionf orientation = new Quaternionf();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        // Proof of concept for yaw movement
        if (this.getMainHandItem().is(Items.STICK)) {
            Axis.YP.rotationDegrees((float)2).mul(this.orientation, this.orientation);
        }
        else if (this.getMainHandItem().is(Items.BLAZE_ROD)) {
            Axis.YP.rotationDegrees((float)-2).mul(this.orientation, this.orientation);
        }
    }

    @Override
    public void absMoveTo(double x, double y, double z, float yaw, float pitch) {
        this.absMoveTo(x, y, z);
        this.setYRot(yaw % 360.0f);
        this.setXRot(pitch % 360.0f);
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public Quaternionf getOrientation() {
        return orientation;
    }

    @Override
    public void updateOrientation(Supplier<Quaternionf> update) {
        this.orientation = update.get();
    }

}
