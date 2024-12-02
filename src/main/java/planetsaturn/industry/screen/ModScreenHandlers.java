package planetsaturn.industry.screen;

import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {
    public static ScreenHandlerType<CrucibleScreenHandler> CRUCIBLE_SCREEN_HANDLER;

    public static void registerModScreenHandlers() {
        CRUCIBLE_SCREEN_HANDLER = new ScreenHandlerType<>(CrucibleScreenHandler::new);
    }
}
