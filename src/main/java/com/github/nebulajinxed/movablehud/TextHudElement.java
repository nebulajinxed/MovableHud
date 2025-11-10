package com.github.nebulajinxed.movablehud;

import net.minecraft.client.gui.DrawContext;

public interface TextHudElement {
    void render(DrawContext context, int x, int y, float delta);
    int getX();
    int getY();
    String getText();
    int getColor();
    float getWidth();
    float getHeight();
    float getScale();
    boolean hasShadow();
    String getId();
    void setPosition(int x, int y);
    void setColor(int color);
    void setScale(float scale);
    void setText(String text);
}
