package planetsaturn.industry.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import planetsaturn.industry.SaturnIndustry;

public class ModSounds {

    public static final SoundEvent WRENCH_TURN = registerSound("wrench_turn");

    private static SoundEvent registerSound(String name) {;
        return Registry.register(Registry.SOUND_EVENT, new Identifier(SaturnIndustry.MOD_ID, name), new SoundEvent(new Identifier(SaturnIndustry.MOD_ID, name)));
    }

    public static void registerModSounds() {
        SaturnIndustry.LOGGER.debug("Registering Mod Sounds for " + SaturnIndustry.MOD_ID);
    }
}
