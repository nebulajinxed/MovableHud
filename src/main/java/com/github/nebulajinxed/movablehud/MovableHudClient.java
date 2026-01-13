package com.github.nebulajinxed.movablehud;

import com.github.nebulajinxed.movablehud.hudElements.ExampleCustomHud;
import com.github.nebulajinxed.movablehud.screen.MovableHudScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;

public class MovableHudClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
//        HudRegistry.registerCustom(new ExampleCustomHud(), Identifier.of("nebuba", "inv"));
        Commands.register();
        MovableHudScreen.loadHudData();
    }
}
