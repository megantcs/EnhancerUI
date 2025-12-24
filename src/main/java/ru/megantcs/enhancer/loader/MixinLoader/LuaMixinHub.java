package ru.megantcs.enhancer.loader.MixinLoader;

import ru.megantcs.enhancer.api.lua.LuaSandBox;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class LuaMixinHub
{
    private final List<LuaMixinModule> mixinModuleList;
    private final LuaSandBox luaSandBox;

    public LuaMixinHub(LuaSandBox luaSandBox) {
        mixinModuleList = new CopyOnWriteArrayList<>();
        this.luaSandBox = Objects.requireNonNull(luaSandBox);
        this.luaSandBox.OnChunkLoaded.register(this::onChunkUpdate);
    }

    private void onChunkUpdate() {
        for (LuaMixinModule luaMixinModule : mixinModuleList) {
            luaMixinModule.onChunkUpdate();
        }
    }

    public void addModule(LuaMixinModule module) {
        Objects.requireNonNull(module, "module cannot be null");
        module.init(luaSandBox.sandBox());
        module.onChunkUpdate();

        mixinModuleList.add(module);
    }

    public void shutdownAll() {
        mixinModuleList.forEach(LuaMixinModule::shutdown);
    }

    public void initAll() {
        for (LuaMixinModule luaMixinModule : mixinModuleList) {
            luaMixinModule.init(luaSandBox.sandBox());
        }
    }

    public void reload() {
        shutdownAll();
        initAll();

        luaSandBox.reload();
    }
}
