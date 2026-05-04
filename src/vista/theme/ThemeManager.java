package vista.theme;

import java.awt.Color;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

public final class ThemeManager {

    public static final Color PRIMARY = new Color(37, 99, 235);
    public static final Color BACKGROUND = new Color(238, 242, 247);
    public static final Color SURFACE = new Color(255, 255, 255);
    public static final Color TEXT_PRIMARY = new Color(31, 41, 55);
    public static final Color TEXT_SECONDARY = new Color(75, 85, 99);
    public static final Color SUCCESS = new Color(25, 135, 84);
    public static final Color FAVORITE = new Color(255, 193, 7);
    public static final Color BORDER = new Color(222, 226, 230);

    private ThemeManager() {
    }

    public static void setupLookAndFeel() {
        try {
            System.setProperty("flatlaf.uiScale", "1.0");
            System.setProperty("flatlaf.useWindowDecorations", "true");
            FlatLightLaf.setup();
        } catch (Exception ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException fallbackEx) {
                // ignore
            } catch (InstantiationException fallbackEx) {
                // ignore
            } catch (IllegalAccessException fallbackEx) {
                // ignore
            } catch (UnsupportedLookAndFeelException fallbackEx) {
                // ignore
            }
        }

        applyCustomDefaults();
    }

    public static void applyCustomDefaults() {
        Border textBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(6, 8, 6, 8));

        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("TabbedPane.background", BACKGROUND);
        UIManager.put("TabbedPane.selectedBackground", PRIMARY);
        UIManager.put("TabbedPane.underlineColor", PRIMARY);
        UIManager.put("TabbedPane.focusColor", new Color(156, 197, 255));
        UIManager.put("TabbedPane.tabArc", 12);
        UIManager.put("TabbedPane.tabInsets", new javax.swing.plaf.InsetsUIResource(10, 14, 10, 14));
        UIManager.put("TabbedPane.selectedForeground", Color.WHITE);
        UIManager.put("TabbedPane.foreground", TEXT_SECONDARY);

        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ProgressBar.arc", 999);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Component.innerFocusWidth", 0);
        UIManager.put("Component.focusColor", new Color(157, 197, 255));

        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.background", SURFACE);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.border", textBorder);

        UIManager.put("ComboBox.background", SURFACE);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);

        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("Table.alternateRowColor", new Color(246, 248, 252));
        UIManager.put("Table.selectionBackground", new Color(197, 221, 255));
        UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("Table.showHorizontalLines", false);
        UIManager.put("Table.showVerticalLines", false);

        UIManager.put("MenuItem.selectionBackground", new Color(219, 234, 254));
        UIManager.put("MenuItem.selectionForeground", TEXT_PRIMARY);

        UIManager.put("ProgressBar.foreground", PRIMARY);
        UIManager.put("ProgressBar.background", SURFACE);
    }
}









