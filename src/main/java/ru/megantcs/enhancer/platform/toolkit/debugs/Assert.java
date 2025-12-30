package ru.megantcs.enhancer.platform.toolkit.debugs;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@SuppressWarnings("all")
public class Assert
{
    private static final String defaultAssertKey = "@assert";

    public static Path throwExistFile(Path path, String comment) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(comment);

        if(!Files.exists(path))
            throw new RuntimeException("file not found: " + path + ": " + comment);

        return path;
    }

    public static Path throwExistFile(String path, String comment) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(comment);
        return throwExistFile(Path.of(path), comment);
    }

    public static Path throwExistFile(String path) {
        Objects.requireNonNull(path);
        return throwExistFile(Path.of(path), defaultAssertKey);
    }

    public static Path throwExistFile(Path path) {
        Objects.requireNonNull(path);
        return throwExistFile(path, defaultAssertKey);
    }

    public static String throwEmpty(String text, String comment) {
        if(text.isEmpty())
            throw new IllegalArgumentException(comment);

        return text;
    }

    public static String throwEmptyOrWhiteSpace(String text, String comment) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(comment);

        if(text.isBlank())
            throw new IllegalArgumentException(comment);


        return text;
    }

    public static String throwEmpty(String text) {
        Objects.requireNonNull(text);

        if(text.isEmpty())
            throw new IllegalArgumentException(String.format("line is empty: %s", text));

        return text;
    }

    public static String throwEmptyOrWhiteSpace(String text) {
        Objects.requireNonNull(text);

        if(text.isBlank())
            throw new IllegalArgumentException(String.format("line is empty or whitespace: %s", text));


        return text;
    }

    public static InputStream throwExistResource(Class<?> sender, String path, String comment) {
        Objects.requireNonNull(sender);
        Objects.requireNonNull(path);
        Objects.requireNonNull(comment);

        InputStream st = sender.getClassLoader().getResourceAsStream(path);
        if (st == null) {
            throw new IllegalArgumentException(comment);
        }
        return st;
    }

    public static InputStream throwExistResource(Class<?> sender, String path) {
        Objects.requireNonNull(sender);
        Objects.requireNonNull(path);

        return throwExistResource(sender, path, String.format("file not found: %s%s",path, defaultAssertKey));
    }

    public static <T extends Number> T throwMoreNumber(T src, T max, String comment) {
        Objects.requireNonNull(src, "source value cannot be null");
        Objects.requireNonNull(max, "max value cannot be null");
        Objects.requireNonNull(comment, "comment cannot be null");

        if(src.doubleValue() > max.doubleValue())
            throw new IllegalArgumentException(comment);

        return src;
    }

    public static <T extends Number> T throwLessNumber(T src, T min, String comment) {
        Objects.requireNonNull(src, "source value cannot be null");
        Objects.requireNonNull(min, "min value cannot be null");
        Objects.requireNonNull(comment, "comment cannot be null");

        if(src.doubleValue() < min.doubleValue())
            throw new IllegalArgumentException(comment);

        return src;
    }

    public static <T extends Number> T throwRangeNumber(T src, T min, T max, String comment) {
        Objects.requireNonNull(src, "source value cannot be null");
        Objects.requireNonNull(min, "min value cannot be null");
        Objects.requireNonNull(max, "max value cannot be null");
        Objects.requireNonNull(comment, "comment cannot be null");

        if(src.doubleValue() > max.doubleValue() || src.doubleValue() < min.doubleValue()) {
            throw new IllegalArgumentException(comment);
        }

        return src;
    }

    public static int throwMoreInt(int src, int max) {
        return Assert.throwMoreNumber(Integer.valueOf(src), Integer.valueOf(max));
    }

    public static float throwMoreFloat(float src, float max) {
        return Assert.throwMoreNumber(Float.valueOf(src), Float.valueOf(max));
    }

    public static double throwMoreDouble(double src, double max) {
        return Assert.throwMoreNumber(Double.valueOf(src), Double.valueOf(max));
    }

    public static short throwMoreShort(short src, short max) {
        return Assert.throwMoreNumber(Short.valueOf(src), Short.valueOf(max));
    }

    public static byte throwMoreByte(byte src, byte max) {
        return Assert.throwMoreByte(Byte.valueOf(src), Byte.valueOf(max));
    }

    public static int throwMoreInt(int src, int max, String comment) {
        return Assert.throwMoreNumber(Integer.valueOf(src), Integer.valueOf(max), comment);
    }

    public static float throwMoreFloat(float src, float max, String comment) {
        return Assert.throwMoreNumber(Float.valueOf(src), Float.valueOf(max), comment);
    }

    public static double throwMoreDouble(double src, double max, String comment) {
        return Assert.throwMoreNumber(Double.valueOf(src), Double.valueOf(max), comment);
    }

    public static short throwMoreShort(short src, short max, String comment) {
        return Assert.throwMoreNumber(Short.valueOf(src), Short.valueOf(max), comment);
    }

    public static byte throwMoreByte(byte src, byte max, String comment) {
        return Assert.throwMoreByte(Byte.valueOf(src), Byte.valueOf(max), comment);
    }

    public static int throwLessInt(int src, int min, String comment) {
        return throwLessNumber(Integer.valueOf(src), Integer.valueOf(min), comment);
    }

    public static float throwLessFloat(float src, float min, String comment) {
        return throwLessNumber(Float.valueOf(src), Float.valueOf(min), comment);
    }

    public static double throwLessDouble(double src, double min, String comment) {
        return throwLessNumber(Double.valueOf(src), Double.valueOf(min), comment);
    }

    public static short throwLessShort(short src, short min, String comment) {
        return throwLessNumber(Short.valueOf(src), Short.valueOf(min), comment);
    }

    public static byte throwLessByte(byte src, byte min, String comment) {
        return throwLessNumber(Byte.valueOf(src), Byte.valueOf(min), comment);
    }

    public static int throwLessInt(int src, int min) {
        return throwLessNumber(Integer.valueOf(src), Integer.valueOf(min));
    }

    public static float throwLessFloat(float src, float min) {
        return throwLessNumber(Float.valueOf(src), Float.valueOf(min));
    }

    public static double throwLessDouble(double src, double min) {
        return throwLessNumber(Double.valueOf(src), Double.valueOf(min));
    }

    public static short throwLessShort(short src, short min) {
        return throwLessNumber(Short.valueOf(src), Short.valueOf(min));
    }

    public static byte throwLessByte(byte src, byte min) {
        return throwLessNumber(Byte.valueOf(src), Byte.valueOf(min));
    }

    public static int throwRangeInt(int src, int min, int max) {
        return throwRangeNumber(Integer.valueOf(src), Integer.valueOf(min), Integer.valueOf(max));
    }

    public static float throwRangeFloat(float src, float min, float max) {
        return throwRangeNumber(Float.valueOf(src), Float.valueOf(min), Float.valueOf(max));
    }

    public static double throwRangeDouble(float src, float min, float max) {
        return throwRangeNumber(Double.valueOf(src), Double.valueOf(min), Double.valueOf(max));
    }

    public static double throwRangeShort(short src, short min, short max) {
        return throwRangeNumber(Short.valueOf(src), Short.valueOf(min), Short.valueOf(max));
    }

    public static byte throwRangeByte(byte src, byte min, byte max) {
        return throwRangeNumber(Byte.valueOf(src), Byte.valueOf(min), Byte.valueOf(max));
    }

    public static int throwRangeInt(int src, int min, int max, String comment) {
        return throwRangeNumber(Integer.valueOf(src), Integer.valueOf(min), Integer.valueOf(max), comment);
    }

    public static float throwRangeFloat(float src, float min, float max, String comment) {
        return throwRangeNumber(Float.valueOf(src), Float.valueOf(min), Float.valueOf(max), comment);
    }

    public static double throwRangeDouble(float src, float min, float max, String comment) {
        return throwRangeNumber(Double.valueOf(src), Double.valueOf(min), Double.valueOf(max), comment);
    }

    public static double throwRangeShort(short src, short min, short max, String comment) {
        return throwRangeNumber(Short.valueOf(src), Short.valueOf(min), Short.valueOf(max), comment);
    }

    public static byte throwRangeByte(byte src, byte min, byte max, String comment) {
        return throwRangeNumber(Byte.valueOf(src), Byte.valueOf(min), Byte.valueOf(max), comment);
    }

    public static <T extends Number> T throwRangeNumber(T src, T min, T max) {
        return throwRangeNumber(src, min, max, String.format("%s, %s < %s || %s > %s", defaultAssertKey, src, min, src, max));
    }

    public static <T extends Number> T throwLessNumber(T src, T min) {
        return throwLessNumber(src, min,  String.format("%s, %s < %s", defaultAssertKey, src, min));
    }

    public static <T extends Number> T throwMoreNumber(T src, T max) {
        return throwMoreNumber(src, max,  String.format("%s, %s > %s", defaultAssertKey, src, max));
    }
}
