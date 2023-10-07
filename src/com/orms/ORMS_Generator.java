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

    private static final int versionMajor = 2;
    private static final int versionMinor = 0;

    private static final int getVersionMinorer = 2;

    protected JPanel mainFrame;

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

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(25);
        }

        table.getTableHeader().setReorderingAllowed(false);

        scrollPane.getViewport().setPreferredSize(new Dimension(1200, 800));

        JButton saveButton = new JButton("Exportovat LUa Kód");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataTo2DArray();
                displayDataInNewWindow();
            }
        });

        JButton saveToFileButton = new JButton("Uložit");
        saveToFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataToTxtFile();
            }
        });

        JButton loadFromFileButton = new JButton("Načíst");
        loadFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataFromTxtFile();
            }
        });

        JButton clearButton = new JButton("Vymazat Plochu");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable();
            }
        });

        JButton rulesButton = new JButton("Nápověda");
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
        JFrame displayFrame = new JFrame("Vyexportovaný kód pro ORMS");
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
                        String cellValue;
                        if (table.getValueAt(row, column) == "") {
                            cellValue = "null";
                        } else {
                            cellValue = String.valueOf(table.getValueAt(row, column));
                        }
                        writer.write(cellValue);
                        if (column < numColumns - 1) {
                            writer.write("\t"); // Separate values with tab
                        }
                    }
                    writer.write("\n"); // Newline for each row
                }

                writer.close();
                JOptionPane.showMessageDialog(mainFrame, "Data uložena do " + file.getAbsolutePath());
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

                JOptionPane.showMessageDialog(mainFrame, "Data načtena z " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void displayRules() {
        JFrame displayFrame = new JFrame("Podmíky, které je nutno dodržet pro funkčnost");
        displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea(30, 80);
        textArea.setEditable(false);

        textArea.append("\n" +
                "Koleje a výhybky\n" +
                "\n" +
                "Pro tvorbu kolejí/výhybek používejte jen tyto symboly: ║ | ═ ╔ ╗ ╚ ╝ ╡ ╞ ╧ ╤\n" +
                "\n" +
                "Pro směr návěstidel tyto symboly: < > V ^\n" +
                "\n" +
                "Pravidla: \n" +
                "\n" +
                "Návěstidla:\n" +
                "Příklad: < N Pr5VjKr1\n" +
                "\n" +
                "< označuje směr jízdy vlaku, v tomto případě <-- (vlevo), Pr označuje předvěst. Tudíž se bude jednat o předvěst pro návěstidlo 5VjKr1\n" +
                "\n" +
                "Návěstidlo 5VjKr1 označuje návěstidlo, které má ve hře 5 světel, Vj - Vjezdové / Od - Odjezdové" + "\n" +
                "\n" +
                "Vjezdové: Kr - Krupka (jméno stanice ze které vlak přijede), 1 je číslo koleje, u které návěstidlo stojí\n" +
                "Odjezdové: Kr - Krupka (jméno stanice do které vlak jede), 1 opět číslo koleje\n" +
                "\n" +
                "Znamenaje, že 5VjKr1 je návěstidlo: 5 světelné, vjezdové od stanice Krupka a stojí u koleje čislo 1\n" +
                "\n" +
                "Návěstidla máme 1,4 a 5 světlové" +
                "\n" +
                "\n" +
                "Seřaďovací návěstidla (pro posun): Nemusí se uvádět počet světel a Vj/Od., Sh - Shunt(Posun), Kr značí stanici, ve které se návěst nachází a 1 je čislo návěstidla: < N ShKr1" +
                "\n" +
                "\n" +
                "Výhybky:\n" +
                "Příklad: ╝ V ═ Vy2: Výhybka, na kterou se dá kliknout s výchozí pozicí ╝, V označuje vyhybku, ═ je přehozená pozice\n" +
                "Pak následuje Vy2, což je jméno a číslo výhybky. Musí být stejné jako jméno receiveru pod výhybkou\n" +
                "\n" +
                "Poslední update 2.0: Čeština." +
                "\n" +
                "Přepsáno pro novou verzi ORMS" +
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
            JFrame frame = new JFrame("ORMS Layout Generator v" + versionMajor + "." + versionMinor + "." + getVersionMinorer + " by Petsox");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ORMS_Generator generator = new ORMS_Generator();
            frame.add(generator.mainFrame);

            frame.pack();
            frame.setLocationRelativeTo(null); // Center the frame on the screen
            frame.setVisible(true);
        });
    }
}