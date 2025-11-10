package com.github.nebulajinxed.movablehud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public interface ImageHudElement {
    void render(DrawContext context, int x, int y);
    float getX();
    float getY();
    float getScale();
    String getId();

    Identifier getTexture();
    float getWidth();
    float getHeight();

    void setScale(float scale);
    void setPosition(float x, float y);
}
