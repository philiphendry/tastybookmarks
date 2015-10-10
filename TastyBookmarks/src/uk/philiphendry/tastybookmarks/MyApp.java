package uk.philiphendry.tastybookmarks;

import uk.philiphendry.storage.DataHelper;
import android.content.Context;

public class MyApp extends android.app.Application {

    private static MyApp instance;
    private static DataHelper dataHelper;

    public static Context getContext() {
        return instance;
    }
    
    public static DataHelper getDataHelper() {
    	return dataHelper;
    }

    @Override
    public void onCreate() {
    	super.onCreate();

    	MyApp.instance = this;
    	MyApp.dataHelper = new DataHelper(this);
   }
}