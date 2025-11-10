package com.github.nebulajinxed.movablehud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;
import com.github.nebulajinxed.movablehud.screen.MovableHudScreen;
import org.joml.Matrix3x2fStack;

import java.util.*;

public class HudRegistry {
    public static final List<TextHudElement> TEXTELEMENTS = new ArrayList<>();
    public static final List<ImageHudElement> IMAGEELEMENTS = new ArrayList<>();
    static MinecraftClient client = MinecraftClient.getInstance();

    public static void registerText(TextHudElement element, String name) {
        TEXTELEMENTS.add(element);
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

    public static void registerImage(ImageHudElement element, String name) {
        IMAGEELEMENTS.add(element);
        HudElementRegistry.addLast(Identifier.of(MovableHudClient.MODID, name), (context, renderTickCounter) -> {
            if (Objects.equals(client.currentScreen, new MovableHudScreen())) return;

            Matrix3x2fStack m = context.getMatrices();
            m.pushMatrix();
            m.translate(element.getX() - ((element.getWidth() * element.getScale()) / 2), element.getY() - ((element.getHeight() * element.getScale()) / 2));
            m.scale(element.getScale(), element.getScale());
            context.drawTexture(RenderPipelines.GUI_TEXTURED, element.getTexture(), 0, 0, (float) 0, (float) 0, (int) element.getWidth(), (int) element.getWidth(), (int) element.getWidth(), (int) element.getWidth());
            m.popMatrix();
        });
    }

    public static TextHudElement getTextElement(String id) {
        for (TextHudElement element : HudRegistry.TEXTELEMENTS) {
            if (element.getId().equals(id)) return element;
        }
        return null;
    }

    public static ImageHudElement getImageElement(String id) {
        for (ImageHudElement element : HudRegistry.IMAGEELEMENTS) {
            if (element.getId().equals(id)) return element;
        }
        return null;
    }
}
