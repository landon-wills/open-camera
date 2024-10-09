package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.joml.Vector3f;
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

        rollable.updateOrientation(() -> {
            var pitch = - cursorDeltaY * 0.015f;
            var roll = - cursorDeltaX * 0.015f;

            return rollable.getOrientation()
                    .rotate((float) pitch, new Vector3f(1, 0, 0))
                    .rotate((float) roll, new Vector3f(0, 0, 1));
        });
        return false;
    }

}
