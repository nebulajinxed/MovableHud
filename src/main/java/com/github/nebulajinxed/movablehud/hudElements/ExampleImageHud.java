package com.github.nebulajinxed.movablehud.hudElements;

import com.github.nebulajinxed.movablehud.ImageHudElement;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;

public class ExampleImageHud implements ImageHudElement {
    private float x = 30, y = 50;
    private String Id = "exampleimage";
    private Identifier texture = Identifier.of("minecraft", "textures/block/target_top.png");
    private int width = 5;
    private int height = 5;

    private float scale = 1;


    @Override
    public void render(DrawContext context, int x, int y) {
        Matrix3x2fStack m = context.getMatrices();
        m.pushMatrix();
        m.scale(getScale(), getScale());
        context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 0, 0, width, height, width, height);
        m.popMatrix();
    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getScale() { return this.scale; }
    public String getId() { return this.Id; }
    public Identifier getTexture() { return this.texture; }
    public float getWidth() { return (this.width); }
    public float getHeight() { return (this.height); }

    public void setScale(float scale) { this.scale = scale; }
    public void setPosition(float x, float y) { this.x = x; this.y = y;}
}
