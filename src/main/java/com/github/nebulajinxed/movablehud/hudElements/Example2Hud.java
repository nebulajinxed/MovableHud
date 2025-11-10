package com.github.nebulajinxed.movablehud.hudElements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import com.github.nebulajinxed.movablehud.TextHudElement;
import com.github.nebulajinxed.movablehud.MovableHudClient;

public class Example2Hud implements TextHudElement {
    private int x = 30, y = 50;
    private String text = "Hello HUD test 2";
    private int color = 0xFFFFFFFF;
    private boolean hasShadow = false;
    private String Id = "example2";

    private float scale = 1;

    @Override
    public void render(DrawContext ctx, int x, int y, float delta) {
        ctx.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, hasShadow);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getText() { return text; }
    public int getColor() { return color; }
    public float getHeight() { return MinecraftClient.getInstance().textRenderer.fontHeight * scale; }
    public float getWidth() { return MinecraftClient.getInstance().textRenderer.getWidth(text) * scale; }
    public float getScale() { return scale; }
    public boolean hasShadow() { return hasShadow; }
    public String getId() { return this.Id; }

    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public void setColor(int color) { this.color = color; }
    public void setScale(float scale) { this.scale = scale; }
    public void setText(String text) { this.text = text; }
}
