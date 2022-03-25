package ui;

import model.ProductivityEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.time.LocalTime;
import java.util.*;

// creates a graph (line and scatter plot) of the daily averages of all productivity entries
public class GraphPanel extends JPanel {

    private static final Color[] DATA_COLORS = new Color[]{
        new Color(255, 0, 0), // ENERGY
        new Color(100, 255, 0), // FOCUS
        new Color(0, 200, 255) // MOTIVATION
    };

    private static final Color GRID_COLOR = new Color(200, 200, 200);

    private Graphics2D g2d;

    private int scaleX; // means scaleX pixels in one X unit
    private int scaleY; // means scaleY pixels in one Y unit
    private int pointRadius;

    private HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Double>> averageLog;

    // EFFECTS: constructs a graph panel with a given averageLog
    public GraphPanel(HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Double>> averageLog) {
        this.averageLog = averageLog;
    }

    // MODIFIES: this
    // EFFECTS: calls methods to paint graph
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;

        scaleX = getWidth() / 25;
        scaleY = getHeight() / 12;
        pointRadius = getHeight() / 150;

        setBackgroundColor();
        drawGrid();
        plotData();
    }

    // MODIFIES: this
    // EFFECTS: draws grid lines for graph
    private void drawGrid() {
        g2d.setStroke(new BasicStroke(1));

        drawVerticalGridLines();
        drawHorizontalGridLines();

        drawAxis();
        setTitle();
    }

    // MODIFIES: this
    // EFFECTS: draws 11 vertical grid lines
    private void drawVerticalGridLines() {
        for (int x = 1; x < 25; x++) {
            g2d.setColor(GRID_COLOR);
            g2d.drawLine(x * scaleX, 0, x * scaleX, getHeight());
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(LocalTime.of(x - 1, 0)), x * scaleX - 16, scaleY * 11 + 15);
        }
    }

    // MODIFIES: this
    // EFFECTS: draws 11 horizontal grid lines
    private void drawHorizontalGridLines() {
        for (int y = 1; y < 12; y++) {
            g2d.setColor(GRID_COLOR);
            g2d.drawLine(0, y * scaleY, getWidth(), y * scaleY);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(11 - y), scaleX - 20, y * scaleY + 5);
        }
    }

    // MODIFIES: this
    // EFFECTS: draws axis lines, with color = black and width = 2
    private void drawAxis() {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(scaleX, 0, scaleX, getHeight());
        g2d.drawLine(0, scaleY * 11, getWidth(), scaleY * 11);
    }

    // MODIFIES: this
    // EFFECTS: draws a title for the graph
    private void setTitle() {
        g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, g2d.getFont().getSize() * 2));
        g2d.drawString("Productivity Levels Over Time", getWidth() / 2 - 150, getHeight() / 20);
        g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, g2d.getFont().getSize() / 2));
    }

    // MODIFIES: this
    // EFFECTS: makes the graph background white
    private void setBackgroundColor() {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    // MODIFIES: this
    // EFFECTS: plots data points as points and as line
    private void plotData() {
        int count = 0;
        for (Map.Entry<ProductivityEntry.Label, TreeMap<LocalTime, Double>> mapSet : averageLog.entrySet()) {
            g2d.setColor(DATA_COLORS[count]);

            int prevX = -1;
            double prevY = -1.0;
            for (Map.Entry<LocalTime, Double> entry : mapSet.getValue().entrySet()) {
                //legend, !!! change pos of legend, this place should be time axis label instead
                g2d.drawString(mapSet.getKey().toString(), getWidth() / 2 - (count - 1) * 2 * scaleX, scaleY * 11 + 40);

                int posX = entry.getKey().getHour() + 1;
                double posY = 11 - entry.getValue();

                drawEllipse(posX, posY);
                drawLineBetweenPoints(prevX, prevY, posX, posY);

                prevX = posX;
                prevY = posY;
            }
            count++;
        }
    }

    // MODIFIES: this
    // EFFECTS: draws ellipse onto graph to create a scatter-plot
    private void drawEllipse(int posX, double posY) {
        Ellipse2D dot = new Ellipse2D.Double(
                posX * scaleX - pointRadius,
                posY * scaleY - pointRadius,
                2 * pointRadius, 2 * pointRadius
        );
        g2d.fill(dot);
    }

    // MODIFIES: this
    // EFFECTS: connects ellipses on graph together to create a line graph
    private void drawLineBetweenPoints(int prevX, double prevY, int posX, double posY) {
        if (prevX > 0) {
            Shape l = new Line2D.Double(
                    prevX * scaleX,
                    prevY * scaleY,
                    posX * scaleX,
                    posY * scaleY
            );
            g2d.draw(l);
        }
    }
}