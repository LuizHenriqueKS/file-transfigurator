package br.zul.filetransfigurator.core;

import java.awt.Color;

public class ColorUtils {

    private final static int X = 36;
    private final static int Y = 7;

    public static Color getColor(byte b) {
        int i = b - Byte.MIN_VALUE;
        int cr = (i % Y) * X;
        int cg = ((i / Y) % Y) * X;
        int cb = ((i / Y) / Y) * X;
        return new Color(cr, cg, cb);
    }

    public static byte getByte(Color color) {
        int r = color.getRed() / X;
        int g = (color.getGreen() / X) * Y;
        int b = (color.getBlue() / X) * Y * Y;
        return (byte) ((r + g + b) + Byte.MIN_VALUE);
    }

    public static byte getByte(int r, int g, int b) {
        r = r / X;
        g = (g / X) * Y;
        b = (b / X) * Y * Y;
        return (byte) ((r + g + b) + Byte.MIN_VALUE);
    }

}