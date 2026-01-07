package com.github.nebulajinxed.movablehud;

import net.minecraft.client.gui.DrawContext;

public interface CustomHudElement {
    void render(DrawContext context);
    int getX();
    int getY();
    float getWidth();
    float getHeight();
    float getScale();
    String getId();
    void setPosition(int x, int y);
    void setScale(float scale);
}
