/*
    Slightly modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControll;

public class SQLUpdateException extends Exception {
    private String ERROR;

    public SQLUpdateException(String error) {
        this.ERROR = error;
    }

    public String getMessage() {
        return this.ERROR;
    }
}
