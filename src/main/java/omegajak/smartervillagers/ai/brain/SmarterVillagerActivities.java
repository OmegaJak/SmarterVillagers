package omegajak.smartervillagers.ai.brain;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.task.GoTowardsLookTarget;
import net.minecraft.entity.ai.brain.task.RandomTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.WaitTask;
import net.minecraft.entity.passive.VillagerEntity;
import omegajak.smartervillagers.SmarterVillagers;
import omegajak.smartervillagers.ai.brain.task.LookAtLeaderTask;
import omegajak.smartervillagers.ai.brain.task.StopFollowingLeaderTask;

public class SmarterVillagerActivities {
    public static final Activity FOLLOW_LEADER_ACTIVITY = Activity.register(SmarterVillagers.id("follow_leader").toString());

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> createFollowTasks() {
        float f = 0.5f;
        return ImmutableList.of(
            Pair.of(0, new StopFollowingLeaderTask()),
            Pair.of(1, new RandomTask(ImmutableList.of(
                Pair.of(new LookAtLeaderTask(16.0f), 3),
                Pair.of(new GoTowardsLookTarget(f, 2), 3)))
            ),
            Pair.of(8, new WaitTask(30, 60))
        );
    }
}
