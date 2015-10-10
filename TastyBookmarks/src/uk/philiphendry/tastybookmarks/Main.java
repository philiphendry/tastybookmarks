package uk.philiphendry.tastybookmarks;

import uk.philiphendry.storage.Preferences;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstancestate) {
		super.onCreate(savedInstancestate);
				
    	// If there are login details then bypass the login screen.
    	Preferences preferences = new Preferences(this);
    	String username = preferences.getUsername();
		if (username == null || username == "") {
			startActivity(new Intent(this, Login_.class));
		}
		else
		{
			startActivity(new Intent(this, ViewTags_.class));
		}

	}
	
}
