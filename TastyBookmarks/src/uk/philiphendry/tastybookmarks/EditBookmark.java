package uk.philiphendry.tastybookmarks;

import uk.philiphendry.tastybookmarks.R;
import uk.philiphendry.utils.StringUtilities;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@EActivity(R.layout.bookmark)
public class EditBookmark extends Activity {
	
	@Extra(Constants.EXTRA_TITLE) String title;
	@Extra(Constants.EXTRA_URL) String url;
	@Extra(Constants.EXTRA_TAGS) String[] tags;
	@Extra(Constants.EXTRA_DESCRIPTION) String description;
	
	@ViewById EditText titleText;
	@ViewById EditText urlText;
	@ViewById EditText tagsText;
	@ViewById EditText descriptionText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		titleText.setText(title);
		urlText.setText(url);
		tagsText.setText(StringUtilities.arrayToString(tags, ", "));
		descriptionText.setText(description);
	}
	
	@Click public void saveButton(View v) {
		Toast.makeText(getApplicationContext(), "Saved", Constants.TOAST_DURATION).show();
		finish();
	}
	
	@Click public void cancelButton(View v) {
		finish();
	}
}
