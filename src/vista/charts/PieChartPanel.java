package vista.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.UIManager;

import vista.theme.ThemeManager;

public class PieChartPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private String title = "";
    private String[] labels = new String[] { "", "", "" };
    private int[] values = new int[] { 0, 0, 0 };
    private final Color[] colors = new Color[] {
        new Color(76, 201, 129),
        new Color(84, 153, 237),
        new Color(255, 127, 39)
    };

    public PieChartPanel() {
        setOpaque(false);
    }

    public void setChart(String title, String[] labels, int[] values) {
        this.title = title == null ? "" : title;
        this.labels = labels == null ? new String[] { "", "", "" } : labels;
        this.values = values == null ? new int[] { 0, 0, 0 } : values;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(ThemeManager.TEXT_PRIMARY);
        g2.setFont(uiFont(Font.BOLD, 16f));
        g2.drawString(title, 12, 24);

        int pieSize = Math.min(200, Math.min(w - 160, h - 70));
        pieSize = Math.max(120, pieSize);
        int pieX = 20;
        int pieY = 40;

        int total = 0;
        for (int v : values) {
            total += Math.max(0, v);
        }

        if (total == 0) {
            g2.setColor(new Color(226, 232, 238));
            g2.fillOval(pieX, pieY, pieSize, pieSize);
            g2.setColor(ThemeManager.TEXT_SECONDARY);
            g2.setFont(uiFont(Font.PLAIN, 12f));
            g2.drawString("Sin datos", pieX + pieSize / 2 - 24, pieY + pieSize / 2 + 4);
            g2.dispose();
            return;
        }

        int start = 0;
        for (int i = 0; i < values.length; i++) {
            int v = Math.max(0, values[i]);
            int angle = (int) Math.round((v * 360.0) / total);
            g2.setColor(colors[i % colors.length]);
            g2.fillArc(pieX, pieY, pieSize, pieSize, start, angle);
            start += angle;
        }

        int legendX = pieX + pieSize + 18;
        int legendY = pieY + 16;
        g2.setFont(uiFont(Font.PLAIN, 13f));
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < values.length; i++) {
            int y = legendY + i * 28;
            g2.setColor(colors[i % colors.length]);
            g2.fillRoundRect(legendX, y, 14, 14, 4, 4);

            String txt = labels[i] + ": " + values[i];
            g2.setColor(ThemeManager.TEXT_SECONDARY);
            g2.drawString(txt, legendX + 22, y + fm.getAscent());
        }

        g2.dispose();
    }

    private Font uiFont(int style, float size) {
        Font base = UIManager.getFont("Label.font");
        if (base == null) {
            return new Font("Segoe UI", style, (int) size);
        }
        return base.deriveFont(style, size);
    }
}

