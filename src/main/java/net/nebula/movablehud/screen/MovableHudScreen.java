package net.nebula.movablehud.screen;

import com.google.gson.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.nebula.movablehud.HudElement;
import net.nebula.movablehud.HudRegistry;
import net.nebula.movablehud.MovableHudClient;
import org.joml.Matrix3x2fStack;

import java.io.*;
import java.nio.file.Files;

public class MovableHudScreen extends Screen {

    private HudElement draggingElement = null;
    private HudElement resizingElement = null;
    private int dragOffsetX, dragOffsetY;
    private float resizeStartX, resizeStartY, originalWidth, originalHeight, originalScale;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MovableHudScreen() {
        super(Text.of("MovableHud"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for (HudElement element : HudRegistry.getByNamespace(MovableHudClient.MODID)) {
            Matrix3x2fStack m = context.getMatrices();
            m.pushMatrix();
            m.translate(element.getX() - (element.getWidth() / 2), element.getY() - (element.getHeight() / 2));
            m.scale(element.getScale());
            element.render(context, 0, 0, delta);
            m.popMatrix();
        }
    }

    @Override
    public void init() {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (HudElement element : HudRegistry.getByNamespace(MovableHudClient.MODID)) {
            int ex = element.getX();
            int ey = element.getY();
            float ew = element.getWidth();
            float eh = element.getHeight();

            if (mouseX >= ex - (ew / 2) && mouseX <= ex + (ew / 2) && mouseY >= ey - (eh / 2) && mouseY <= ey + (eh / 2)) {
                if (button == 0) { // Left click -> drag
                    draggingElement = element;
                    dragOffsetX = (int) (mouseX - ex);
                    dragOffsetY = (int) (mouseY - ey);
                } else if (button == 1) { // Right click -> resize
                    resizingElement = element;
                    resizeStartX = (float) mouseX;
                    resizeStartY = (float) mouseY;
                    originalWidth = ew;
                    originalHeight = eh;
                    originalScale = element.getScale();
                }
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltax, double deltay) {
        if (draggingElement != null && button == 0) {
            draggingElement.setPosition((int) mouseX - dragOffsetX, (int) mouseY - dragOffsetY);
            return true;
        }

        if (resizingElement != null && button == 1) {
            double deltaX = mouseX - resizeStartX;
            double deltaY = mouseY - resizeStartY;

            double scaleChangeX = 1 + (deltaX / originalWidth);
            double scaleChangeY = 1 + (deltaY / originalHeight);
            double scaleChange = Math.max(0.1, Math.min(scaleChangeX, scaleChangeY));

            float newScale = (float) (originalScale * scaleChange);
            resizingElement.setScale(newScale);

            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltax, deltay);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingElement != null) {
            draggingElement = null;
            return true;
        }
        if (button == 1 && resizingElement != null) {
            resizingElement = null;
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
        for (HudElement e : HudRegistry.getByNamespace(MovableHudClient.MODID)) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", e.getId()); // requires getId()
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
                int x = obj.get("x").getAsInt();
                int y = obj.get("y").getAsInt();
                float scale = obj.get("scale").getAsFloat();

                HudElement hud = HudRegistry.get(id);
                if (hud != null) {
                    hud.setPosition(x, y);
                    hud.setScale(scale);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
