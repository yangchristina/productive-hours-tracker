package ui;

import model.ProductivityEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.time.LocalTime;
import java.util.*;

public class GraphPanel extends JPanel {
//    private static final int padding = 25;

    private static final Color[] DATA_COLORS = new Color[]{
        new Color(255, 0, 0), // Energy
        new Color(100, 255, 0), // Focus
        new Color(0, 200, 255) // MOTIVATION
    };

    private ArrayList<Point2D.Float> points;

    private static final Color GRID_COLOR = new Color(200, 200, 200);

    private int scaleX; // means __ pixels in one X unit
    private int scaleY; // means __ pixels in one X unit
    private int pointRadius;

    private ArrayList<Point2D.Float> energyPoints;
    private ArrayList<Point2D.Float> focusPoints;
    private ArrayList<Point2D.Float> motivationPoints;

    private HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Integer>> averageLog;

    public GraphPanel(HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Integer>> averageLog) {
        this.averageLog = averageLog;
//        energyPoints = new ArrayList<>();
//        focusPoints = new ArrayList<>();
//        motivationPoints = new ArrayList<>();


//        for (Map.Entry<LocalTime, Integer> entry : averageLog.get(ProductivityEntry.Label.ENERGY).entrySet()) {
//            if (entry.getKey() != null) {
//                energyPoints.add(new Point2D.Float(entry.getKey().getHour(), entry.getValue()));
//            }
//        }
//        System.out.println("energy points");
//        System.out.println(energyPoints);
//        for (Map.Entry<LocalTime, Integer> entry : averageLog.get(ProductivityEntry.Label.FOCUS).entrySet()) {
//            if (entry.getKey() != null) {
//                focusPoints.add(new Point2D.Float(entry.getKey().getHour() + 1, entry.getValue()));
//            }
//        }
//        for (Map.Entry<LocalTime, Integer> entry : averageLog.get(ProductivityEntry.Label.MOTIVATION).entrySet()) {
//            if (entry.getKey() != null) {
//                motivationPoints.add(new Point2D.Float(entry.getKey().getHour() + 1, entry.getValue()));
//            }
//        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        scaleX = getWidth() / 25;
        scaleY = getHeight() / 12;
        pointRadius = getHeight() / 150;

        //paint background white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // draw grid
        g2d.setStroke(new BasicStroke(1));
        // draw <23 vertical lines
        for (int x = 1; x < 25; x++) {
            g2d.setColor(GRID_COLOR);
            g2d.drawLine(x * scaleX, 0, x * scaleX, getHeight());
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(LocalTime.of(x - 1, 0)), x * scaleX - 16, scaleY * 11 + 15);
//            g2d.drawLine(x * scaleX, getHeight(), x * scaleX, -getHeight());
        }

        // draw <11 horizontal lines
        for (int y = 1; y < 12; y++) {
            g2d.setColor(GRID_COLOR);
            g2d.drawLine(0, y * scaleY, getWidth(), y * scaleY);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(11 - y), scaleX - 20, y * scaleY + 5);
        }

        // draw axis
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(scaleX, 0, scaleX, getHeight());
        g2d.drawLine(0, scaleY * 11, getWidth(), scaleY * 11);

        // title
        g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, g2d.getFont().getSize() * 2));
        g2d.drawString("Productivity Levels Over Time", getWidth() / 2 - 150, getHeight() / 20);
        g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, g2d.getFont().getSize() / 2));

        plotData(g2d);

//        g2d.setColor(DATA_COLORS[0]);
//        plotData(energyPoints, g2d);
//
//        g2d.setColor(DATA_COLORS[1]);
//        plotData(focusPoints, g2d);
//
//        g2d.setColor(DATA_COLORS[2]);
//        plotData(motivationPoints, g2d);
    }

    private void plotData(Graphics2D g2d) {
        int count = 0;
        for (Map.Entry<ProductivityEntry.Label, TreeMap<LocalTime, Integer>> mapSet : averageLog.entrySet()) {
            g2d.setColor(DATA_COLORS[count]);

            int prevX = -1;
            int prevY = -1;
            for (Map.Entry<LocalTime, Integer> entry : mapSet.getValue().entrySet()) {
                //legend, !!! change pos of legend, this place should be time axis label instead
                g2d.drawString(mapSet.getKey().toString(), getWidth() / 2 - (count - 1) * 2 * scaleX, scaleY * 11 + 40);

                int posX = entry.getKey().getHour() + 1;
                int posY = 11 - entry.getValue();

                Ellipse2D dot = new Ellipse2D.Float(
                        posX * scaleX - pointRadius,
                        posY * scaleY - pointRadius,
                        2 * pointRadius, 2 * pointRadius
                );
                g2d.fill(dot);
                if (prevX > 0) {
                    g2d.drawLine(
                            prevX * scaleX,
                            prevY * scaleY,
                            posX * scaleX,
                            posY * scaleY
                    );
                }
                prevX = posX;
                prevY = posY;
            }
            count++;
        }
    }
//
//    // MODIFIES: g2d
//    // EFFECTS: plots data points as line and as points
//    private void plotData(ArrayList<Point2D.Float> points, Graphics2D g2d) {
//
//
//
//        Point2D.Float prevPoint = null;
//        for (Point2D.Float point : points) {
//            Ellipse2D dot = new Ellipse2D.Float(
//                    (point.x + 1) * scaleX - pointRadius,
//                    (11 - point.y) * scaleY - pointRadius,
//                    2 * pointRadius, 2 * pointRadius
//            );
//            g2d.fill(dot);
//            if (prevPoint != null) {
//                g2d.drawLine(
//                        (int) ((prevPoint.x + 1) * scaleX),
//                        (int) (11 - prevPoint.y) * scaleY,
//                        (int) (point.x + 1) * scaleX,
//                        (int) (11 - point.y) * scaleY
//                );
//            }
//            prevPoint = point;
//        }
//    }
}