package com.group02tue.geomeet.DatabaseControll;

import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

public class SQLFacade {
    private ConnectHost con;

    /**
     * Constructor collects the data for the connection
     * */

    public SQLFacade(String fileURL, String host, String user, String pass, String name) {
        con = new ConnectHost(fileURL, host, user, pass, name);	//make connection
    }


    /**
     * Executes a fetch query on the database.
     * @param queryText: the query to execute.
     * @param columns: the columns you wish to inspect
     * */
    public String getData(String queryText, String ... columns) throws ExecutionException, InterruptedException {
        DataFetchTask task = new DataFetchTask(queryText, columns);
        task.execute();
        return task.get();
    }

    /**
     * Updates the database with the given query
     * */
    public void sendData(String queryText) {
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
