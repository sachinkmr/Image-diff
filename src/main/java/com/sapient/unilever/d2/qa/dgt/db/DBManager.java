
package com.sapient.unilever.d2.qa.dgt.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.sapient.unilever.d2.qa.dgt.AppConstants;

/**
 *
 * @author Sachin
 */
public class DBManager implements AutoCloseable {
    private final MongoClient mongo;
    private final MongoDatabase mongoDB;

    public DBManager() {
	String host = AppConstants.PROPERTIES.getProperty("db.host", "10.207.16.9");
	int port = Integer.parseInt(AppConstants.PROPERTIES.getProperty("db.port", "27017"));
	String db = AppConstants.PROPERTIES.getProperty("db.name", "DGT");
	mongo = new MongoClient(host, port);
	mongoDB = mongo.getDatabase(db);
    }

    public MongoDatabase getMongoDB() {
	return mongoDB;
    }

    @Override
    public void close() {
	mongo.close();
    }

}
