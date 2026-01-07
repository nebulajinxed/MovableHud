package com.github.nebulajinxed.movablehud.hudElements;

import com.github.nebulajinxed.movablehud.CustomHudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import com.github.nebulajinxed.movablehud.TextHudElement;
import com.github.nebulajinxed.movablehud.MovableHudClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.joml.Matrix3x2fStack;

public class ExampleCustomHud implements CustomHudElement {
    private int x = 30, y = 50;
    private String Id = "example2";

    private float scale = 1;

    @Override
    public void render(DrawContext ctx) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerInventory inventory = client.player.getInventory();

        float x = this.x; // your x
        float y = this.y; // your y
        float scale = this.scale; // your scale

        Matrix3x2fStack matrices = ctx.getMatrices();
        matrices.pushMatrix();

        // 1. translate first
        matrices.translate(x, y);

        // 2. then scale
        matrices.scale(scale, scale);

        // ItemRenderer
        ItemRenderer itemRenderer = client.getItemRenderer();

        // 3. render inventory items
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (stack.isEmpty()) continue;

            int slotX;
            int slotY;

            // PlayerInventory layout
            if (slot < 9) {
                // hotbar
                slotX = slot * 18;
                slotY = 58;
            } else {
                // main inventory (3 rows × 9)
                int row = (slot - 9) / 9;
                int col = (slot - 9) % 9;
                slotX = col * 18;
                slotY = row * 18;
            }

            // Because of scaling, divide coordinates
            int drawX = (int) (slotX / scale);
            int drawY = (int) (slotY / scale);

            ctx.drawItem(stack, drawX, drawY);
//            ctx.drawItemInSlot(client.textRenderer, stack, drawX, drawY);
        }

        matrices.popMatrix();
    }


    public int getX() { return x; }
    public int getY() { return y; }
    public float getHeight() {
        return 0;
    }
    public float getWidth() {
        return 0;
    }
    public float getScale() { return scale; }
    public String getId() { return this.Id; }

    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public void setScale(float scale) { this.scale = scale; }
}
