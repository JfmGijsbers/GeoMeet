/*
    Modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

public class ConnectHost {
    final String DB_HOST;
    final String DB_USERNAME;
    final String DB_NAME;
    final String DB_PASSWORD;
    final String FILE_URL;
    PrintStream ps;

    public ConnectHost(String fileURL, String dbhost, String dbuser, String dbpass, String dbname) {
        this.DB_USERNAME = dbuser;
        this.DB_PASSWORD = dbpass;
        this.DB_NAME = dbname;
        this.DB_HOST = dbhost;
        this.FILE_URL = fileURL;
    }

    /**
     * Connects to the server with the given data and returns the data as a BufferedReader
     * */
    public BufferedReader getReader (String stmt, String queryType) throws IOException {
        URL url = new URL(this.FILE_URL);
        URLConnection con = url.openConnection();
        con.setDoOutput(true);
        ps = new PrintStream(con.getOutputStream());
        ps.print("DB_HOST=" + this.DB_HOST);
        ps.print("&DB_USERNAME=" + this.DB_USERNAME);
        ps.print("&DB_PASSWORD=" + this.DB_PASSWORD);
        ps.print("&DB_NAME=" + this.DB_NAME);
        ps.print("&stmt=" + stmt);
        ps.print("&SQL=" + queryType);
        return new BufferedReader(new InputStreamReader(con.getInputStream()));
    }

    /**
     * Closes the local printStream
     * */
    public void close() {
        ps.close();
    }
}
