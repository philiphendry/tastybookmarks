package uk.philiphendry.storage;

import uk.philiphendry.tastybookmarks.Constants;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {

	private static final String PREF_USERNAME = "USERNAME";
	private static final String PREF_PASSWORD = "PASSWORD";
	
	private SharedPreferences preferences;
	
	public Preferences(Context context) {
		this.preferences = context.getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
	}
	
	public String getUsername() {
		return this.preferences.getString(PREF_USERNAME, "");
	}
	
	public void setUsername(String username) {
		Editor editor = this.preferences.edit();
		editor.putString(PREF_USERNAME, username);
		editor.commit();
	}
	
	public String getPassword() {
		return this.preferences.getString(PREF_PASSWORD, "");
	}
	
	public void setPassword(String password) {
		Editor editor = this.preferences.edit();
		editor.putString(PREF_PASSWORD, password);
		editor.commit();
	}
	
}
