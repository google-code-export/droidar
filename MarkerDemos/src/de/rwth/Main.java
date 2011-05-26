package de.rwth;

import gl.GLFactory;
import gui.simpleUI.EditItem;
import gui.simpleUI.ModifierGroup;
import gui.simpleUI.SimpleUI;
import gui.simpleUI.modifiers.TextModifier;
import system.ArActivity;
import system.DefaultARSetup;
import worldData.World;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.rwth.setups.CollectItemsSetup;
import de.rwth.setups.DebugSetup;
import de.rwth.setups.PlaceObjectsSetup;

public class Main extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button b = new Button(this);
		b.setText("Load marker-setup");
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArActivity.startWithSetup(Main.this,
						new MultiMarkerSetup());
			}
		});
		setContentView(b);
	}
}