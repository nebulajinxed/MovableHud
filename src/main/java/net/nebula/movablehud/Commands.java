package net.nebula.movablehud;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.nebula.movablehud.screen.MovableHudScreen;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Commands {

    public static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess) -> {
            commandDispatcher.register(ClientCommandManager.literal("movableHud")
                    .executes(ctx -> {
                        OpenScreen(new MovableHudScreen());
                        return Command.SINGLE_SUCCESS;
                    })
            );
        }));
    }

    public static void OpenScreen(Screen screen) {
        SCHEDULER.schedule(() ->
                        MinecraftClient.getInstance().execute(() ->
                                MinecraftClient.getInstance().setScreen(screen)
                        ),
                50, TimeUnit.MILLISECONDS
        );
    }
}
