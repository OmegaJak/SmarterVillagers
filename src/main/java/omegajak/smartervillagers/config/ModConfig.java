package omegajak.smartervillagers.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import omegajak.smartervillagers.SmarterVillagers;

@Config(name = SmarterVillagers.MODID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public DuplicateTrades duplicateTradesConfig = new DuplicateTrades();

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.Gui.RequiresRestart(value =  false)
    public boolean avoidGettingStuckOnLadders = true;

    public static class DuplicateTrades {
        @ConfigEntry.Gui.Tooltip(count = 2)
        @ConfigEntry.Gui.RequiresRestart(value =  false)
        public boolean enabled = true;

        @ConfigEntry.Gui.Tooltip(count = 3)
        @ConfigEntry.Gui.RequiresRestart(value =  false)
        public int searchRange = 150;

        @ConfigEntry.Gui.Tooltip(count = 3)
        @ConfigEntry.Gui.RequiresRestart(value =  false)
        public int newTradeAttempts = 5;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        positiveIntegerValidation(duplicateTradesConfig.searchRange);
        positiveIntegerValidation(duplicateTradesConfig.newTradeAttempts);
    }

    private void positiveIntegerValidation(int toValidate) throws ValidationException {
        if (toValidate <= 0) throw new ValidationException("Config value must be a positive integer. Reverting to default.");
    }
}
