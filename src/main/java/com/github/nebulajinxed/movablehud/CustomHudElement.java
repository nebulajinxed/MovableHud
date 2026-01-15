package com.github.nebulajinxed.movablehud;

import net.minecraft.client.gui.DrawContext;

public interface CustomHudElement {
    void render(DrawContext context);
    float getX();
    float getY();
    float getWidth();
    float getHeight();
    float getScale();
    String getId();
    void setPosition(float x, float y);
    void setScale(float scale);

    void toggleHidden();
    boolean isHidden();
    void setHidden(boolean hidden);

    float getOpacity();
    void setOpacity(float opacity);
}
