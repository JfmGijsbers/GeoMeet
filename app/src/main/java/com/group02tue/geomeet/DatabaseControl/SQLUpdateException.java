/*
    Modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControl;

public class SQLUpdateException extends RuntimeException {

    public SQLUpdateException(String error) {
        super(error);
    }
}
