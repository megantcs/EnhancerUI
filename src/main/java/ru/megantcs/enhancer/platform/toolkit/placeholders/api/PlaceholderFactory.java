package ru.megantcs.enhancer.platform.toolkit.placeholders;

public interface PlaceholderFactory
{
    static Placeholder create(String first, String second) {
        return new BasePlaceholderFactory().createPlaceholder(first, second);
    }

    Placeholder createPlaceholder(String first, String second);

    class BasePlaceholderFactory implements PlaceholderFactory {

        @Override
        public Placeholder createPlaceholder(String first, String second) {
            return new Placeholder.BasePlaceholder(first, second);
        }
    }
}
