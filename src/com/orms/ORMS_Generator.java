package com.orms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ORMS_Generator {
    protected JPanel mainFrame;
    private JTable table1;

    private static JTable table;
    private static DefaultTableModel model;
    private static String[][] data;

    public ORMS_Generator() {
        initializeUI();
    }

    private void initializeUI() {
        int numRows = 50;
        int numColumns = 160;

        model = new DefaultTableModel(numRows, numColumns);
        table = new JTable(model);

        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getTableHeader().setReorderingAllowed(false);

        scrollPane.getViewport().setPreferredSize(new Dimension(1200, 800));

        JButton saveButton = new JButton("Export Code");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataTo2DArray();
                displayDataInNewWindow();
            }
        });

        JButton saveToFileButton = new JButton("Save to File");
        saveToFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataToTxtFile();
            }
        });

        JButton loadFromFileButton = new JButton("Load from File");
        loadFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataFromTxtFile();
            }
        });

        JButton clearButton = new JButton("Clear Table");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable();
            }
        });

        JButton rulesButton = new JButton("Rules");
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRules();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(saveToFileButton);
        buttonPanel.add(loadFromFileButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(rulesButton);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(buttonPanel, BorderLayout.NORTH);
        mainFrame.add(scrollPane, BorderLayout.CENTER);
    }

    private static void displayDataInNewWindow() {
        JFrame displayFrame = new JFrame("Exported Lua Code for ORMS");
        displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea(20, 60);
        textArea.setEditable(false);

        textArea.append(CodeGenerator.generateTracks(data));

        JScrollPane scrollPane = new JScrollPane(textArea);

        displayFrame.add(scrollPane);
        displayFrame.pack();
        displayFrame.setLocationRelativeTo(null); // Center the frame on the screen
        displayFrame.setVisible(true);
    }

    private void saveDataToTxtFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(mainFrame);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                FileWriter writer = new FileWriter(file);

                int numRows = table.getRowCount();
                int numColumns = table.getColumnCount();

                for (int row = 0; row < numRows; row++) {
                    for (int column = 0; column < numColumns; column++) {
                        String cellValue = String.valueOf(table.getValueAt(row, column));
                        writer.write(cellValue);
                        if (column < numColumns - 1) {
                            writer.write("\t"); // Separate values with tab
                        }
                    }
                    writer.write("\n"); // Newline for each row
                }

                writer.close();
                JOptionPane.showMessageDialog(mainFrame, "Data saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadDataFromTxtFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(mainFrame);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                List<String[]> loadedData = new ArrayList<>();

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] rowData = line.split("\t");
                    loadedData.add(rowData);
                }

                reader.close();

                // Populate the table with loaded data
                int numRows = loadedData.size();
                int numColumns = loadedData.get(0).length;

                for (int row = 0; row < numRows; row++) {
                    for (int column = 0; column < numColumns; column++) {
                        String cellValue = loadedData.get(row)[column];
                        if (cellValue.equals("null")) {
                            table.setValueAt("", row, column);
                        } else {
                            table.setValueAt(cellValue, row, column);
                        }
                    }
                }

                JOptionPane.showMessageDialog(mainFrame, "Data loaded from " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void displayRules() {
        JFrame displayFrame = new JFrame("Rules to follow for optimal operation");
        displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea(20, 60);
        textArea.setEditable(false);

        textArea.append("\n" +
                "Tracks, Switches and Signals Cheatsheet\n" +
                "\n" +
                "Tracks, Switches: ║ | ═ ╔ ╗ ╚ ╝\n" +
                "\n" +
                "Signals: < > V ^\n" +
                "\n" +
                "Now the Rules: \n" +
                "\n" +
                "Signals Creation Rule:\n" +
                "1. Direction of the train travel, 2. space + N + space (Signal in Czech is Návěstidlo so N), 3. Name of the Signal\n" +
                "Example: < N Si1, will show up as Signal with name Si1\n"+
                "\n" +
                "Switches Creation Rule:\n" +
                "1. Base position, 2. space + V + space (Switch in Czech is Vyhybka so V), 3. Switched position, 4. Name of the Switch\n"+
                "Example: ╝ V ═ Vy2, will show up as clickable Switch with name Si1, base position ╝ and switched position ═\n"+
                "\n" +
                "Thats it, enjoy. PS FD is the last collum" +
                "\n" +
                "Petsox"
        );

        JScrollPane scrollPane = new JScrollPane(textArea);

        displayFrame.add(scrollPane);
        displayFrame.pack();
        displayFrame.setLocationRelativeTo(null); // Center the frame on the screen
        displayFrame.setVisible(true);
    }

    private static void saveDataTo2DArray() {
        int numRows = table.getRowCount();
        int numColumns = table.getColumnCount();

        data = new String[numRows][numColumns];


        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                data[row][column] = String.valueOf(table.getValueAt(row, column));
            }
        }
    }

    private static void clearTable() {
        int numRows = table.getRowCount();
        int numColumns = table.getColumnCount();

        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                table.setValueAt("", row, column); // Clear cell content
            }
        }
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ORMS Layout Generator v1.5 by Petsox");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ORMS_Generator generator = new ORMS_Generator();
            frame.add(generator.mainFrame);

            frame.pack();
            frame.setLocationRelativeTo(null); // Center the frame on the screen
            frame.setVisible(true);
        });
    }
}
