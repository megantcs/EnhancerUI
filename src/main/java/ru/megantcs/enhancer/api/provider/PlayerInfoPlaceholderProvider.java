package ru.megantcs.enhancer.api.provider;

import net.minecraft.client.MinecraftClient;

import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBusRegister;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Returnable;
import ru.megantcs.enhancer.platform.toolkit.placeholders.api.Placeholder;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventHandler;

import java.util.Objects;

public class PlayerInfoPlaceholderProvider implements EventHandler, Returnable<Placeholder>
{
    private final Placeholder placeholder;

    public PlayerInfoPlaceholderProvider(EventBusRegister busRegister, Placeholder placeholder)
    {
        Objects.requireNonNull(busRegister);
        Objects.requireNonNull(placeholder);

        busRegister.register(this);
        this.placeholder = placeholder;
    }

    @Override
    public void onGameTick(MinecraftClient minecraftClient)
    {
        if(minecraftClient.player != null) {
            placeholder.addVariableOrNull("display_name", minecraftClient.player.getDisplayName().getString(), "(null)");
            placeholder.addVariableOrNull("name", minecraftClient.player.getName().getString(), "(null)");
            placeholder.addVariableOrNull("health",  String.format("%.1f", minecraftClient.player.getHealth()), "0.0");
            placeholder.addVariable("x", String.format("%.1f", minecraftClient.player.getX()));
            placeholder.addVariable("y", String.format("%.1f", minecraftClient.player.getY()));
            placeholder.addVariable("z", String.format("%.1f", minecraftClient.player.getZ()));
            placeholder.addVariable("saturation", String.format("%.1f", minecraftClient.player.getHungerManager().getSaturationLevel()));
            placeholder.addVariable("food", String.format("%.1f", (float)minecraftClient.player.getHungerManager().getFoodLevel()));
        }
    }

    @Override
    public Placeholder get() {
        return placeholder;
    }
}
