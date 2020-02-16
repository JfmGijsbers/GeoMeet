/*
    Modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControl;

public class SQLQueryException extends RuntimeException {

    public SQLQueryException(String error) {
        super(error);
    }
}