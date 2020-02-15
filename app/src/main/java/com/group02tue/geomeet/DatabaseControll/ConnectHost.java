/*
    Slightly modified library from rohit7209
    Source: https://github.com/rohit7209/AccessRemoteMySQLDB
 */

package com.group02tue.geomeet.DatabaseControll;

class ConnectHost {
    final String DB_HOST;
    final String DB_USERNAME;
    final String DB_NAME;
    final String DB_PASSWORD;
    final String FILE_URL;

    ConnectHost(String fileURL, String dbhost, String dbuser, String dbpass, String dbname) {
        this.DB_USERNAME = dbuser;
        this.DB_PASSWORD = dbpass;
        this.DB_NAME = dbname;
        this.DB_HOST = dbhost;
        this.FILE_URL = fileURL;
    }
}
