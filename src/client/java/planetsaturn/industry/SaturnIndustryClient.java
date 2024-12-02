package planetsaturn.industry;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import planetsaturn.industry.screen.CrucibleScreen;
import planetsaturn.industry.screen.ModScreenHandlers;

public class SaturnIndustryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		HandledScreens.register(ModScreenHandlers.CRUCIBLE_SCREEN_HANDLER, CrucibleScreen::new);
	}
}