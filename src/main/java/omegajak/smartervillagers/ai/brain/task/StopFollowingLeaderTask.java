package omegajak.smartervillagers.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import omegajak.smartervillagers.SmarterVillagers;
import omegajak.smartervillagers.ai.brain.SmarterVillagerMemoryModules;

import java.util.Optional;

public class StopFollowingLeaderTask extends Task<VillagerEntity> {

    public StopFollowingLeaderTask() {
        super(ImmutableMap.of(SmarterVillagerMemoryModules.LEADER, MemoryModuleState.VALUE_PRESENT, SmarterVillagerMemoryModules.STARTED_FOLLOWING_TIME, MemoryModuleState.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        Optional<Long> startedFollowingOptional = entity.getBrain().getOptionalMemory(SmarterVillagerMemoryModules.STARTED_FOLLOWING_TIME);
        return startedFollowingOptional.isPresent() && hasExceededDuration(world, startedFollowingOptional.get());
    }

    @Override
    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        entity.getBrain().forget(SmarterVillagerMemoryModules.LEADER);
        entity.getBrain().forget(SmarterVillagerMemoryModules.STARTED_FOLLOWING_TIME);
        entity.getBrain().refreshActivities(world.getTimeOfDay(), world.getTime());
    }

    private boolean hasExceededDuration(ServerWorld world, Long startedFollowingTime) {
        return world.getTime() - startedFollowingTime > SmarterVillagers.getCurrentConfig().followButtonConfig.followDuration;
    }
}
