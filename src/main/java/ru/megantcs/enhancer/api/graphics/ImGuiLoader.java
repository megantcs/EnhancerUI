package ru.megantcs.enhancer.api.graphics;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.toolkit.events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.events.impl.ActionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImGuiLoader
{
    public static ImGuiLoader INSTANCE = new ImGuiLoader();

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final List<Interface> interfaces = new ArrayList<>();
    private ImFontBuilder fontBuilder;

    private ImGuiLoader() {}

    public ActionEvent<ImGuiLoader> LOADER_INIT_EVENT = EventFactory.makeActionEvent();
    public ActionEvent<ImGuiIO> RENDER_FRAME_EVENT = EventFactory.makeActionEvent();


    public boolean addInterface(Interface item) {
        Objects.requireNonNull(item);
        return interfaces.add(item);
    }

    public boolean removeInterface(Interface item) {
        Objects.requireNonNull(item);
        return interfaces.remove(item);
    }

    public void initWindow(long handle)
    {
        if(handle <= 0) throw new IllegalArgumentException("handle <= 0: " + handle);
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();
        fontBuilder = new ImFontBuilder(io.getFonts(), LoggerFactory.getLogger(ImFontBuilder.class));

        io.setIniFilename(null);
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);

        LOADER_INIT_EVENT.emit(this);

        imGuiGlfw.init(handle,true);
        imGuiGl3.init();
    }

    public ImFontBuilder fontBuilder() {
        return fontBuilder;
    }

    public void renderFrame()
    {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        ImGuiIO io = ImGui.getIO();
        RENDER_FRAME_EVENT.emit(io);

        for (var interfaceRender : interfaces)
        {
            interfaceRender.preRender(io);
            interfaceRender.render(io);
            interfaceRender.postRender(io);
        }

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    public ImGuiImplGlfw getImGuiGlfw() {
        return imGuiGlfw;
    }

    public ImGuiImplGl3 getImGuiGl3() {
        return imGuiGl3;
    }
}
