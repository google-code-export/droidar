package de.rwth.setups;

import de.rwth.R;
import actions.Action;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import gamelogic.ActionFeedback;
import gamelogic.ActionThrowFireball;
import gamelogic.GameAction;
import gamelogic.GameElement;
import gamelogic.GameElementView;
import gamelogic.GameParticipant;
import gamelogic.Stat;
import gl.GLFactory;
import gl.GLRenderer;
import gui.GuiSetup;
import gui.simpleUI.ModifierGroup;
import gui.simpleUI.ModifierInterface;
import system.DefaultARSetup;
import worldData.World;

public class GameDemoSetup extends DefaultARSetup {
	private GameAction e;

	public GameDemoSetup() {
		GameParticipant p = new GameParticipant("Player", "Karlo",
				R.drawable.hippopotamus64);
		p.addStat(new Stat(Stat.INTELLIGENCE, R.drawable.icon, 2));
		e = new ActionThrowFireball("Fireball");
		p.addAction(e);
	}

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory) {

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);
		guiSetup.addViewToTop(e.getNewDefaultView(getActivity()));
	}
}
