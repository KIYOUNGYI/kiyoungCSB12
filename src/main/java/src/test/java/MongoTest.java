package src.test.java;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class MongoTest {
	public static void main(String[] args) throws UnknownHostException {
	
//		DBObject doc = createDBObject(user);
		
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("dcloudmongodb1");
		
		DBCollection col = db.getCollection("dummy");
		
		DBObject dbObject = col.findOne();
		System.out.println(dbObject.toString());
		
		
		
		//update example
	
		
		//close resources
		mongo.close();
	}
}
