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

    public static int getColor(CustomHudElement element) {
        if (element.isHidden())
            return 0x40FFFFFF; // still fixed when hidden

        int alpha = (int)((1.0f - element.getOpacity()) * 255); // 0.0–1.0 → 0–255
        alpha = Math.max(0, Math.min(255, alpha)); // clamp
        return (alpha << 24) | 0xFFFFFF;
    }
}
