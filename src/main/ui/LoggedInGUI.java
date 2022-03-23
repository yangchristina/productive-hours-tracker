package ui;

import model.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;


import org.jfree.data.category.DefaultCategoryDataset;

public class LoggedInGUI {
    private static final Object[] ENTRY_TYPE_OPTIONS = {ProductivityEntry.Label.ENERGY, ProductivityEntry.Label.FOCUS,
            ProductivityEntry.Label.MOTIVATION};
    private static final Object[] LEVEL_OPTIONS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    private static final Object[] TIME_OPTIONS = createTimeOptions();


    private User user;
    private UserList userList;
    private boolean wasSaved;

    private JFrame frame;
    private JPanel panel;
    private ChartPanel chartPanel;

    private GraphPanel graphPanel;

    private JList<ListModel<ProductivityEntry>> entryList;
    private DefaultListModel<ProductivityEntry> listModel;

    private JMenuBar menuBar;

    private final Day today = new Day();
    private TimeSeries energyTimeSeries;
    private TimeSeries focusTimeSeries;
    private TimeSeries motivationTimeSeries;

    public LoggedInGUI(User user, UserList users) {
        this.user = user;
        this.userList = users;
        initPanel();
        initMenuBar();
//        initGraph();
        graphPanel =  new GraphPanel(user.getProductivityLog().getDailyAverageLog().getLog());
        initEntryList();
        initAddEntryButton();
        initEditEntryButton();
        initDeleteEntryButton();
        initFrame();
    }

    private void initPanel() {
        panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //makes scrollable stop working
        JLabel label = new JLabel("Energy entries:");
        panel.add(label);
    }

    private void initGraph() {
        // Create dataset
        XYDataset dataset = createDataset();
        // Create chart
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Daily Productivity Levels", // Chart title
                "Time", // X-Axis Label
                "Level", // Y-Axis Label
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setDefaultShapesVisible(true);
//        renderer.setBaseShapesVisible(true);

//        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
//        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();

        chartPanel = new ChartPanel(chart);
    }

    private XYDataset createDataset() {
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        energyTimeSeries = createTimeSeries(ProductivityEntry.Label.ENERGY);
        focusTimeSeries = createTimeSeries(ProductivityEntry.Label.FOCUS);
        motivationTimeSeries = createTimeSeries(ProductivityEntry.Label.MOTIVATION);
        tsc.addSeries(energyTimeSeries);
        tsc.addSeries(focusTimeSeries);
        tsc.addSeries(motivationTimeSeries);
        return tsc;
    }

    // !!! combine below three methods into one
    private TimeSeries createTimeSeries(ProductivityEntry.Label label) {
        TimeSeries series = new TimeSeries(label);

        for (Map.Entry<LocalTime, Integer> entry
                : user.getProductivityLog().getDailyAverageLog().getLog().get(label).entrySet()) {
            series.add(new Hour(entry.getKey().getHour(), today), entry.getValue());
        }

        return series;
    }

//    private DefaultCategoryDataset createDataset() {
//        dataset = new DefaultCategoryDataset();
//
//        for (Map.Entry<LocalTime, Integer> entry
//                : user.getProductivityLog().getDailyAverageLog().getEnergyAverages().entrySet()) {
//            dataset.addValue(entry.getValue(), ProductivityEntry.Label.ENERGY, entry.getKey());
//        }
//
//        for (Map.Entry<LocalTime, Integer> entry
//                : user.getProductivityLog().getDailyAverageLog().getFocusAverages().entrySet()) {
//            dataset.addValue(entry.getValue(), ProductivityEntry.Label.FOCUS, entry.getKey());
//        }
//
//        for (Map.Entry<LocalTime, Integer> entry
//                : user.getProductivityLog().getDailyAverageLog().getMotivationAverages().entrySet()) {
//            dataset.addValue(entry.getValue(), ProductivityEntry.Label.MOTIVATION, entry.getKey());
//        }
//
//        return dataset;
//    }


