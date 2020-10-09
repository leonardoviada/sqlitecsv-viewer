import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.util.ArrayList;



public class MyTable extends JTable {
    public MyTable() {
        super();
    }

    public void loadFromCSV(File file) throws IOException {
        DefaultTableModel model = (DefaultTableModel) this.getModel();

        /* RESET */
        model.setRowCount(0);
        model.setColumnCount(0);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        String[] cols = line.split("[,;]");
        for(String col:cols)
            model.addColumn(col);

        while((line = br.readLine())!=null) {
            model.addRow(line.split("[,;]"));
        }
        br.close();

        this.setModel(model);
    }

    public void loadFromSQL(File file) throws IOException {
        DefaultTableModel model = (DefaultTableModel) this.getModel();

        /* RESET */
        model.setRowCount(0);
        model.setColumnCount(0);

        /* CONNESSIONE */
        Connection conn = null;

        try {
            String url = "jdbc:sqlite:" + file.toString();
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            /* RETRIEVE TABELLE */
            ResultSet rs1 = conn.getMetaData().getTables(null, null, null, null);

            ArrayList<String> tables = new ArrayList<>();
            while(rs1.next()) {
                tables.add(rs1.getString("TABLE_NAME"));
            }

            /* SELEZIONE TABELLA */
            JOptionPane jop = new JOptionPane();
            JComboBox tablesList = new JComboBox(tables.toArray());
            tablesList.setSelectedIndex(4);
            JOptionPane.showConfirmDialog(null, tablesList, null, JOptionPane.OK_CANCEL_OPTION);
            String tableName = tablesList.getSelectedItem().toString();
            System.out.println("TABELLA SELEZIONATA " + tableName);

            /* QUERY */
            String query = "SELECT * FROM " + tableName + ";";
            Statement stmt = conn.createStatement();

            ResultSet rs2 = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs2.getMetaData();

            /* COLONNE */
            int columnCount = rsmd.getColumnCount();
            ArrayList<String> columns = new ArrayList<>();
            for (int i = 1; i < columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                columns.add(columnName);
            }
            for(String col:columns)
                model.addColumn(col);

            /* RIGHE */
            while(rs2.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (String colName:columns) {
                    Object val = rs2.getObject(colName);
                    row.add(val.toString());
                }
                model.addRow(row.toArray());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}



