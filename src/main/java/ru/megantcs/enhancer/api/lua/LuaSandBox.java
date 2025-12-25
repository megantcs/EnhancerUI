package ru.megantcs.enhancer.api.lua;


import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.api.lua.chunks.Chunk;
import ru.megantcs.enhancer.api.lua.chunks.FileChunk;
import ru.megantcs.enhancer.api.lua.chunks.CodeChunk;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportClass;
import ru.megantcs.enhancer.api.lua.reflect.LuaTableCreator;
import ru.megantcs.enhancer.platform.toolkit.Warnings;
import ru.megantcs.enhancer.platform.toolkit.events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.events.impl.RunnableEvent;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionContainer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class LuaSandBox
{
    private final List<Chunk> chunks;
    private final List<ModuleLoadData> modules;
    private final LuaScriptEngine environment;

    final ExceptionContainer exceptionContainer;

    public final RunnableEvent OnChunkLoaded = EventFactory.makeRunnableEvent();

    public LuaSandBox(ExceptionContainer exceptionContainer) {
        chunks = new CopyOnWriteArrayList<>();
        modules = new CopyOnWriteArrayList<>();

        this.exceptionContainer = Objects.requireNonNull(exceptionContainer);
        environment = new LuaScriptEngine(this.exceptionContainer);
    }

    public void reloadEnvironment() {
        environment.reloadEnvironment();
    }

    public void reloadChunksValue() {
        chunks.forEach(Chunk::updateValue);
    }

    public void reExecuteChunks() {
        chunks.forEach(this::executeChunk);
    }

    public void reloadModules() {
        modules.forEach((e)->
                loadClass(e.instance, false));
    }

    public ExceptionContainer getExceptionContainer() {
        return exceptionContainer;
    }

    public void reload() {
        reloadEnvironment();
        reloadModules();
        reloadChunksValue();
        reExecuteChunks();
    }

    @CheckReturnValue
    private boolean containsChunk(@NotNull Chunk chunk) {
        Objects.requireNonNull(chunk);

        for (Chunk chunk1 : chunks) {
            if(chunk1.equals(chunk)) {
                return true;
            }
        }
        return false;
    }

    private boolean executeChunk(@NotNull Chunk chunk) {
        Objects.requireNonNull(chunk);

        var loaded = chunk.executeChunk(environment);
        OnChunkLoaded.invoker().run();

        return loaded;
    }

    @SuppressWarnings(Warnings.unusedReturnValue)
    private boolean addChunk(@NotNull Chunk chunk) {
        Objects.requireNonNull(chunk);
        if(containsChunk(chunk)) return false;
        return chunks.add(chunk);
    }

    private boolean loadChunk(Chunk chunk) {
        Objects.requireNonNull(chunk);

        // if the chunk already loaded. then exit
        if(containsChunk(chunk)) return false;

        chunk.updateValue();
        if(!executeChunk(chunk)) {
            exceptionContainer.logLast();

            // only fileChunk can be updated.
            // we only save it in case of an error.
            // because it can become fixed when reloaded
            if(chunk instanceof FileChunk)
            {
                addChunk(chunk);
            }
            return false;
        }

        addChunk(chunk);
        return true;
    }

    private boolean moduleIsLoaded(Object instance)
    {
        Objects.requireNonNull(instance);

        LuaExportClass annotation = instance.getClass().getAnnotation(LuaExportClass.class);
        if(annotation == null) return false;

        for (ModuleLoadData module : modules) {
            if(Objects.equals(module.name, annotation.name())) {
                return true;
            }
            if(module.instance.equals(instance)) {
                return true;
            }
        }
        return false;
    }

    private boolean addModuleData(ModuleLoadData data) {
        Objects.requireNonNull(data);
        if(moduleIsLoaded(data)) return false;

        LoggerFactory.getLogger("addModuleData").warn(data.instance.toString());
        return modules.add(data);
    }

    private Object extractInstance(Object obj) {
        Objects.requireNonNull(obj);
        if(obj instanceof ModuleLoadData data) obj = data.instance;

        return obj;
    }



    public boolean loadClass(Object instance)
    {
        return loadClass(instance, true); // пока колхозно
    }

    public boolean loadClass(Object instance, boolean addClass)
    {
        Objects.requireNonNull(instance);
        instance = extractInstance(instance);

        if(!LuaTableCreator.exportClass(instance,
                environment, exceptionContainer)) {
            exceptionContainer.logAll();
            return false;
        }

        if(!addClass) return false;

        return addModuleData(new ModuleLoadData(instance,
                instance.getClass().getAnnotation(LuaExportClass.class).name())); // safe
    }

    @SuppressWarnings(Warnings.unusedReturnValue)
    public boolean loadClass(Class<?> instance) throws
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
       return loadClass(instance.getDeclaredConstructor().newInstance());
    }

    public boolean loadScript(String code, String name) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(name);

        var newChunk = new CodeChunk(code, unique("script#chunk",name));
        return loadChunk(newChunk);
    }

    @SuppressWarnings(Warnings.unusedReturnValue)
    public boolean loadFile(String fileName) throws IOException {
        Objects.requireNonNull(fileName);
        var path = Path.of(fileName);

        if(!Files.exists(path)) {
            return false;
        }

        var newChunk = new FileChunk("(null)", unique("file#chunk", path.getFileName().toString()), fileName);
        return loadChunk(newChunk);
    }

    private String unique(String namespace, String start) {
        Objects.requireNonNull(namespace);
        Objects.requireNonNull(start);

        return namespace + "@" + start;
    }

    public LuaScriptEngine scriptEngine() {
        return environment;
    }

    private record ModuleLoadData(Object instance, String name) { }
}
