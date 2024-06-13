package ca.landonjw.mixin.client;

import ca.landonjw.Rollable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private Camera mainCamera;

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 2
            )
    )
    public void open_camera$removeVanillaRotation(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        if (mainCamera instanceof Rollable) {
            poseStack.pushPose();
        }
    }

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 3,
                    shift = At.Shift.AFTER
            )
    )
    public void open_camera$applyCameraRotations(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        if (mainCamera instanceof Rollable rollable) {
            poseStack.popPose();
            poseStack.mulPose(rollable.getOrientation());
        }
    }

}
