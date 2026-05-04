package vista.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.UIManager;

import vista.theme.ThemeManager;

public class BarChartPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private String title = "";
    private String[] labels = new String[] { "", "", "", "" };
    private int[] values = new int[] { 0, 0, 0, 0 };

    public BarChartPanel() {
        setOpaque(false);
    }

    public void setChart(String title, String[] labels, int[] values) {
        this.title = title == null ? "" : title;
        this.labels = labels == null ? new String[] { "", "", "", "" } : labels;
        this.values = values == null ? new int[] { 0, 0, 0, 0 } : values;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int padLeft = 42;
        int padRight = 16;
        int padTop = 40;
        int padBottom = 36;

        g2.setColor(ThemeManager.TEXT_PRIMARY);
        g2.setFont(uiFont(Font.BOLD, 16f));
        g2.drawString(title, 12, 24);

        int chartX = padLeft;
        int chartY = padTop;
        int chartW = Math.max(10, w - padLeft - padRight);
        int chartH = Math.max(10, h - padTop - padBottom);

        g2.setColor(new Color(229, 233, 240));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(chartX, chartY + chartH, chartX + chartW, chartY + chartH);
        g2.drawLine(chartX, chartY, chartX, chartY + chartH);

        int max = 1;
        for (int v : values) {
            if (v > max) {
                max = v;
            }
        }
        int levels = Math.max(4, max);
        g2.setFont(uiFont(Font.PLAIN, 11f));
        for (int i = 0; i <= levels; i++) {
            int y = chartY + chartH - (int) ((i / (double) levels) * chartH);
            g2.setColor(new Color(237, 241, 246));
            g2.drawLine(chartX, y, chartX + chartW, y);
            g2.setColor(ThemeManager.TEXT_SECONDARY);
            g2.drawString(String.valueOf(i), 22, y + 4);
        }

        Color[] barColors = new Color[] {
            new Color(76, 201, 129),
            new Color(84, 153, 237),
            new Color(255, 127, 39),
            new Color(200, 205, 214)
        };

        int n = Math.max(1, values.length);
        int slotW = chartW / n;
        int barW = Math.max(24, (int) (slotW * 0.56));

        FontMetrics fm = g2.getFontMetrics(uiFont(Font.PLAIN, 12f));
        g2.setFont(uiFont(Font.PLAIN, 12f));
        for (int i = 0; i < n; i++) {
            int value = i < values.length ? Math.max(0, values[i]) : 0;
            int barH = max == 0 ? 0 : (int) ((value / (double) max) * (chartH - 6));
            int x = chartX + i * slotW + (slotW - barW) / 2;
            int y = chartY + chartH - barH;

            g2.setColor(barColors[i % barColors.length]);
            g2.fillRoundRect(x, y, barW, barH, 12, 12);

            String label = i < labels.length ? labels[i] : "";
            g2.setColor(ThemeManager.TEXT_SECONDARY);
            int textW = fm.stringWidth(label);
            g2.drawString(label, x + (barW - textW) / 2, chartY + chartH + 18);
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

