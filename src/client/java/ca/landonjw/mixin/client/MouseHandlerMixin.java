package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.SmoothDouble;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow @Final private Minecraft minecraft;
    @Unique SmoothDouble pitchSmoother = new SmoothDouble();
    @Unique SmoothDouble rollSmoother = new SmoothDouble();

    @WrapWithCondition(
            method = "turnPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"
            )
    )
    public boolean open_camera$modifyRotation(LocalPlayer player, double cursorDeltaX, double cursorDeltaY, @Local(argsOnly = true) double d) {
        if (!(player instanceof Rollable rollable)) return true;
        if (!rollable.shouldRoll()) {
            pitchSmoother.reset();
            rollSmoother.reset();
            return true;
        }

        var pitch = pitchSmoother.getNewDeltaValue(cursorDeltaY * 0.15f, d);
        var roll = rollSmoother.getNewDeltaValue(cursorDeltaX * 0.15f, d);
        rollable.rotate(0.0F, (float)pitch, (float)roll);
        return false;
    }

    @Inject(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MouseHandler;isMouseGrabbed()Z", ordinal = 0))
    private void open_camera$maintainMovementWhenInScreens(CallbackInfo ci, @Local(ordinal = 1) double e) {
        if (minecraft.player == null) return;
        if (!(minecraft.player instanceof Rollable rollable)) return;
        if (!rollable.shouldRoll()) return;
        if (minecraft.isPaused()) return;

        var pitch = pitchSmoother.getNewDeltaValue(0, e);
        var roll = rollSmoother.getNewDeltaValue(0, e);
        rollable.rotate(0.0F, (float)pitch, (float)roll);
    }

}
