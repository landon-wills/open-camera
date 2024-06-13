package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow @Final private Minecraft minecraft;

    @WrapWithCondition(
            method = "turnPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"
            )
    )
    public boolean open_camera$modifyRotation(LocalPlayer player, double cursorDeltaX, double cursorDeltaY) {
        if (!(player instanceof Rollable rollable)) return true;

        var pitch = cursorDeltaY * 0.15f;
        var roll = cursorDeltaX * 0.15f;

        var pitchQ = Axis.XP.rotationDegrees((float)pitch);
        var rollQ = Axis.ZP.rotationDegrees((float)roll);

        pitchQ.mul(rollQ).mul(rollable.getOrientation(), rollable.getOrientation());
        return false;
    }

}
