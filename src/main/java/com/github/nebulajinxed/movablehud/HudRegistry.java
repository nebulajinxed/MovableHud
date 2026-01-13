package com.github.nebulajinxed.movablehud;

import com.github.nebulajinxed.movablehud.screen.MovableHudScreen;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HudRegistry {
    public static final List<CustomHudElement> CUSTOMELEMENTS = new ArrayList<>();
    static MinecraftClient client = MinecraftClient.getInstance();

    public static void registerCustom(CustomHudElement element, Identifier name) {
        CUSTOMELEMENTS.add(element);
        HudElementRegistry.addLast(name, (context, tickCounter) -> {
            if (Objects.equals(client.currentScreen, new MovableHudScreen())) return;
            if (element.isHidden()) return;

            element.render(context);
        });
    }

    public static CustomHudElement getCustomElement(String id) {
        for (CustomHudElement element : HudRegistry.CUSTOMELEMENTS) {
            if (element.getId().equals(id)) return element;
        }
        return null;
    }
}
