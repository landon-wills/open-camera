package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements Rollable {

    @Unique private Quaternionf orientation = new Quaternionf();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void turn(double deltaY, double deltaX) {
        float adjustedDeltaX = (float)deltaX * 0.15f;
        float adjustedDeltaY = (float)deltaY * 0.15f;
        this.setXRot(this.getXRot() + adjustedDeltaX);
        this.setYRot(this.getYRot() + adjustedDeltaY);
        this.xRotO += adjustedDeltaX;
        this.yRotO += adjustedDeltaY;
        if (this.getVehicle() != null) {
            this.getVehicle().onPassengerTurned(this);
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
    public void setOrientation(Quaternionf orientation) {
        this.orientation = orientation;
    }
}
