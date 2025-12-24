package ru.megantcs.enhancer.platform.toolkit.colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BrushHelper
{
    public static boolean isGradient(Brush brush) {
        return brush instanceof GradientBrush;
    }

    public static java.util.List<Color> fillList(Brush brush, int count) {
        List<Color> result = new ArrayList<>();

        if (brush == null || count <= 0) {
            return result;
        }

        if (brush instanceof GradientBrush gradientBrush) {
            if (gradientBrush.isHorizontal()) {
                for (int i = 0; i < count; i++) {
                    if (i < 2) {
                        result.add(i == 0 ? gradientBrush.color1() : gradientBrush.color2());
                    } else {
                        result.add(i == 2 ? gradientBrush.color2() : gradientBrush.color1());
                    }
                }
            } else {
                for (int i = 0; i < count; i++) {
                    if (i < 2) {
                        result.add(gradientBrush.color1());
                    } else {
                        result.add(gradientBrush.color2());
                    }
                }
            }
            return result;
        }

        if (brush.isAvailable(2)) {
            Color c1 = brush.color1();
            Color c2 = brush.color2();

            for (int i = 0; i < count; i++) {
                result.add(i % 2 == 0 ? c1 : c2);
            }
            return result;
        }

        Color singleColor = brush.isAvailable(1) ? brush.color1() : Color.BLACK;

        for (int i = 0; i < count; i++) {
            result.add(singleColor);
        }

        return result;
    }

    public static Color[] fillArray(Brush brush, int count) {
        if (brush == null || count <= 0) {
            return new Color[0];
        }

        Color[] result = new Color[count];

        if (brush instanceof GradientBrush gradientBrush) {
            if (gradientBrush.isHorizontal()) {
                for (int i = 0; i < count; i++) {
                    if (i < 2) {
                        result[i] = (i == 0 ? gradientBrush.color1() : gradientBrush.color2());
                    } else {
                        result[i] = (i == 2 ? gradientBrush.color2() : gradientBrush.color1());
                    }
                }
            } else {
                for (int i = 0; i < count; i++) {
                    if (i < 2) {
                        result[i] = gradientBrush.color1();
                    } else {
                        result[i] = gradientBrush.color2();
                    }
                }
            }
            return result;
        }

        if (brush.isAvailable(2)) {
            Color c1 = brush.color1();
            Color c2 = brush.color2();

            for (int i = 0; i < count; i++) {
                result[i] = (i % 2 == 0 ? c1 : c2);
            }
            return result;
        }

        Color singleColor = brush.isAvailable(1) ? brush.color1() : Color.BLACK;

        for (int i = 0; i < count; i++) {
            result[i] = singleColor;
        }

        return result;
    }
}
