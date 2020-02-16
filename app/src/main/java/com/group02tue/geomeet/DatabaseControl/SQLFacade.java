package com.group02tue.geomeet.DatabaseControl;


import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

public class SQLFacade {
    private ConnectHost con;

    static final String separatorRegex = "\\|";
    static final String separatorString = "|";
    static final String error_message = "/*error*/";

    public SQLFacade(String fileURL, String host, String user, String pass, String name) {
        con = new ConnectHost(fileURL, host, user, pass, name);	//make connection
    }

    public SQLFacade(ConnectHost con) {
        this.con = con;
    }

    /**
     * checks if the facade is has enough connection info to connect
     * */
    public boolean isConnected() {
        return con != null;
    }

    /**
     * Executes a fetch query on the database and returns it as a QueryResultOld
     * @param queryText: the query to execute.
     * */
    public QueryResult getDataAsQueryResult(String queryText) throws SQLQueryException, ExecutionException,
            InterruptedException {
        if(!isConnected()) {
            throw new SQLQueryException("Error in SQLFacade.getDataAsTable(), facade is not connected");
        }

        DataFetchQueryResultTask task = new DataFetchQueryResultTask(queryText);
        task.execute();
        return task.get();
    }

    /**
     * Executes a fetch query on the database and returns is as a table.
     * @param queryText: the query to execute.
     * */
    public Table getDataAsTable(String queryText) throws SQLQueryException, ExecutionException,
            InterruptedException {
        if(!isConnected()) {
            throw new SQLQueryException("Error in SQLFacade.getDataAsTable(), facade is not connected");
        }

        DataFetchTableTask task = new DataFetchTableTask(queryText);
        task.execute();
        return task.get();
    }

    public int sendData(String queryText) throws SQLUpdateException, ExecutionException,
    InterruptedException{
        if(!isConnected()) {
            throw new SQLQueryException("Error in SQLFacade.getDataAsTable(), facade is not connected");
        }

        SendDataTask task = new SendDataTask(queryText);
        return task.execute().get();
    }


    private class DataFetchTableTask extends AsyncTask<ConnectHost, Void, Table> {
        private String queryText;

        DataFetchTableTask(String query) {
            this.queryText = query;
        }

        protected Table doInBackground(ConnectHost...connectHosts) throws SQLQueryException{
            SQLQuery query=new SQLQuery(con);
            return query.getAsTable(queryText);//execution of query statement
        }
    }

    private class DataFetchQueryResultTask extends AsyncTask<ConnectHost, Void, QueryResult> {
        private String queryText;

        DataFetchQueryResultTask(String query) {
            this.queryText = query;
        }

        protected QueryResult doInBackground(ConnectHost...connectHosts) throws SQLQueryException{
            SQLQuery query=new SQLQuery(con);
            return query.getAsQueryResult(queryText);//execution of query statement
        }
    }

    private class SendDataTask extends AsyncTask<ConnectHost, Void, Integer> {
        private String queryText;

        SendDataTask(String query) {
            this.queryText = query;
        }

        protected Integer doInBackground(ConnectHost...connectHosts) throws SQLQueryException{
            SQLUpdate update = new SQLUpdate(con);
            return update.execute(queryText);//execution of query statement

        }
    }
}
