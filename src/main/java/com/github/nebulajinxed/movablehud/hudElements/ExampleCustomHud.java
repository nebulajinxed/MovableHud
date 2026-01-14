package com.github.nebulajinxed.movablehud.hudElements;

import com.github.nebulajinxed.movablehud.CustomHudElement;
import com.github.nebulajinxed.movablehud.MovableHudClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;

public class ExampleCustomHud implements CustomHudElement {
    private float opacity = 0.3F;

    private float x = 30, y = 50;
    private String Id = "example2";
    private float height = 16;
    private float width = 16;
    private boolean hidden;

    private static final Identifier TARGET_TEXTURE =
            Identifier.of("minecraft", "textures/block/target_top.png");

    private float scale = 1;

    // All rendering logic
    @Override
    public void render(DrawContext DrawCtx) {
        Matrix3x2fStack m = DrawCtx.getMatrices();
        m.pushMatrix();
        m.translate(x, y);
        m.translate(-(width * scale / 2) , -(height * scale / 2)); // Translating so the x and y are in the center of the object, THIS IS REQUIRED BY DRAGGING LOGIC
        m.scale(scale);
        DrawCtx.drawTexture(RenderPipelines.GUI_TEXTURED, TARGET_TEXTURE, 0, 0, 0, 0, 16, 16, 16,16, 16, 16, MovableHudClient.getColor(this)); // Rendering a texture with the color set before
        m.popMatrix();
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getHeight() {return height; }
    public float getWidth() {return width; }
    public float getScale() { return scale; }
    public String getId() { return this.Id; }

    public void setPosition(float x, float y) { this.x = x; this.y = y; }
    public void setScale(float scale) { this.scale = scale; }
    public void toggleHidden() {this.hidden = !this.hidden; }
    public boolean isHidden() {return this.hidden; }

    public float getOpacity() { return this.opacity; }
    public void setOpacity(float opacity) { this.opacity = opacity; }
}
