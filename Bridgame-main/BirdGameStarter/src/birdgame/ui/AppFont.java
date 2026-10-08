package birdgame.ui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public final class AppFont {

    private static final List<String> PREFERRED_FONTS = List.of(
            "Tahoma",
            "Leelawadee UI",
            "Noto Sans Thai",
            "Noto Sans",
            "Arial",
            "Dialog"
    );

    private static final String FONT_NAME = findAvailableFont();

    private AppFont() {}

    private static String findAvailableFont() {
        String[] installed = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();

        for (String preferred : PREFERRED_FONTS) {
            boolean found = Arrays.stream(installed)
                    .anyMatch(name -> name.equalsIgnoreCase(preferred));

            if (found) {
                return preferred;
            }
        }

        return Font.DIALOG;
    }

    public static Font plain(int size) {
        return new Font(FONT_NAME, Font.PLAIN, size);
    }

    public static Font bold(int size) {
        return new Font(FONT_NAME, Font.BOLD, size);
    }

    public static void applyToSwing() {
        FontUIResource font = new FontUIResource(plain(16));

        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);

            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }

    public static String getFontName() {
        return FONT_NAME;
    }
}
