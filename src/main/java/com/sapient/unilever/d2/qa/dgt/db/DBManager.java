
package com.sapient.unilever.d2.qa.dgt.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author Sachin
 */
public class DBManager implements AutoCloseable {
	private final MongoClient mongo;
	private final MongoDatabase mongoDB;

	public DBManager() {
		String host = "localhost";
		int port = 27017;
		String db = "DGT";
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
