package com.github.nebulajinxed.movablehud.screen;

import com.github.nebulajinxed.movablehud.ImageHudElement;
import com.google.gson.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import com.github.nebulajinxed.movablehud.TextHudElement;
import com.github.nebulajinxed.movablehud.HudRegistry;
import org.joml.Matrix3x2fStack;

import java.io.*;
import java.nio.file.Files;

public class MovableHudScreen extends Screen {

    private TextHudElement draggingTextElement = null;
    private TextHudElement resizingTextElement = null;

    private ImageHudElement draggingImageElement = null;
    private ImageHudElement resizingImageElement = null;


    private int dragOffsetX, dragOffsetY;
    private float resizeStartX, resizeStartY, originalWidth, originalHeight, originalScale;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MovableHudScreen() {
        super(Text.of("MovableHud"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for (TextHudElement element : HudRegistry.TEXTELEMENTS) {
            Matrix3x2fStack m = context.getMatrices();
            m.pushMatrix();
            m.translate(element.getX() - (element.getWidth() / 2), element.getY() - (element.getHeight() / 2));
            m.scale(element.getScale());
            element.render(context, 0, 0, delta);
            m.popMatrix();
        }

        for (ImageHudElement e : HudRegistry.IMAGEELEMENTS) {
            Matrix3x2fStack m = context.getMatrices();
            m.pushMatrix();
            m.translate(e.getX() - ((e.getWidth() / 2) * e.getScale()), e.getY() - ((e.getHeight() / 2) * e.getScale()));
            e.render(context, 0, 0);
            m.popMatrix();
        }
    }

    @Override
    public void init() {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (TextHudElement element : HudRegistry.TEXTELEMENTS) {
            int ex = element.getX();
            int ey = element.getY();
            float ew = element.getWidth();
            float eh = element.getHeight();

            if (mouseX >= ex - (ew / 2) && mouseX <= ex + (ew / 2) && mouseY >= ey - (eh / 2) && mouseY <= ey + (eh / 2)) {
                if (button == 0) { // Left click -> drag
                    draggingTextElement = element;
                    dragOffsetX = (int) (mouseX - ex);
                    dragOffsetY = (int) (mouseY - ey);
                } else if (button == 1) { // Right click -> resize
                    resizingTextElement = element;
                    resizeStartX = (float) mouseX;
                    resizeStartY = (float) mouseY;
                    originalWidth = ew;
                    originalHeight = eh;
                    originalScale = element.getScale();
                }
                return true;
            }
        }
        for (ImageHudElement element : HudRegistry.IMAGEELEMENTS) {
            float ex = element.getX();
            float ey = element.getY();
            float ew = element.getWidth() * element.getScale();
            float eh = element.getHeight() * element.getScale();

            if (mouseX >= ex - (ew / 2) && mouseX <= ex + (ew / 2) && mouseY >= ey - (eh / 2) && mouseY <= ey + (eh / 2)) {
                if (button == 0) { // Left click -> drag
                    draggingImageElement = element;
                    dragOffsetX = (int) (mouseX - ex);
                    dragOffsetY = (int) (mouseY - ey);
                } else if (button == 1) { // Right click -> resize
                    resizingImageElement = element;
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
        if (draggingTextElement != null && button == 0) {
            draggingTextElement.setPosition((int) mouseX - dragOffsetX, (int) mouseY - dragOffsetY);
            return true;
        }
        if (draggingImageElement != null && button == 0) {
            draggingImageElement.setPosition( (int) mouseX - dragOffsetX, (int) mouseY - dragOffsetY);
            return true;
        }

        if (resizingTextElement != null && button == 1) {
            double deltaX = mouseX - resizeStartX;
            double deltaY = mouseY - resizeStartY;

            double scaleChangeX = 1 + (deltaX / originalWidth);
            double scaleChangeY = 1 + (deltaY / originalHeight);
            double scaleChange = Math.max(0.1, Math.min(scaleChangeX, scaleChangeY));

            float newScale = (float) (originalScale * scaleChange);
            resizingTextElement.setScale(newScale);

            return true;
        }
        if (resizingImageElement != null && button == 1) {
            double deltaX = mouseX - resizeStartX;
            double deltaY = mouseY - resizeStartY;

            double scaleChangeX = 1 + (deltaX / originalWidth);
            double scaleChangeY = 1 + (deltaY / originalHeight);
            double scaleChange = Math.max(0.1, Math.min(scaleChangeX, scaleChangeY));

            float newScale = (float) (originalScale * scaleChange);
            resizingImageElement.setScale(newScale);

            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltax, deltay);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingTextElement != null) {
            draggingTextElement = null;
            return true;
        }
        if (button == 1 && resizingTextElement != null) {
            resizingTextElement = null;
            return true;
        }
        if (button == 0 && draggingImageElement != null) {
            draggingImageElement = null;
            return true;
        }
        if (button == 1 && resizingImageElement != null) {
            resizingImageElement = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
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
        for (TextHudElement e : HudRegistry.TEXTELEMENTS) {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "text");
            obj.addProperty("id", e.getId()); // requires getId()
            obj.addProperty("x", e.getX());
            obj.addProperty("y", e.getY());
            obj.addProperty("scale", e.getScale());
            array.add(obj);
        }
        for (ImageHudElement e : HudRegistry.IMAGEELEMENTS) {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "img");
            obj.addProperty("id", e.getId());
            obj.addProperty("x", e.getX());
            obj.addProperty("y", e.getY());
            obj.addProperty("scale", e.getScale());
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

                if (type.equals("text")) {
                    TextHudElement hud = HudRegistry.getTextElement(id);
                    if (hud != null) {
                        hud.setPosition(x, y);
                        hud.setScale(scale);
                    }
                } else if (type.equals("img")) {
                    ImageHudElement hud = HudRegistry.getImageElement(id);
                    if (hud != null) {
                        hud.setPosition(x, y);
                        hud.setScale(scale);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
