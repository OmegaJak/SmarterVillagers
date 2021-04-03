package omegajak.smartervillagers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import omegajak.smartervillagers.config.ModConfig;

public class SmarterVillagers implements ModInitializer {
	public static final String MODID = "smartervillagers";
	private static ConfigHolder<ModConfig> CONFIG;

	@Override
	public void onInitialize() {
		SmarterVillagers.CONFIG = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
	}

	public static ModConfig getCurrentConfig() {
		return CONFIG.get();
	}
}
