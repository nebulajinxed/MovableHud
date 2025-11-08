package net.nebula.movablehud;

import net.fabricmc.api.ClientModInitializer;
import net.nebula.movablehud.hudElements.Example2Hud;
import net.nebula.movablehud.hudElements.ExampleHud;
import net.nebula.movablehud.screen.MovableHudScreen;

public class MovableHudClient implements ClientModInitializer {
    public static String MODID = "movablehud";

    @Override
    public void onInitializeClient() {
//        HudRegistry.register(new ExampleHud(), "test1");
//        HudRegistry.register(new Example2Hud(), "test2");
        Commands.register();
        MovableHudScreen.loadHudData();
    }
}
