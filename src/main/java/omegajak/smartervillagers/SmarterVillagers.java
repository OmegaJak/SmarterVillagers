package omegajak.smartervillagers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import omegajak.smartervillagers.ai.brain.SmarterVillagerMemoryModules;
import omegajak.smartervillagers.config.ModConfig;
import omegajak.smartervillagers.network.Networking;

public class SmarterVillagers implements ModInitializer {
	public static final String MODID = "smartervillagers";
	private static ConfigHolder<ModConfig> CONFIG;

	@Override
	public void onInitialize() {
		SmarterVillagers.CONFIG = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
		Networking.init();
		SmarterVillagerMemoryModules.init();
	}

	public static Identifier id(String name) {
		return new Identifier(MODID, name);
	}

	public static ModConfig getCurrentConfig() {
		return CONFIG.get();
	}
}
