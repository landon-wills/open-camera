package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import ca.landonjw.math.Smoother;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow @Final private Minecraft minecraft;

    @Unique
    Smoother pitchSmoother = new Smoother(30);
    @Unique
    Smoother rollSmoother = new Smoother(30);

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
            var pitch = pitchSmoother.smooth((float) (cursorDeltaY * 0.15f), 1);
            var roll = rollSmoother.smooth((float) (cursorDeltaX * 0.15f), 1);

            // This has side effects so either remove return or make a duplicate
            return rollable.rotate(0.0F, pitch, roll).getOrientation();
        });
        return false;
    }

}
