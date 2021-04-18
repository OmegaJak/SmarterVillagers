package omegajak.smartervillagers.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import omegajak.smartervillagers.ai.brain.SmarterVillagerMemoryModules;

public class LookAtLeaderTask extends Task<VillagerEntity> {
    private final float maxDistanceSquared;

    public LookAtLeaderTask(float maxDistance) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_ABSENT, SmarterVillagerMemoryModules.LEADER, MemoryModuleState.VALUE_PRESENT));
        this.maxDistanceSquared = maxDistance * maxDistance;
    }

    @Override
    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        Brain<?> brain = entity.getBrain();

        brain.getOptionalMemory(SmarterVillagerMemoryModules.LEADER).ifPresent(leader -> {
            if (entity.squaredDistanceTo(leader) <= (double)this.maxDistanceSquared) {
                brain.remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(leader, true));
            }
        });
    }
}