    public void initMenuBar() {
        menuBar = new JMenuBar();

        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        menuBar.add(m1);
        menuBar.add(m2);

        JMenuItem m11 = new JMenuItem("Logout");
        m11.addActionListener(ev -> {
            endUserSession();
            new LoggedOutGUI();
            frame.dispose();
        });

        JMenuItem m22 = new JMenuItem("Save");
        m22.addActionListener(ev -> {
            save();
            System.out.println("saved :)");
        });
        m1.add(m11);
        m1.add(m22);
    }

    // MODIFIES: this
    // EFFECTS: adds button to panel which adds an entry on click
    private void initAddEntryButton() {
        JButton add = new JButton(new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("add clicked");
                ProductivityEntry entry = createDefaultEntry();
                if (entryOptionForm(entry)) { // true means added, false means not added
                    int newAverageLevel = user.getProductivityLog().add(entry);
                    listModel.addElement(entry);
                    updateTimeSeries(entry, newAverageLevel);
                    graphPanel.revalidate();
                    graphPanel.repaint();
                }
            }
        });
        panel.add(add);
    }

    private void updateTimeSeries(ProductivityEntry entry, Integer newAverageLevel) {
//        switch (entry.getLabel()) {
//            case ENERGY:
//                energyTimeSeries.addOrUpdate(new Hour(entry.getTime().getHour(), today), newAverageLevel);
//                break;
//            case FOCUS:
//                focusTimeSeries.addOrUpdate(new Hour(entry.getTime().getHour(), today), newAverageLevel);
//                break;
//            case MOTIVATION:
//                motivationTimeSeries.addOrUpdate(new Hour(entry.getTime().getHour(), today), newAverageLevel);
//                break;
//        }
    }

    // MODIFIES: this
    // EFFECTS: adds button to panel which edits selected entry on click
    private void initEditEntryButton() {
        JButton edit = new JButton(new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductivityEntry selected = (ProductivityEntry) entryList.getSelectedValue();
                if (selected != null) {
                    ProductivityEntry old = new ProductivityEntry(selected.getLabel(), selected.getDate(),
                            selected.getTime(), selected.getLevel());
                    if (entryOptionForm(selected)) {
                        Integer newRemovedAverageLevel = user.getProductivityLog().getDailyAverageLog().remove(old);
                        Integer newAddedAverageLevel = user.getProductivityLog().getDailyAverageLog().add(selected);
                        JOptionPane.showMessageDialog(panel,"Entry changed to: " + selected);
//                        graphPanel.updateUI();
                        graphPanel.revalidate();
                        graphPanel.repaint();
//                        updateTimeSeries(old, newRemovedAverageLevel);
//                        updateTimeSeries(selected, newAddedAverageLevel);
                    }
                } //does editing entry change it in both listModel and arrayList? yup it does
            }
        });
        panel.add(edit);
    }

    // MODIFIES: this
    // EFFECTS: adds button to panel which deletes selected entry on click
    private void initDeleteEntryButton() {
        JButton delete = new JButton(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductivityEntry selected = (ProductivityEntry) entryList.getSelectedValue();

                Integer newAverageLevel;
                // !!! fix tests, get rid of not contained null, since its impossible
                if (selected != null) { // two cases for null, one for not contained, other for after removing no average level for that time
                    newAverageLevel = user.getProductivityLog().remove(selected);

                    JOptionPane.showMessageDialog(panel, "Removed: " + selected);

                    listModel.removeElement(selected);
                    updateTimeSeries(selected, newAverageLevel);
                    graphPanel.revalidate();
                    graphPanel.repaint();
//                    dataset.removeValue(selected.getLabel(), selected.getTime());
                }
            }
        });
        panel.add(delete);
    }


    // MODIFIES: this
    // EFFECTS: initializes entry list
    private void initEntryList() {
        listModel = new DefaultListModel<>();
        for (ProductivityEntry val : user.getProductivityLog().getEntries()) {
            listModel.addElement(val);
        }
        entryList = new JList(listModel);
        initList(entryList);
    }

    private void initList(JList<ListModel<ProductivityEntry>> list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(5);

        JScrollPane pane = new JScrollPane(list);
        pane.setPreferredSize(new Dimension(300, 80));
        panel.add(pane);
    }

    private void initFrame() {
        frame = new JFrame("Application");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200,900);

