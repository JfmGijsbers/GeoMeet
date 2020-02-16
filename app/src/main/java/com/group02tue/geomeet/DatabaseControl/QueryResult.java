/*
    Modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControl;

import java.util.ArrayList;
import java.util.List;

public class QueryResult {
    List<String> results = new ArrayList<>();
    List<String> columns = new ArrayList<>();
    private int CURRENT_ROW = 0;
    private final int ROW_COUNT;

    QueryResult(int row) {
        this.ROW_COUNT = row;
    }

    /**
     * Check if there is another row and increase the current row count
     * */
    public boolean nextFlag() {
        if (CURRENT_ROW < ROW_COUNT) {
            CURRENT_ROW++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set the current row count to 0
     * */
    public void resetFlag() {
        CURRENT_ROW = 0;
    }

    /**
     * Return the value in the given column, in the current row
     * */
    public String getValue(String column) {
        String column_name = column.toLowerCase();
        if (columns.contains(column_name)) {
            int col = columns.indexOf(column_name);
            return this.getValueAt(CURRENT_ROW - 1, col);
        } else {
            return "";
        }
    }

    /**
     * Return the value in the column with the given index, in the current row
     * */
    public String getValue(int column_index) {
        return column_index < this.columns.size() ? this.getValueAt(this.CURRENT_ROW - 1, column_index) : "";
    }

    /**
     * Return the value in the given location
     * Top left = 0,0
     * */
    public String getValueAt(int row, int col) {
        if (row < numRows() && col < numFields()) {
            StringBuilder row_line = new StringBuilder(results.get(row));

            for(int i = 0; i < col; i++) {
                int j = row_line.indexOf(SQLFacade.separatorString);
                row_line.delete(0, j + SQLFacade.separatorString.length());
            }

            String value = row_line.toString();
            if (value.contains(SQLFacade.separatorString)) {
                value = value.substring(0, value.indexOf(SQLFacade.separatorString));
            }

            return value;
        } else {
            return "";
        }
    }

    /**
     * Getter for the number of rows
     * */
    public int numRows() {
        return results.size();
    }

    /**
     * Getter for the number of columns
     * */
    public int numFields() {
        String line = results.get(0);
        String[] splitted = line.split(SQLFacade.separatorRegex);
        return splitted.length;
    }

    /** Empties out the result*/
    public void clear() {
        results.clear();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(String line: results) {
            output.append(line).append("\n");
        }
        return output.toString();
    }
}

