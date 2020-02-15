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

public class SQLQuery {
    private final String DB_HOST;
    private final String DB_USERNAME;
    private final String DB_NAME;
    private final String DB_PASSWORD;
    private final String FILE_URL;

    public SQLQuery(ConnectHost con) {
        this.DB_HOST = con.DB_HOST;
        this.DB_USERNAME = con.DB_USERNAME;
        this.DB_NAME = con.DB_NAME;
        this.DB_PASSWORD = con.DB_PASSWORD;
        this.FILE_URL = con.FILE_URL;
    }

    public final QueryResult statement(String stmt) throws SQLQueryException {
        String line;
        StringBuilder res = new StringBuilder();
        if (stmt.substring(0, stmt.indexOf(" ")).equalsIgnoreCase("select")) {
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
                ps.print("&SQL=query");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                while(true) {
                    if ((line = in.readLine()) == null) {
                        ps.close();
                        break;
                    }

                    res.append(line);
                }
            } catch (MalformedURLException var8) {
                var8.printStackTrace();
                throw new SQLQueryException(var8.getMessage());
            } catch (IOException var9) {
                var9.printStackTrace();
                throw new SQLQueryException(var9.getMessage());
            }

            if (res.substring(0, 10).equals("/*error*/:")) {
                throw new SQLQueryException(res.substring(10));
            } else {
                return this.addResult(res.toString());
            }
        } else {
            throw new SQLQueryException("Not a proper query statement");
        }
    }

    private QueryResult addResult(String res) {
        StringBuilder sb = new StringBuilder(res);
        String row = sb.substring(0, sb.indexOf(","));
        String col = sb.substring(sb.indexOf(",") + 1, sb.indexOf("*/*"));
        QueryResult rs = new QueryResult(Integer.parseInt(row));
        sb.delete(0, sb.indexOf("*/*") + 3);

        int i;
        for(i = 0; i < Integer.parseInt(col); ++i) {
            rs.columns.add(sb.substring(0, sb.indexOf("*/*")).toLowerCase());
            sb.delete(0, sb.indexOf("*/*") + 3);
        }

        for(i = 0; i < Integer.parseInt(row); ++i) {
            rs.results.add(sb.substring(0, sb.indexOf("*/*")));
            sb.delete(0, sb.indexOf("*/*") + 3);
        }

        return rs;
    }
}
