package com.group02tue.geomeet.DatabaseControll;

import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

public class SQLFacade {
    private ConnectHost con;
    static private SQLFacade instance;

    /**
     * Not singelton constructor
     * */
    /*
    public SQLFacade(String fileURL, String host, String user, String pass, String name) {
        con = new ConnectHost(fileURL, host, user, pass, name);	//make connection
    }
     */

    /**
     * empty constructor
     * */
    private SQLFacade() {

    }

    /**
     * returns an instance of the facade (Singelton)
     * */
    public static SQLFacade getInstance() {
        if(instance == null) {
            instance = new SQLFacade();
        }
        return instance;
    }

    /**
     *  Sets the connection details
     * */
    public void setConnection(String fileURL, String host, String user, String pass, String name) {
        con = new ConnectHost(fileURL, host, user, pass, name);	//make connection
    }

    /**
     * checks if the facade is has enough connection info to connect
     * */
    public boolean isConnected() {
        return con != null;
    }

    /**
     * Executes a fetch query on the database.
     * @param queryText: the query to execute.
     * @param columns: the columns you wish to inspect
     * @pre: isConnected()
     * */
    public String getData(String queryText, String ... columns) throws ExecutionException, InterruptedException {
        /*
        StringBuilder result = new StringBuilder();
        SQLQuery query=new SQLQuery(con);
        try{
            QueryResult qr=query.statement(queryText);		//execution of query statement
            while(qr.nextFlag()){						//setting flag to next row till next row exists
                for (String columnName: columns) {
                    result.append(qr.getValue(columnName)).append(", ");
                }
                result.append("\n");
                //System.out.print(qr.getValue("file_name")+", ");		//printing column_1 data of the row where flag is set
                //System.out.println(qr.getValue("code")+", ");			//printing column_2 data of the row where flag is set
                //System.out.print(qr.getValue("column_3")+", ");			//printing column_3 data of the row where flag is set
                //System.out.print(qr.getValue("column_4"));			//printing column_4 data of the row where flag is set
            }
        }catch(SQLQueryException e){						//catch exception if occurred
            System.out.println(e.getMessage());					//print exception message
        }
        return result.toString();
         */
        if(!isConnected()) {
            throw new IllegalStateException("Error in SQLFacade.getData(), !isConnected()");
        }
        DataFetchTask task = new DataFetchTask(queryText, columns);
        task.execute();
        return task.get();
    }

    /**
     * Updates the database with the given query
     * @Pre: isConnected()
     * */
    public void sendData(String queryText) {
        if(!isConnected()) {
            throw new IllegalStateException("Error in SQLFacade.getData(), !isConnected()");
        }
        SendDataTask task = new SendDataTask(queryText);
        task.execute();
    }

    /**
     * Fetches data from the database in the background
     * */
    //TODO: make it static as it recomends?
    private class DataFetchTask extends AsyncTask<ConnectHost, Void, String> {
        private String queryText;
        private String[] columnNames;

        DataFetchTask(String query, String...columns) {
            this.queryText = query;
            this.columnNames = columns;
        }

        protected String doInBackground(ConnectHost...connectHosts) {
            StringBuilder result = new StringBuilder();
            SQLQuery query=new SQLQuery(con);
            try{
                QueryResult qr=query.statement(queryText);		//execution of query statement
                while(qr.nextFlag()){						//setting flag to next row till next row exists
                    for (String columnName: columnNames) {
                        result.append(qr.getValue(columnName)).append(", ");
                    }
                    result.append("\n");
                    //System.out.print(qr.getValue("file_name")+", ");		//printing column_1 data of the row where flag is set
                    //System.out.println(qr.getValue("code")+", ");			//printing column_2 data of the row where flag is set
                    //System.out.print(qr.getValue("column_3")+", ");			//printing column_3 data of the row where flag is set
                    //System.out.print(qr.getValue("column_4"));			//printing column_4 data of the row where flag is set
                }
            }catch(SQLQueryException e){						//catch exception if occurred
                //TODO: how to handle this?
                System.out.println(e.getMessage());					//print exception message
            }
            return result.toString();
        }
    }

    private class SendDataTask extends AsyncTask<ConnectHost, Void, Void> {
        private String queryText;

        SendDataTask(String query) {
            this.queryText = query;
        }

        protected Void doInBackground(ConnectHost...connectHosts) {
            SQLUpdate update=new SQLUpdate(con);
            try{
                int rows=update.statement(queryText);	//execution of update statement
                System.out.println(rows+" no. of rows affected");								//printing no. of affected rows
            }catch(SQLUpdateException e){											//catch exception if occurred
                System.out.println("Error occured");
                System.out.println(e.getMessage());										//print exception message
            }
            return null;
        }
    }
}
