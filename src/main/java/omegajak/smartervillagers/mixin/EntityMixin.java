package omegajak.smartervillagers.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;
import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow @Final protected Random random;

    @Shadow public abstract UUID getUuid();

    @Shadow public abstract BlockPos getBlockPos();
}