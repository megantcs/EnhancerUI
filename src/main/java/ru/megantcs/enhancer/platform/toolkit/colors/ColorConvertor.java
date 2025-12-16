package ru.megantcs.enhancer.platform.toolkit.Colors;

import java.awt.*;
import java.util.Objects;

public class ColorConvertor
{
    public static String withAlpha(String hexColor, float alpha) {
        if (hexColor == null || hexColor.isEmpty()) {
            return hexColor;
        }

        String color = hexColor.startsWith("#") ? hexColor : "#" + hexColor;

        int alphaValue = Math.max(0, Math.min(255, (int)(alpha * 255)));
        String alphaHex = String.format("%02X", alphaValue);

        if (color.length() == 9) {
            return "#" + alphaHex + color.substring(3);
        }
        else if (color.length() == 7) {
            return "#" + alphaHex + color.substring(1);
        }
        else if (color.length() == 4) {
            String r = color.substring(1, 2);
            String g = color.substring(2, 3);
            String b = color.substring(3, 4);
            return "#" + alphaHex + r + r + g + g + b + b;
        }
        else if (color.length() == 5) {
            String a = color.substring(1, 2);
            String r = color.substring(2, 3);
            String g = color.substring(3, 4);
            String b = color.substring(4, 5);
            return "#" + alphaHex + r + r + g + g + b + b;
        }

        return color;
    }

