package ru.megantcs.enhancer.api.provider;

import ru.megantcs.enhancer.platform.toolkit.interfaces.Returnable;
import ru.megantcs.enhancer.platform.toolkit.placeholders.api.Placeholder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public record SystemPlaceholderProvider(Placeholder placeholder) implements Returnable<Placeholder> {
    public SystemPlaceholderProvider(Placeholder placeholder) {
        this.placeholder = Objects.requireNonNull(placeholder);
        init();
    }

    private void init() {
        placeholder.addVariable("user.name", System.getProperty("user.name"));
        placeholder.addVariable("os.name", System.getProperty("os.name"));
        placeholder.addVariable("os.arch", System.getProperty("os.arch"));
        placeholder.addVariable("os.version", System.getProperty("os.version"));
        placeholder.addVariable("user.home", System.getProperty("user.home"));
        placeholder.addVariable("user.dir", System.getProperty("user.dir"));
        placeholder.addVariable("timestamp", String.valueOf(System.currentTimeMillis()));
        placeholder.addVariable("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        placeholder.addVariable("time", new SimpleDateFormat("HH:mm:ss").format(new Date()));
        placeholder.addVariable("timezone", TimeZone.getDefault().getID());
        placeholder.addVariable("locale", Locale.getDefault().toString());
        placeholder.addVariable("java.version", System.getProperty("java.version"));
        placeholder.addVariable("java.vendor", System.getProperty("java.vendor"));
        placeholder.addVariable("java.home", System.getProperty("java.home"));
        placeholder.addVariable("available.processors", String.valueOf(Runtime.getRuntime().availableProcessors()));
        placeholder.addVariable("total.memory", String.valueOf(Runtime.getRuntime().totalMemory()));
        placeholder.addVariable("free.memory", String.valueOf(Runtime.getRuntime().freeMemory()));
        placeholder.addVariable("temp.dir", System.getProperty("java.io.tmpdir"));
    }

    @Override
    public Placeholder get() {
        return placeholder;
    }
}
