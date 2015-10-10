package uk.philiphendry.tastybookmarks;

import uk.philiphendry.storage.Preferences;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.login)
public class Login extends Activity {
    @ViewById EditText usernameText;
    @ViewById EditText passwordText;
    @ViewById Button loginButton;
      
    @Click public void loginButton(View v) {
    	Preferences preferences = new Preferences(this);
    	preferences.setUsername(this.usernameText.getText().toString());
    	preferences.setPassword(this.passwordText.getText().toString());
    	startActivity(new Intent(this, ViewTags_.class));
	}    
}