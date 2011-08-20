package gamelogic;

import gui.simpleUI.EditItem;
import gui.simpleUI.ModifierGroup;

import java.util.Arrays;

import util.EfficientList;
import android.util.Log;

import components.Component;

public abstract class GameParticipant implements Component, EditItem {

	private static final String LOG_TAG = "GameParticipant";
	private StatList myStatList;
	private ActionList myActionList;
	private String myType;
	private String myName;
	private int myIconId;

	public GameParticipant(String type, String participantName, int iconId) {
		myType = type;
		myName = participantName;
		myIconId = iconId;
	}

	public String getName() {
		return myName;
	}

	public String getType() {
		return myType;
	}

	public int getIconId() {
		return myIconId;
	}

	public StatList getStatList() {
		if (myStatList == null)
			myStatList = new StatList();
		return myStatList;
	}

	public float getStatValue(String statName) {
		if (myStatList == null) {
			Log.e(LOG_TAG, "Tryed to get " + statName
					+ " from emplty statList (was null)");
			return Float.NaN;
		}
		Stat s = myStatList.get(statName);
		if (s == null) {
			Log.e(LOG_TAG, "Stat " + statName
					+ " could not be found! Returning Float.NaN");
			return Float.NaN;
		}
		return s.getValue();
	}

	public ActionList getActionList() {
		if (myActionList == null)
			myActionList = new ActionList();
		return myActionList;
	}

	public ActionFeedback doAction(String actionName, GameParticipant target) {
		if (actionName == null)
			return null;
		if (target == null)
			return null;
		GameAction a = getActionList().get(actionName);
		if (a != null) {
			ActionFeedback feedback = a.doAction(this, target);
			FeedbackReports.getInstance().addFeedback(feedback);
			return feedback;
		}
		return null;
	}

	public void generateEditGUI(ModifierGroup s) {
		if (myStatList != null) {
			EfficientList<Stat> statList = myStatList.getNodes();
			int l = myStatList.getNodes().myLength;
			for (int i = 0; i < l; i++) {
				Stat o = statList.get(i);
				o.generateEditGUI(s);
				if (o.getMyBoosterList() != null)
					o.getMyBoosterList().generateViewGUI(s);
			}
		}
		if (myActionList != null)
			myActionList.generateEditGUI(s);
	}

	public void generateViewGUI(ModifierGroup s) {
		if (myStatList != null)
			myStatList.generateViewGUI(s);
		if (myActionList != null)
			myActionList.generateViewGUI(s);
	}

	public boolean addStat(Stat stat) {
		return getStatList().add(stat);
	}

	public boolean addAction(GameAction attackAction) {
		return getActionList().add(attackAction);
	}

	@Override
	public void customizeScreen(ModifierGroup group, Object message) {

		if (message instanceof String) {
			String m = (String) message;
			String[] keywords = { "Edit", "edit", "editscreen", "Edit screen",
					"edit mode", "editmode", "Editmode" }; // TODO
			if (Arrays.asList(keywords).contains(m)) {
				generateEditGUI(group);
			}
		} else {
			generateViewGUI(group);
		}

	}

}
