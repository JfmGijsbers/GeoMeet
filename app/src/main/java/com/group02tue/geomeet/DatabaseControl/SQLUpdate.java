/*
    Modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;

public class SQLUpdate {
    private final ConnectHost con;

    public SQLUpdate(ConnectHost con) {
        this.con  = con;
    }

    /**
     * Executes the query and returns the number of line affected in the database
     * */
    public final int execute(String query) throws SQLUpdateException {
        String line;
        StringBuilder output = new StringBuilder("0");

        if (!query.substring(0, query.indexOf(" ")).equalsIgnoreCase("select")) { // is not a fetch query
            try {
                BufferedReader in = con.getReader(query, "update");
                while ((line = in.readLine()) != null) {
                    output.append(line);
                }
                con.close();
            } catch (MalformedURLException e) {
                throw new SQLUpdateException("Error in SQLUpdate(), MalformedURLException occured: \n" +
                        e.getMessage());
            } catch (IOException e) {
                throw new SQLUpdateException("Error in SQLUpdate(), IOException occured: \n" +
                        e.getMessage());
            }
        }

        try {
            return Integer.parseInt(output.toString());
        } catch (Exception e) {
            throw new SQLUpdateException("Error in SQLUpdate(), Exception occured: \n" +
                    e.getMessage());
        }
    }
}