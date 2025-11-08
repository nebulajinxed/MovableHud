package com.github.nebulajinxed.movablehud;

import net.fabricmc.api.ClientModInitializer;
import com.github.nebulajinxed.movablehud.screen.MovableHudScreen;

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
