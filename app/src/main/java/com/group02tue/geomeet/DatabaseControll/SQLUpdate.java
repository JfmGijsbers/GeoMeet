/*
    Slightly modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SQLUpdate {
    private final String DB_HOST;
    private final String DB_USERNAME;
    private final String DB_NAME;
    private final String DB_PASSWORD;
    private final String FILE_URL;

    public SQLUpdate(ConnectHost con) {
        this.DB_HOST = con.DB_HOST;
        this.DB_USERNAME = con.DB_USERNAME;
        this.DB_NAME = con.DB_NAME;
        this.DB_PASSWORD = con.DB_PASSWORD;
        this.FILE_URL = con.FILE_URL;
    }

    public final int statement(String stmt) throws SQLUpdateException {
        String line = "";
        String res = "0";
        if (!stmt.substring(0, stmt.indexOf(" ")).equalsIgnoreCase("select")) {
            try {
                URL url = new URL(this.FILE_URL);
                URLConnection con = url.openConnection();
                con.setDoOutput(true);
                PrintStream ps = new PrintStream(con.getOutputStream());
                ps.print("DB_HOST=" + this.DB_HOST);
                ps.print("&DB_USERNAME=" + this.DB_USERNAME);
                ps.print("&DB_PASSWORD=" + this.DB_PASSWORD);
                ps.print("&DB_NAME=" + this.DB_NAME);
                ps.print("&stmt=" + stmt);
                ps.print("&SQL=update");

                for(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); (line = in.readLine()) != null; res = res + line) {
                }

                ps.close();
            } catch (MalformedURLException var9) {
                System.out.println("Error in SQLUpdate.java: MalformedURLException");
                var9.printStackTrace();
                throw new SQLUpdateException(var9.getMessage());
            } catch (IOException var10) {
                System.out.println("Error in SQLUpdate.java: IOException");
                var10.printStackTrace();
                throw new SQLUpdateException(var10.getMessage());
            }
        }

        try {
            return Integer.parseInt(res);
        } catch (Exception var8) {
            System.out.println("Error in SQLUpdate.java: Exception" + res);
            throw new SQLUpdateException(res.substring(11));
        }
    }
}