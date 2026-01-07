package com.github.nebulajinxed.movablehud;

import com.github.nebulajinxed.movablehud.hudElements.Example2Hud;
import com.github.nebulajinxed.movablehud.hudElements.ExampleCustomHud;
import com.github.nebulajinxed.movablehud.hudElements.ExampleHud;
import com.github.nebulajinxed.movablehud.hudElements.ExampleImageHud;
import com.github.nebulajinxed.movablehud.screen.MovableHudScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;

public class MovableHudClient implements ClientModInitializer {
    public static String MODID = "movablehud";

    @Override
    public void onInitializeClient() {
//        HudRegistry.registerText(new ExampleHud(), "test1");
//        HudRegistry.registerText(new Example2Hud(), "test2");
//        HudRegistry.registerImage(new ExampleImageHud(), "testimg");
        HudRegistry.registerCustom(new ExampleCustomHud(), Identifier.of("nebuba", "inv"));
        Commands.register();
        MovableHudScreen.loadHudData();
    }
}
