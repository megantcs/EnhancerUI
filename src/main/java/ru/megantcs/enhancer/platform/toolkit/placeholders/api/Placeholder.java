package ru.megantcs.enhancer.platform.toolkit.placeholders.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.megantcs.enhancer.platform.toolkit.api.API;
import ru.megantcs.enhancer.platform.toolkit.api.AccessExceptions;
import ru.megantcs.enhancer.platform.toolkit.api.Noexcept;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Interface for creating classes that implement variable substitution in strings.
 * <p>
 * Example usage:
 * <pre>{@code
 * Placeholder placeholder = PlaceholderFactory.create("{", "}");
 * placeholder.addVariable("fps", "215");
 * String text = placeholder.parse("my fps equal: {fps}");
 * // Result: "my fps equal: 215"
 * }</pre>
 *
 * @see PlaceholderFactory Factory for creating Placeholder instances
 * @author [megantcs]
 * @since [1.0]
 */
@API(status = API.Status.MAINTAINED, since = "1.0")
public interface Placeholder
{
    @AccessExceptions(access = NullPointerException.class)
    void addVariable(@NotNull String name, @NotNull String value);

    @AccessExceptions(access = NullPointerException.class)
    void addVariableOrNull(@NotNull String name, @Nullable String value, @NotNull String orNull);

    @AccessExceptions(access = NullPointerException.class)
    boolean removeVariable(@NotNull String name);

    @AccessExceptions(access = NullPointerException.class)
    boolean containsVariable(@NotNull String name);

    @AccessExceptions(access = NullPointerException.class)
    @NotNull String parse(@NotNull String src);

    @Noexcept
    void clear();

    @AccessExceptions(access = NullPointerException.class)
    default void addAll(@NotNull Map<String, String> data)
    {
        Objects.requireNonNull(data);
        data.forEach((key, value)->{
            Objects.requireNonNull(key);
            Objects.requireNonNull(value);
            addVariable(key, value);
        });
    }

    @AccessExceptions(access = NullPointerException.class)
    default void addAll(@NotNull MapBuilder<String, String> data) {
        Objects.requireNonNull(data);
        addAll(data.get());
    }

    @AccessExceptions(access = NullPointerException.class)
    @Nullable String getVariable(@NotNull String key);

    @Noexcept
    @NotNull Map<String, String> getVariables();

    class BasePlaceholder implements Placeholder
    {
        private final Map<String, String> data = new HashMap<>();
        private final ParseTokens tokens;

        public BasePlaceholder(String first, String second) {
            Objects.requireNonNull(first, second);
            tokens = new ParseTokens(first, second);
        }

        @Override
        public void addVariable(@NotNull String name, @NotNull String value)
        {
            Objects.requireNonNull(name);
            Objects.requireNonNull(value);

            data.put(name, value);
        }

        @Override
        public void addVariableOrNull(@NotNull String name, @Nullable String value, @NotNull String orNull) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(orNull);

            addVariable(name, value == null? orNull : value);
        }

        @Override
        public boolean removeVariable(@NotNull String name) {
            Objects.requireNonNull(name);
            return data.remove(name) != null;
        }

        @Override
        public boolean containsVariable(@NotNull String name) {
            Objects.requireNonNull(name);
            return data.containsKey(name);
        }

        @Override
        public @NotNull String parse(@NotNull String src) {
            String result = Objects.requireNonNull(src);
            for (Map.Entry<String, String> entry : data.entrySet()) {
                result = result.replace(tokens.first + entry.getKey() + tokens.second, entry.getValue());
            }
            return result;
        }

        @Override
        public void clear() {
            data.clear();
        }

        @Override
        public @Nullable String getVariable(@NotNull String key) {
            Objects.requireNonNull(key);
            if(!containsVariable(key)) return null;

            return data.get(key);
        }

        @Override
        public @NotNull Map<String, String> getVariables() {
            return Map.copyOf(data);
        }

        private record ParseTokens(String first, String second) {}
    }
}
