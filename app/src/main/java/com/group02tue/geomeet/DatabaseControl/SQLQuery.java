/*
    Modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLQuery {
    private ConnectHost con;

    public SQLQuery(ConnectHost con) {
        this.con = con;
    }

    /**
     * Runs the given query on the connection and returns the data as as query
     * */
    public QueryResult getAsQueryResult(String query) throws SQLQueryException {
        Result result = getData(query);
        return createQueryResult(result.numRows, result.numColumns, result.columnNames, result.rows);
    }

    /**
     * Runs the given query on the connection and returns the data as as query
     * */
    public Table getAsTable(String query) throws SQLQueryException {
        Result result = getData(query);
        return createTable(result.numRows, result.numColumns, result.columnNames, result.rows);
    }

    /**
     * Runs the given query and returns usefull data in a Result object
     * */
    private Result getData(String query) throws SQLQueryException {
        String line;
        if (query.substring(0, query.indexOf(" ")).equalsIgnoreCase("select")) {// the query is a fetch
            try {
                BufferedReader in = con.getReader(query, "query");

                int numRows;
                int numColumns;
                String columns;
                List<String> rows = new ArrayList<>();

                String firstLine = in.readLine();
                if(firstLine.contains(SQLFacade.error_message)) {
                    throw new SQLQueryException(firstLine);
                } else {
                    numRows = Integer.parseInt(firstLine);
                }
                numColumns = Integer.parseInt(in.readLine());
                columns = in.readLine();

                String[] columnNames = columns.split(SQLFacade.separatorRegex);

                boolean continueReading = true;
                while(continueReading) {
                    if ((line = in.readLine()) == null) {
                        con.close();
                        continueReading = false;
                    } else {
                        rows.add(line);
                    }

                }

                return new Result(numRows,numColumns,rows,columnNames);
            } catch (MalformedURLException e) {
                throw new SQLQueryException("Error in SQLQuery(), MalformedURLException occured: \n" +
                        e.getMessage());
            } catch (IOException e) {
                throw new SQLQueryException("Error in SQLQuery(), IOException occured: \n" +
                        e.getMessage());
            }
        } else {
            throw new SQLQueryException("Error in SQLQuery(), Not a proper fetch query statement: \n" +
                    query);
        }
    }

    /**
     * Construct a QueryResultOld from the given data
     * */
    private QueryResult createQueryResult(int numRow, int numCol, String[] columnNames, List<String> rows) {
        QueryResult output = new QueryResult(numRow);
        output.columns.addAll(Arrays.asList(columnNames));
        output.results.addAll(rows);
        return output;
    }

    /**
     * Construct a Table from the given data
     * */
    private Table createTable(int numRow, int numCol, String[] columnNames, List<String> rows) {
        Table output = new Table(numRow, numCol,columnNames);
        for(String row: rows) {
            String[] items = row.split(SQLFacade.separatorRegex);
            output.addRow(items);
        }
        return output;
    }

    /**
     * Recordtype of data collect form the server
     * */
    private class Result {
        int numRows;
        int numColumns;
        List<String> rows;
        String[] columnNames;

        public Result(int numRows, int numColumns, List<String> rows, String[] columnNames) {
            this.numRows = numRows;
            this.numColumns = numColumns;
            this.rows = rows;
            this.columnNames = columnNames;
        }
    }
}
