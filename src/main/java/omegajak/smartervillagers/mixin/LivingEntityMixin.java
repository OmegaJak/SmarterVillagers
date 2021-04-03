package omegajak.smartervillagers.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;
import omegajak.smartervillagers.SmarterVillagers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    private boolean descendLadders = false;

    @Shadow public abstract boolean isClimbing();

    @ModifyVariable(at = @At("HEAD"), method = "applyClimbingSpeed(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", ordinal = 0)
    private Vec3d modifyClimbingSpeedMotionParameter(Vec3d motion) {
        if (SmarterVillagers.getCurrentConfig().avoidGettingStuckOnLadders && this.isClimbing() && ((Object)this instanceof MobEntity)) {
            MobEntity mobEntity = ((MobEntity)(Object)this);
            EntityNavigation entityNavigation = mobEntity.getNavigation();
            if (entityNavigation.isIdle() && mobEntity.verticalCollision && !mobEntity.isOnGround()) {
                descendLadders = true;
            } else if (!entityNavigation.isIdle() && mobEntity.isOnGround()) {
                descendLadders = false;
            }

            if (descendLadders) {
                return new Vec3d(motion.x, -0.2, motion.z);
            }
        }

        return motion;
    }
}
