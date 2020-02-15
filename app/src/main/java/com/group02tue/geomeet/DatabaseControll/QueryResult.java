/*
    Slightly modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControll;

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

    public final boolean nextFlag() {
        if (this.CURRENT_ROW < this.ROW_COUNT) {
            ++this.CURRENT_ROW;
            return true;
        } else {
            return false;
        }
    }

    public final void resetFlag() {
        this.CURRENT_ROW = 0;
    }

    public final String getValue(String column) {
        String column_name = column.toLowerCase();
        if (this.columns.contains(column_name)) {
            int col = this.columns.indexOf(column_name);
            return this.getValueAt(this.CURRENT_ROW - 1, col);
        } else {
            return "";
        }
    }

    public final String getValue(int column_index) {
        return column_index < this.columns.size() ? this.getValueAt(this.CURRENT_ROW - 1, column_index) : "";
    }

    public final String getValueAt(int row, int col) {
        if (row < this.numRows() && col < this.numFields()) {
            StringBuilder row_line = new StringBuilder((String)this.results.get(row));

            for(int i = 0; i < col; ++i) {
                row_line.delete(0, row_line.indexOf("/*/") + 3);
            }

            String value = row_line.toString();
            if (value.contains("/*/")) {
                value = value.substring(0, value.indexOf("/*/"));
            }

            return value;
        } else {
            return "";
        }
    }

    public final int numRows() {
        return this.results.size();
    }

    public final int numFields() {
        String line = (String)this.results.get(0);
        boolean i = true;
        int j = 1;

        while(i) {
            if (line.contains("/*/")) {
                ++j;
                line = line.substring(line.indexOf("/*/") + 3);
            } else {
                i = false;
            }
        }

        return j;
    }

    public final void clear() {
        this.results.clear();
    }
}

