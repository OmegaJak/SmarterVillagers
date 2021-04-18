package omegajak.smartervillagers.ai.brain;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import omegajak.smartervillagers.SmarterVillagers;

import java.util.stream.Stream;

public class SmarterVillagerMemoryModules {
    public static final MemoryModuleType<PlayerEntity> LEADER = MemoryModuleType.register(SmarterVillagers.id("leader").toString());
    public static final MemoryModuleType<Long> STARTED_FOLLOWING_TIME = MemoryModuleType.register(SmarterVillagers.id("started_following_time").toString());

    public static void init() {
        Stream<MemoryModuleType<?>> customModules = getAll();
        VillagerEntity.MEMORY_MODULES = ImmutableList.copyOf(Stream.concat(VillagerEntity.MEMORY_MODULES.stream(), customModules).iterator());
    }

    private static Stream<MemoryModuleType<?>> getAll() {
        return Stream.of(LEADER, STARTED_FOLLOWING_TIME);
    }
}