//        // Text Area at the Center - !!! change to graph later
//        JTextArea ta = new JTextArea();

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
//        frame.getContentPane().add(BorderLayout.CENTER, chartPanel);
        frame.getContentPane().add(BorderLayout.CENTER, graphPanel);

        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                endUserSession();
            }
        });
    }

    private void endUserSession() {
        promptSave();
        if (wasSaved) {
            System.out.println("was saved, now saving user list");
            saveUserList();
        }
    }

    // asks user if they would like to save their session
    // EFFECTS: if user inputs true, save session, if false don't save, else ask again
    private void promptSave() {
        System.out.println("Would you like to save?");
        if (JOptionPane.showConfirmDialog(frame, "Would you like to save?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // yes option
            save();
            System.out.println("You said yes");
        } else {
            // no option
            System.out.println("not saved");
        }
    }

    // EFFECTS: saves user to file
    private void save() {
        try {
            JsonWriter writer = new JsonWriter(user.getId().toString());

            writer.open();
            writer.write(user);
            writer.close();

            wasSaved = true;
        } catch (IOException e) {
            // idk what yet, shouldn't every be thrown cuz no illegal file names, all filenames are uuid
        }
    }

    private boolean entryOptionForm(ProductivityEntry entry) {
        ProductivityEntry.Label entryType = (ProductivityEntry.Label) JOptionPane.showInputDialog(null,
                "Which energy type?", "Option", JOptionPane.QUESTION_MESSAGE, null, ENTRY_TYPE_OPTIONS,
                entry.getLabel());
        if (entryType == null) {
            return false;
        }

        Integer level = (Integer) JOptionPane.showInputDialog(null, "Level?",
                "Option", JOptionPane.QUESTION_MESSAGE, null, LEVEL_OPTIONS, entry.getLevel());
        if (level == null) {
            return false;
        }

        LocalTime time = (LocalTime) JOptionPane.showInputDialog(null, "Time?",
                "Option", JOptionPane.QUESTION_MESSAGE, null, TIME_OPTIONS, entry.getTime());
        if (time == null) {
            return false;
        }

        entry.editLabel(entryType);
        entry.editLevel(level);
        entry.editTime(time);

        return true;
    }

    // MODIFIES: this
    // EFFECTS: removes selected entry from the productivity log
    public void removeEntry(ProductivityEntry entry) {
        System.out.println("Operation: remove");
        Integer newAverageLevel = user.getProductivityLog().remove(entry);

        System.out.println("Removed: " + entry.toString());
    }

    public boolean getWasSaved() {
        return wasSaved;
    }

    // EFFECTS: creates a default energy entry with a label of ENERGY, today's date, time of 0:0 and level of 0.
    private ProductivityEntry createDefaultEntry() {
        return new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(),
                LocalTime.of(LocalTime.now().getHour(), 0),5);
    }

    private static Object[] createTimeOptions() {
        Object[] timeOptions = new Object[24];
        for (int i = 0; i < 24; i++) {
            timeOptions[i] = LocalTime.of(i, 0);
        }
        return timeOptions;
    }

    // EFFECTS: saves user list to file
    public void saveUserList() {
        try {
            JsonWriter writer = new JsonWriter("users");

            writer.open();
            writer.write(userList);
            writer.close();
        } catch (IOException e) {
            System.out.println("fight ");
        }
    }







//    // EFFECTS: shows the user's peak hours for either focus, energy, or motivation, depending on the user's input
//    private void showPeakHours(ProductivityEntry.Label label) {
//        ArrayList<LocalTime> peakHours = user.getPeaksAndTroughs(label).get("peak");
//        if (peakHours.isEmpty()) {
//            System.out.println("Not enough " + label + " entries");
//        } else {
//            System.out.println("Your peak " + label + " hours are at " + peakHours);
//        }
//    }
//
//    // EFFECTS: shows the user's peak hours for either focus, energy, or motivation, depending on the user's input
//    private void showTroughHours() {
//        String label = input.entryType();
//        ArrayList<LocalTime> troughHours = user.getPeaksAndTroughs(label).get("trough");
//        if (troughHours.isEmpty()) {
//            System.out.println("Not enough " + label + " entries");
//        } else {
//            System.out.println("Your trough " + label + " hours are at " + troughHours);
//        }
//    }
}
