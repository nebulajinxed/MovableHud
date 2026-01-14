package com.github.nebulajinxed.movablehud.screen;

import com.github.nebulajinxed.movablehud.CustomHudElement;
import com.github.nebulajinxed.movablehud.HudRegistry;
import com.google.gson.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;

public class MovableHudScreen extends Screen {

    private CustomHudElement draggingCustomElement = null;
    private CustomHudElement resizingCustomElement = null;

    private int dragOffsetX, dragOffsetY;
    private float resizeStartX, resizeStartY, originalWidth, originalHeight, originalScale;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MovableHudScreen() {
        super(Text.of("MovableHud"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for (CustomHudElement e : HudRegistry.CUSTOMELEMENTS) {
            e.render(context);
        }

    }

    @Override
    public void init() {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CustomHudElement element : HudRegistry.CUSTOMELEMENTS) {

            float ex = element.getX();
            float ey = element.getY();
            float ew = element.getWidth() * element.getScale();
            float eh = element.getHeight() * element.getScale();

            if (mouseX >= ex - (ew / 2) && mouseX <= ex + (ew / 2) && mouseY >= ey - (eh / 2) && mouseY <= ey + (eh / 2)) {
                if (button == 2) {
                    element.toggleHidden();
                }
                if (element.isHidden()) continue;

                if (button == 0) { // Left click -> drag
                    draggingCustomElement = element;
                    dragOffsetX = (int) (mouseX - ex);
                    dragOffsetY = (int) (mouseY - ey);
                } else if (button == 1) { // Right click -> resize
                    resizingCustomElement = element;
                    resizeStartX = (float) mouseX;
                    resizeStartY = (float) mouseY;
                    originalWidth = ew;
                    originalHeight = eh;
                    originalScale = element.getScale();
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltax, double deltay) {
        if (draggingCustomElement != null && button == 0) {
            draggingCustomElement.setPosition((float) (mouseX - dragOffsetX), (float) (mouseY - dragOffsetY));
            return true;
        }
        if (resizingCustomElement != null && button == 1) {
            double deltaX = mouseX - resizeStartX;
            double deltaY = mouseY - resizeStartY;

            double scaleChangeX = 1 + (deltaX / originalWidth);
            double scaleChangeY = 1 + (deltaY / originalHeight);
            double scaleChange = Math.max(0.1, Math.min(scaleChangeX, scaleChangeY));

            float newScale = (float) (originalScale * scaleChange);
            resizingCustomElement.setScale(newScale);

            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltax, deltay);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingCustomElement != null) {
            draggingCustomElement = null;
            return true;
        }
        if (button == 1 && resizingCustomElement != null) {
            resizingCustomElement = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
        for (CustomHudElement element : HudRegistry.CUSTOMELEMENTS) {

            float ex = element.getX();
            float ey = element.getY();
            float ew = element.getWidth() * element.getScale();
            float eh = element.getHeight() * element.getScale();

            boolean hovered =
                    mouseX >= ex - (ew / 2) && mouseX <= ex + (ew / 2) &&
                            mouseY >= ey - (eh / 2) && mouseY <= ey + (eh / 2);

            if (!hovered || element.isHidden()) continue;

            if (vertical > 0) {
                // Scroll up
                float newOpacity = element.getOpacity() - 0.05f;
                element.setOpacity(Math.max(0f, newOpacity));
            } else if (vertical < 0) {
                // Scroll down
                float newOpacity = element.getOpacity() + 0.05f;
                element.setOpacity(Math.min(0.8f, newOpacity));
            }

            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontal, vertical);
    }


    @Override
    public void close() {
        saveHudData();
        super.close();
    }

    @Override
    public void tick() {
    }

    private void saveHudData() {
        File configDir = new File(MinecraftClient.getInstance().runDirectory, "config");
        if (!configDir.exists()) configDir.mkdirs();

        File file = new File(configDir, "movableHud.json");

        JsonArray array = new JsonArray();
        for (CustomHudElement e : HudRegistry.CUSTOMELEMENTS) {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "custom");
            obj.addProperty("id", e.getId());
            obj.addProperty("x", e.getX());
            obj.addProperty("y", e.getY());
            obj.addProperty("scale", e.getScale());
            obj.addProperty("opacity", e.getOpacity());
            array.add(obj);
        }

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(array, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadHudData() {
        File file = new File(new File(MinecraftClient.getInstance().runDirectory, "config"), "movableHud.json");
        if (!file.exists()) return;

        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                String id = obj.get("id").getAsString();
                String type = obj.get("type").getAsString();
                int x = obj.get("x").getAsInt();
                int y = obj.get("y").getAsInt();
                float scale = obj.get("scale").getAsFloat();
                float opacity = obj.get("opacity").getAsFloat();

                if (type.equals("custom")) {
                    CustomHudElement hud = HudRegistry.getCustomElement(id);
                    if (hud != null) {
                        hud.setPosition(x, y);
                        hud.setScale(scale);
                        hud.setOpacity(opacity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
