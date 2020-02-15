package com.group02tue.geomeet.DatabaseControll;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Table {
    //WORK IN PROGRESS

    private List<Map<Integer, String>> values;
    private List<String> columnNames;

    Table(String ... columnNames) {
        this.columnNames = Arrays.asList(columnNames);
    }

    void enterData() {

    }
}
