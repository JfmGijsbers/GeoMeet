package com.group02tue.geomeet.DatabaseControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Table implements Iterable<Table.Row>{
    private List<Row> rows;
    private List<String> columnNames;
    private final int expNumColumns;
    private final int expNumRows;

    public Table(int numRows, int numColumns, String ... columnNames) {
        expNumRows = numRows;
        expNumColumns = numColumns;
        this.columnNames = Arrays.asList(columnNames);
        rows = new ArrayList<>();
    }

    /**
     *  Basic getter
     * */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * Basic getter
     * */
    public int getNumRows() {
        return rows.size();
    }

    /**
     * Basic getter
     * */
    public int getNumColumns() {
        return columnNames.size();
    }

    /**
     * add a given row to the table
     * */
    public void addRow (List<String> row) {
        rows.add(new Row(row));
    }

    /**
     * Add a given row to the table
     * */
    public void addRow (String[] row) {
        rows.add(new Row(Arrays.asList(row)));
    }

    /**
     * Gets the value at the given location.
     * Top left = 0,0
     * */
    public String getValueAt(int rowNum, int columnNum) {
        return rows.get(rowNum).get(columnNum);
    }

    /**
     * Gets the value at the given location.
     * Top left = 0,0
     * */
    public String getValueAt(int rowNum, String columnName) {
        return rows.get(rowNum).get(columnName);
    }

    /**
     * Checks if the expected number of rows and columns equals the actual number of rows and columns
     * */
    public boolean isTableComplete() {
        return rows.size() == expNumRows && columnNames.size() == expNumColumns;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Table: ").append("\n");

        int columnLength = 0;
        for(String name: columnNames) {
            output.append(name).append(" | ");
            columnLength = columnLength + name.length() + 3;
        }
        output.setLength(output.length() - 3);
        columnLength = columnLength - 3;
        output.append("\n");

        for(int i = 0; i < columnLength; i++) {
            output.append("-");
        }
        output.append("\n");

        for (Row row: rows) {
            for (String item: row.items) {
                output.append(item).append(" | ");
            }
            output.setLength(output.length() - 3);
            output.append("\n");
        }

        return output.toString();
    }

    @Override
    public Iterator<Row> iterator() {
        return rows.iterator();
    }

    public class Row implements Iterable<String>{
        List<String> items;
        Row (List<String> items) {
            this.items = items;
        }

        String get(int index) {
            return items.get(index);
        }

        String get(String columnName) {
            if (columnNames.contains(columnName)) {
                int columnNum = columnNames.indexOf(columnName);
                return get(columnNum);
            } else {
                return "";
            }
        }

        @Override
        public Iterator<String> iterator() {
            return items.iterator();
        }
    }
}

