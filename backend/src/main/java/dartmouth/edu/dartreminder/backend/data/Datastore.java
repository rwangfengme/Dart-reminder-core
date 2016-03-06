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
	public static boolean addUser(UserAccount userAccount) {
		if (getUserAccountByUserName(userAccount.getUsername(), null) != null) {
			mLogger.log(Level.INFO, "contact exists");
			return false;
		}

		Key parentKey = getKey();

		Entity entity = new Entity(UserAccount.USER_ENTRY_ENTITY_NAME, userAccount.getUsername(),
				parentKey);
		entity.setProperty(UserAccount.FIELD_NAME_USER_NAME, userAccount.getUsername());
		entity.setProperty(UserAccount.FIELD_NAME_PASSWORD, userAccount.getPassword());
		mDatastore.put(entity);

		return true;
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

    public static boolean userLogin(String userName, String pwd){
        if (userName == null || userName == "" || pwd == null || userName == ""){
            return false;
        }
        UserAccount userAccount = getUserAccountByUserName(userName, null);
        if (userAccount != null) {
            if (pwd.equals(userAccount.getPassword())){
                return true;
            }
        }

        return false;
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
}
