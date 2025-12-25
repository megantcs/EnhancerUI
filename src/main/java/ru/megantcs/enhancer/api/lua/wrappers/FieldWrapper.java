package ru.megantcs.enhancer.api.lua.wrappers;

import ru.megantcs.enhancer.api.lua.reflect.LuaExportClass;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportMethod;

import java.lang.reflect.*;

@LuaExportClass(name = "Field")
public class FieldWrapper {
    private final Field field;

    @LuaExportMethod
    public FieldWrapper() {
        this.field = null;
    }

    public FieldWrapper(Field field) {
        this.field = field;
    }

    @LuaExportMethod
    public String getName() {
        if (field == null) return "(null)";
        return field.getName();
    }

    @LuaExportMethod
    public String getType() {
        if (field == null) return "void";
        return field.getType().getSimpleName();
    }

    @LuaExportMethod
    public String getFullType() {
        if (field == null) return "void";
        return field.getType().getName();
    }

    @LuaExportMethod
    public boolean isPublic() {
        if (field == null) return false;
        return Modifier.isPublic(field.getModifiers());
    }

    @LuaExportMethod
    public boolean isPrivate() {
        if (field == null) return false;
        return Modifier.isPrivate(field.getModifiers());
    }

    @LuaExportMethod
    public boolean isProtected() {
        if (field == null) return false;
        return Modifier.isProtected(field.getModifiers());
    }

    @LuaExportMethod
    public boolean isStatic() {
        if (field == null) return false;
        return Modifier.isStatic(field.getModifiers());
    }

    @LuaExportMethod
    public boolean isFinal() {
        if (field == null) return false;
        return Modifier.isFinal(field.getModifiers());
    }

    @LuaExportMethod
    public boolean isTransient() {
        if (field == null) return false;
        return Modifier.isTransient(field.getModifiers());
    }

    @LuaExportMethod
    public boolean isVolatile() {
        if (field == null) return false;
        return Modifier.isVolatile(field.getModifiers());
    }

    @LuaExportMethod
    public String getModifiers() {
        if (field == null) return "";
        return Modifier.toString(field.getModifiers());
    }

    @LuaExportMethod
    public String getDeclaringClass() {
        if (field == null) return "";
        return field.getDeclaringClass().getName();
    }

    @LuaExportMethod
    public String toGenericString() {
        if (field == null) return "(null)";
        return field.toGenericString();
    }

    @LuaExportMethod
    public String toString() {
        if (field == null) return "FieldWrapper(null)";
        return "FieldWrapper[" + field.getName() + " : " + field.getType().getSimpleName() + "]";
    }

    public Field getJavaField() {
        return field;
    }
}