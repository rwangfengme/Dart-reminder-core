package dartmouth.edu.dartreminder.backend.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Datastore {

	private static final Logger mLogger = Logger
			.getLogger(Datastore.class.getName());
	private static final DatastoreService mDatastore = DatastoreServiceFactory
			.getDatastoreService();

	private static Key getKey() {
		return KeyFactory.createKey(UserAccount.DATASTORE_PARENT_ENTITY_NAME,
				UserAccount.DATASTORE_PARENT_KEY_NAME);
	}


/*    ---------------------------------------------------------------------------------------------
                                        User Account Section
      ---------------------------------------------------------------------------------------------*/
    //add elements into data store
	public static int addUser(UserAccount userAccount) {
		if (getUserAccountByUserName(userAccount.getUsername(), null) != null) {
			mLogger.log(Level.INFO, "contact exists");
			return 1;
		}

		Key parentKey = getKey();

		Entity entity = new Entity(UserAccount.USER_ENTRY_ENTITY_NAME, userAccount.getUsername(),
				parentKey);
		entity.setProperty(UserAccount.FIELD_NAME_USER_NAME, userAccount.getUsername());
		entity.setProperty(UserAccount.FIELD_NAME_PASSWORD, userAccount.getPassword());
		mDatastore.put(entity);

		return 0;
	}

    //query elements in the data store
	public static ArrayList<UserAccount> queryAll() {
		ArrayList<UserAccount> resultList = new ArrayList<UserAccount>();

        Query query = new Query(UserAccount.USER_ENTRY_ENTITY_NAME);
        // get every record from datastore, no filter
        query.setFilter(null);
        // set query's ancestor to get strong consistency
        query.setAncestor(getKey());

        PreparedQuery pq = mDatastore.prepare(query);

        for (Entity entity : pq.asIterable()) {
            UserAccount userAccount = getUserAccountFromEntity(entity);
            if (userAccount != null) {
                resultList.add(userAccount);
            }
        }

		return resultList;
	}

    public static int userLogin(String userName, String pwd){
        if (userName == null || userName == "" || pwd == null || userName == ""){
            return 1;
        }
        UserAccount userAccount = getUserAccountByUserName(userName, null);
        if (userAccount != null) {
            if (pwd.equals(userAccount.getPassword())){
                return 0;
            }
            return 2;
        }else{
            return 1;
        }
    }

    //get exercise element from data store by id
	public static UserAccount getUserAccountByUserName(String userName, Transaction txn) {
		Entity result = null;
		try {
			result = mDatastore.get(KeyFactory.createKey(getKey(),
					UserAccount.USER_ENTRY_ENTITY_NAME, userName));
		} catch (Exception ex) {

		}

		return getUserAccountFromEntity(result);
    }

    //transform entity into exercise entry
	private static UserAccount getUserAccountFromEntity(Entity entity) {
		if (entity == null) {
			return null;
		}

		return new UserAccount(
                (String) entity.getProperty(UserAccount.FIELD_NAME_USER_NAME),
                (String) entity.getProperty(UserAccount.FIELD_NAME_PASSWORD));
	}

/*    ---------------------------------------------------------------------------------------------
                                        Schedule Section
      ---------------------------------------------------------------------------------------------*/
    //add elements into data store
    public static int addSchedule(Schedule schedule) {
        if (getScheduleById(schedule.getId()) != null) {
            mLogger.log(Level.INFO, "schedule exists");
            return 0;
        }

        Key parentKey = getKey();

        Entity entity = new Entity(Schedule.SCHEDULE_ENTRY_ENTITY_NAME, schedule.getId(),
                parentKey);
        entity.setProperty(Schedule.FIELD_NAME_ID, schedule.getId());
        entity.setProperty(Schedule.FIELD_NAME_TITLE, schedule.getTitle());
        entity.setProperty(Schedule.FIELD_NAME_NOTES, schedule.getNotes());
        entity.setProperty(Schedule.FIELD_NAME_USE_TIME, schedule.getUseTime());
        entity.setProperty(Schedule.FIELD_NAME_TIME, schedule.getTime());
        entity.setProperty(Schedule.FIELD_NAME_LOCATION_NAME, schedule.getLocationName());
        entity.setProperty(Schedule.FIELD_NAME_LAT, schedule.getLat());
        entity.setProperty(Schedule.FIELD_NAME_LNG, schedule.getLng());
        entity.setProperty(Schedule.FIELD_NAME_ARRIVE, schedule.getArrive());
        entity.setProperty(Schedule.FIELD_NAME_RADIUS, schedule.getRadius());
        entity.setProperty(Schedule.FIELD_NAME_PRIORITY, schedule.getPriority());
        entity.setProperty(Schedule.FIELD_NAME_REPEAT, schedule.getRepeat());
        entity.setProperty(Schedule.FIELD_NAME_IS_COMPLETED, schedule.getCompleted());
        entity.setProperty(Schedule.FIELD_NAME_USER_NAME, schedule.getUserName());
        entity.setProperty(Schedule.FIELD_NAME_SENDER, schedule.getSender());

        mDatastore.put(entity);
        return 1;
    }

    //query elements in the data store
    public static ArrayList<Schedule> queryAllSchedule() {
        ArrayList<Schedule> resultList = new ArrayList<Schedule>();

        Query query = new Query(Schedule.SCHEDULE_ENTRY_ENTITY_NAME);
        // get every record from datastore, no filter
        query.setFilter(null);
        // set query's ancestor to get strong consistency
        query.setAncestor(getKey());

        PreparedQuery pq = mDatastore.prepare(query);

        for (Entity entity : pq.asIterable()) {
            Schedule schedule = getScheduleFromEntity(entity);
            if (schedule != null) {
                resultList.add(schedule);
            }
        }

        return resultList;
    }

    //query elements in the data store
    public static ArrayList<Schedule> fetchScheduleByUserName(String userName) {
        ArrayList<Schedule> resultList = new ArrayList<Schedule>();

        Filter filter = new FilterPredicate(Schedule.FIELD_NAME_USER_NAME,
                FilterOperator.EQUAL, userName);

        Query query = new Query(Schedule.SCHEDULE_ENTRY_ENTITY_NAME);
        // get every record from datastore, set user name as filter
        query.setFilter(filter);
        // set query's ancestor to get strong consistency
        query.setAncestor(getKey());


        PreparedQuery pq = mDatastore.prepare(query);

        for (Entity entity : pq.asIterable()) {
            Schedule schedule = getScheduleFromEntity(entity);
            if (schedule != null) {
                resultList.add(schedule);
            }
        }

        return resultList;
    }

//    //query schedule in the data store by id
//    public static ArrayList<Schedule> fetchScheduleById(String id) {
//        ArrayList<Schedule> resultList = new ArrayList<Schedule>();
//
//        Filter filter = new FilterPredicate(Schedule.FIELD_NAME_ID,
//                FilterOperator.EQUAL, id);
//
//        Query query = new Query(Schedule.SCHEDULE_ENTRY_ENTITY_NAME);
//        // get every record from datastore, set id as filter
//        query.setFilter(filter);
//        // set query's ancestor to get strong consistency
//        query.setAncestor(getKey());
//
//
//        PreparedQuery pq = mDatastore.prepare(query);
//
//        for (Entity entity : pq.asIterable()) {
//            Schedule schedule = getScheduleFromEntity(entity);
//            if (schedule != null) {
//                resultList.add(schedule);
//            }
//        }
//
//        return resultList;
//    }

    public static int updateSchedule(Schedule schedule) {
        Entity entity = null;
        try {
            entity = mDatastore.get(KeyFactory.createKey(getKey(),
                    Schedule.SCHEDULE_ENTRY_ENTITY_NAME, schedule.getId()));

            entity.setProperty(Schedule.FIELD_NAME_ID, schedule.getId());
            entity.setProperty(Schedule.FIELD_NAME_TITLE, schedule.getTitle());
            entity.setProperty(Schedule.FIELD_NAME_NOTES, schedule.getNotes());
            entity.setProperty(Schedule.FIELD_NAME_USE_TIME, schedule.getUseTime());
            entity.setProperty(Schedule.FIELD_NAME_TIME, schedule.getTime());
            entity.setProperty(Schedule.FIELD_NAME_LOCATION_NAME, schedule.getLocationName());
            entity.setProperty(Schedule.FIELD_NAME_LAT, schedule.getLat());
            entity.setProperty(Schedule.FIELD_NAME_LNG, schedule.getLng());
            entity.setProperty(Schedule.FIELD_NAME_ARRIVE, schedule.getArrive());
            entity.setProperty(Schedule.FIELD_NAME_RADIUS, schedule.getRadius());
            entity.setProperty(Schedule.FIELD_NAME_PRIORITY, schedule.getPriority());
            entity.setProperty(Schedule.FIELD_NAME_REPEAT, schedule.getRepeat());
            entity.setProperty(Schedule.FIELD_NAME_IS_COMPLETED, schedule.getCompleted());
            entity.setProperty(Schedule.FIELD_NAME_USER_NAME, schedule.getUserName());
            entity.setProperty(Schedule.FIELD_NAME_SENDER, schedule.getSender());

            mDatastore.put(entity);
            return 0;
        } catch (Exception ex) {

        }
        return 1;
    }

    public static boolean deleteSchedule(String id) {
        // you can also use name to get key, then use the key to delete the
        // entity from datastore directly
        // because name is also the entity's key

        // query
        Filter filter = new FilterPredicate(Schedule.FIELD_NAME_ID,
                FilterOperator.EQUAL, id);

        Query query = new Query(Schedule.SCHEDULE_ENTRY_ENTITY_NAME);
        query.setFilter(filter);

        // Use PreparedQuery interface to retrieve results
        PreparedQuery pq = mDatastore.prepare(query);

        Entity result = pq.asSingleEntity();
        boolean ret = false;
        if (result != null) {
            // delete
            mDatastore.delete(result.getKey());
            ret = true;
        }

        return ret;
    }

    //get exercise element from data store by id
    public static Schedule getScheduleById(String id) {
        Entity result = null;
        try {
            result = mDatastore.get(KeyFactory.createKey(getKey(),
                    Schedule.SCHEDULE_ENTRY_ENTITY_NAME, id));
        } catch (Exception ex) {

        }

        return getScheduleFromEntity(result);
    }

    //transform entity into exercise entry
    private static Schedule getScheduleFromEntity(Entity entity) {
        if (entity == null) {
            return null;
        }

        return new Schedule((String) entity.getProperty(Schedule.FIELD_NAME_ID),
                (String) entity.getProperty(Schedule.FIELD_NAME_TITLE),
                (String) entity.getProperty(Schedule.FIELD_NAME_NOTES),
                (Boolean) entity.getProperty(Schedule.FIELD_NAME_USE_TIME),
                (String) entity.getProperty(Schedule.FIELD_NAME_TIME),
                (String) entity.getProperty(Schedule.FIELD_NAME_LOCATION_NAME),
                (Double) entity.getProperty(Schedule.FIELD_NAME_LAT),
                (Double) entity.getProperty(Schedule.FIELD_NAME_LNG),
                (Boolean) entity.getProperty(Schedule.FIELD_NAME_ARRIVE),
                (Double) entity.getProperty(Schedule.FIELD_NAME_RADIUS),
                (String) entity.getProperty(Schedule.FIELD_NAME_PRIORITY),
                (String) entity.getProperty(Schedule.FIELD_NAME_REPEAT),
                (Boolean) entity.getProperty(Schedule.FIELD_NAME_IS_COMPLETED),
                (String) entity.getProperty(Schedule.FIELD_NAME_USER_NAME),
                (String) entity.getProperty(Schedule.FIELD_NAME_SENDER));
    }
}
