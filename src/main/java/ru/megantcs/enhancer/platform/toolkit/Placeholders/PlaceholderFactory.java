package ru.megantcs.enhancer.platform.toolkit.Placeholders;

public interface PlaceholderFactory
{
    static Placeholder create(String first, String second) {
        return new SimplePlaceholderFactory().createPlaceholder(first, second);
    }

    Placeholder createPlaceholder(String first, String second);

    class SimplePlaceholderFactory implements PlaceholderFactory {

        @Override
        public Placeholder createPlaceholder(String first, String second) {
            return new Placeholder.SimplePlaceholder(first, second);
        }
    }
}
