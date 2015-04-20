
package demo;

import com.mongodb.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;
public class ModeratorService {
    private AtomicLong counter = new AtomicLong();
    //String textUri = "mongodb://root:test@ds033047.mongolab.com:33047/cmpe273db";
    String textUri = "mongodb://root:root@ds061701.mongolab.com:61701/cmpe273lab";
    MongoClientURI uri = new MongoClientURI(textUri);
    MongoClient m = null;
    DB db = null;
    public static DBCollection collection = null;
    DBCursor cursor = null;

    public ModeratorService() {
        try {
            m = new MongoClient(uri);
            db = m.getDB("cmpe273lab");
            collection = db.getCollection("moderators");
        }
        catch(java.net.UnknownHostException e) {
            System.out.println("Custom Message "+e.getMessage());
        }
    }

    public Greeting getModerator(int id) {
        DBObject query = new BasicDBObject("id", id);
        DBObject dbObj = collection.findOne(query);
        if(dbObj != null) {
            return new Greeting(Integer.parseInt(dbObj.get("id").toString()),dbObj.get("name").toString(),
                    dbObj.get("email").toString(),dbObj.get("password").toString(),dbObj.get("created_at").toString());
        }
        return null;
    }

    public Greeting createModerator(String name, String email, String password) {
        cursor = collection.find();
        try {
            while(cursor.hasNext()) {
                DBObject myObj=cursor.next();
                if(!cursor.hasNext()) {
                    counter.set(Long.parseLong(myObj.get("id").toString()));
                }
            }
        } finally {
            cursor.close();
        }
        long tempId=counter.incrementAndGet();
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'dd:HH:mm.sss'Z'");
        df.setTimeZone(tz);
        String myLocalTime = df.format(new Date());
        Greeting myModerator=new Greeting((int)tempId, name, email, password, myLocalTime);
        BasicDBObject document = new BasicDBObject();
        document.append("id", (int)tempId);
        document.append("name", name);
        document.append("email", email);
        document.append("password", password);
        document.append("created_at", myLocalTime);
        collection.insert(document);
        return myModerator;
    }

    public Greeting updateModerator(int id, String name, String email, String password) {
        DBObject query = new BasicDBObject("id", id);
        DBObject dbObj = collection.findOne(query);
        if(dbObj != null) {
            Greeting myModerator=new Greeting(Integer.parseInt(dbObj.get("id").toString()),dbObj.get("name").toString(),
                    dbObj.get("email").toString(),dbObj.get("password").toString(),dbObj.get("created_at").toString());
            BasicDBObject updateQuery = new BasicDBObject();
            BasicDBObject searchQuery = new BasicDBObject();

            if(!(name.equalsIgnoreCase("*"))) {
                myModerator.setName(name);
                searchQuery.append("name", name);
            }
            if(!(email.equalsIgnoreCase("*"))) {
                myModerator.setEmail(email);
                searchQuery.append("email", email);
            }
            if(!(password.equalsIgnoreCase("*"))) {
                myModerator.setPassword(password);
                searchQuery.append("password", password);
            }
            updateQuery.append("$set",searchQuery);
            collection.update(query, updateQuery);
            return myModerator;
        }
        return null;
    }

    public int checkIfId(int id) {
        DBObject query = new BasicDBObject("id", id);
        DBObject dbObj = collection.findOne(query);
        if(dbObj != null) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

