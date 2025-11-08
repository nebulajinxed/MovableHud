package net.nebula.movablehud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.nebula.movablehud.screen.MovableHudScreen;
import org.joml.Matrix3x2fStack;

import java.util.*;

public class HudRegistry {
    private static final List<HudElement> ELEMENTS = new ArrayList<>();
    static MinecraftClient client = MinecraftClient.getInstance();

    public static void register(HudElement element, String name) {
        ELEMENTS.add(element);
        HudElementRegistry.addLast(Identifier.of(MovableHudClient.MODID, name), (drawContext, renderTickCounter) -> {
            if (Objects.equals(client.currentScreen, new MovableHudScreen())) return;

            Matrix3x2fStack m = drawContext.getMatrices();
            m.pushMatrix();
            m.translate(element.getX() - (element.getWidth() / 2), element.getY() - (element.getHeight() / 2));
            m.scale(element.getScale(), element.getScale());
            drawContext.drawText(MinecraftClient.getInstance().textRenderer, element.getText(), 0, 0, element.getColor(), element.hasShadow());
            m.popMatrix();
        });
    }

    public static List<HudElement> getByNamespace(String ns) {
        return ELEMENTS.stream()
                .filter(e -> e.getNamespace().equals(ns))
                .toList();
    }

    public static HudElement get(String id) {
        for (HudElement element : HudRegistry.getByNamespace(MovableHudClient.MODID)) {
            if (element.getId().equals(id)) return element;
        }
        return null;
    }
}
