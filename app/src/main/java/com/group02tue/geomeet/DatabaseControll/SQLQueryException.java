/*
    Slightly modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControll;

public class SQLQueryException extends Exception {
    private String ERROR = "";

    public SQLQueryException(String error) {
        this.ERROR = error;
    }

    public String getMessage() {
        return this.ERROR;
    }
}