    public static Color hexToColor(String hex) {
        if (hex == null) {
            return Color.BLACK;
        }

        String lowerHex = hex.toLowerCase().trim();
        switch (lowerHex) {
            case "white": return Color.WHITE;
            case "red": return Color.RED;
            case "green": return Color.GREEN;
            case "blue": return Color.BLUE;
            case "black": return Color.BLACK;
            case "yellow": return Color.YELLOW;
            case "cyan": return Color.CYAN;
            case "magenta": return Color.MAGENTA;
            case "gray": return Color.GRAY;
            case "lightgray": return Color.LIGHT_GRAY;
            case "darkgray": return Color.DARK_GRAY;
            case "pink": return Color.PINK;
            case "orange": return Color.ORANGE;
        }

        String cleanHex = hex.startsWith("#") ? hex.substring(1) : hex;

        if (cleanHex.isEmpty()) {
            return Color.BLACK;
        }

        try {
            switch (cleanHex.length()) {
                case 3:
                    cleanHex = "" +
                            cleanHex.charAt(0) + cleanHex.charAt(0) +
                            cleanHex.charAt(1) + cleanHex.charAt(1) +
                            cleanHex.charAt(2) + cleanHex.charAt(2);
                case 6:
                    int rgb = Integer.parseInt(cleanHex, 16);
                    return new Color(
                            (rgb >> 16) & 0xFF,
                            (rgb >> 8) & 0xFF,
                            rgb & 0xFF
                    );
                case 4:
                    cleanHex = "" +
                            cleanHex.charAt(0) + cleanHex.charAt(0) +
                            cleanHex.charAt(1) + cleanHex.charAt(1) +
                            cleanHex.charAt(2) + cleanHex.charAt(2) +
                            cleanHex.charAt(3) + cleanHex.charAt(3);
                case 8:
                    long rgba = Long.parseLong(cleanHex, 16);
                    return new Color(
                            (int) ((rgba >> 16) & 0xFF),
                            (int) ((rgba >> 8) & 0xFF),
                            (int) (rgba & 0xFF),
                            (int) ((rgba >> 24) & 0xFF)
                    );
                case 7:
                    if (cleanHex.matches("[0-9a-fA-F]{7}")) {
                        cleanHex = cleanHex + "F";
                        long rgba7 = Long.parseLong(cleanHex, 16);
                        return new Color(
                                (int) ((rgba7 >> 16) & 0xFF),
                                (int) ((rgba7 >> 8) & 0xFF),
                                (int) (rgba7 & 0xFF),
                                (int) ((rgba7 >> 24) & 0xFF)
                        );
                    }
                    break;
                case 9:
                    if (cleanHex.startsWith("f") || cleanHex.startsWith("F")) {
                        String shortened = cleanHex.substring(1);
                        long argb = Long.parseLong(shortened, 16);
                        return new Color(
                                (int) ((argb >> 16) & 0xFF),
                                (int) ((argb >> 8) & 0xFF),
                                (int) (argb & 0xFF),
                                (int) ((argb >> 24) & 0xFF)
                        );
                    }
                    break;
                case 12:
                    int r12 = Integer.parseInt(cleanHex.substring(0, 3), 16);
                    int g12 = Integer.parseInt(cleanHex.substring(3, 6), 16);
                    int b12 = Integer.parseInt(cleanHex.substring(6, 9), 16);
                    int a12 = Integer.parseInt(cleanHex.substring(9, 12), 16);

                    return new Color(
                            r12 * 255 / 4095,
                            g12 * 255 / 4095,
                            b12 * 255 / 4095,
                            a12 * 255 / 4095
                    );
                case 16:
                    long r16 = Long.parseLong(cleanHex.substring(0, 4), 16);
                    long g16 = Long.parseLong(cleanHex.substring(4, 8), 16);
                    long b16 = Long.parseLong(cleanHex.substring(8, 12), 16);
                    long a16 = Long.parseLong(cleanHex.substring(12, 16), 16);

                    return new Color(
                            (int) (r16 * 255 / 65535),
                            (int) (g16 * 255 / 65535),
                            (int) (b16 * 255 / 65535),
                            (int) (a16 * 255 / 65535)
                    );
                default:
                    if (cleanHex.length() >= 6) {
                        String rgbHex = cleanHex.substring(0, 6);
                        int fallbackRgb = Integer.parseInt(rgbHex, 16);
                        return new Color(
                                (fallbackRgb >> 16) & 0xFF,
                                (fallbackRgb >> 8) & 0xFF,
                                fallbackRgb & 0xFF
                        );
                    }
                    throw new IllegalArgumentException("Invalid HEX color format: " + hex);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid HEX color: " + hex, e);
        }

        return Color.BLACK;
    }

    public static String colorToHex(Color color) {
        return colorToHex(color, false, false);
    }

    public static String colorToHex(Color color, boolean withAlpha) {
        return colorToHex(color, withAlpha, false);
    }

    public static String colorToHex(Color color, boolean withAlpha, boolean shortFormat) {
        if (color == null) {
            return withAlpha ? "#00000000" : "#000000";
        }

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        StringBuilder hex = new StringBuilder("#");

        if (withAlpha) {
            if (shortFormat && canBeShortened(r, g, b, a)) {
                hex.append(toShortHex(a >> 4))
                        .append(toShortHex(r >> 4))
                        .append(toShortHex(g >> 4))
                        .append(toShortHex(b >> 4));
            } else {
                hex.append(String.format("%02X%02X%02X%02X", a, r, g, b));
            }
        } else {
            if (shortFormat && canBeShortened(r, g, b)) {
                hex.append(toShortHex(r >> 4))
                        .append(toShortHex(g >> 4))
                        .append(toShortHex(b >> 4));
            } else {
                hex.append(String.format("%02X%02X%02X", r, g, b));
            }
        }

        return hex.toString();
    }

    public static String colorToHex12Bit(Color color) {
        if (color == null) {
            return "000000000000";
        }

        int r12 = color.getRed() * 4095 / 255;
        int g12 = color.getGreen() * 4095 / 255;
        int b12 = color.getBlue() * 4095 / 255;
        int a12 = color.getAlpha() * 4095 / 255;

        return String.format("%03X%03X%03X%03X", r12, g12, b12, a12);
    }

    public static String colorToHex16Bit(Color color) {
        if (color == null) {
            return "0000000000000000";
        }

        long r16 = color.getRed() * 65535L / 255;
        long g16 = color.getGreen() * 65535L / 255;
        long b16 = color.getBlue() * 65535L / 255;
        long a16 = color.getAlpha() * 65535L / 255;

        return String.format("%04X%04X%04X%04X", r16, g16, b16, a16);
    }

    public static String colorToHexARGB(Color color) {
        if (color == null) {
            return "FF000000";
        }

        int a = color.getAlpha();
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        return String.format("F%02X%02X%02X%02X", a, r, g, b).substring(0, 9);
    }

    private static boolean canBeShortened(int... components) {
        for (int component : components) {
            int high = component >> 4;
            int low = component & 0x0F;
            if (high != low) {
                return false;
            }
        }
        return true;
    }

    private static char toShortHex(int value) {
        value = value & 0x0F;
        return (char) (value < 10 ? '0' + value : 'A' + (value - 10));
    }

    public static String colorToHexWithAlpha(Color color) {
        return colorToHex(color, true, false);
    }

    public static String colorToHexShort(Color color) {
        return colorToHex(color, false, true);
    }

    public static String colorToHexShortWithAlpha(Color color) {
        return colorToHex(color, true, true);
    }

    public static String colorToNamedHex(Color color) {
        if (color == null) return "black";

        if (color.equals(Color.WHITE)) return "white";
        if (color.equals(Color.RED)) return "red";
        if (color.equals(Color.GREEN)) return "green";
        if (color.equals(Color.BLUE)) return "blue";
        if (color.equals(Color.BLACK)) return "black";
        if (color.equals(Color.YELLOW)) return "yellow";
        if (color.equals(Color.CYAN)) return "cyan";
        if (color.equals(Color.MAGENTA)) return "magenta";
        if (color.equals(Color.GRAY)) return "gray";
        if (color.equals(Color.LIGHT_GRAY)) return "lightgray";
        if (color.equals(Color.DARK_GRAY)) return "darkgray";
        if (color.equals(Color.PINK)) return "pink";
        if (color.equals(Color.ORANGE)) return "orange";

        return colorToHex(color, color.getAlpha() < 255);
    }
}
