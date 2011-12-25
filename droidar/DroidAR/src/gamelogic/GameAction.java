package gamelogic;

import util.Log;
import worldData.Updateable;

public abstract class GameAction extends GameElement {

	public static final String COOLDOWN_PROGRESS = "Cooldown Progress";
	public static final String COOLDOWN_TIME = "Cooldown Time";
	private static final String LOG_TAG = "GameAction";
	private StatList myStatList;

	public GameAction(String uniqueName, int iconId) {
		super(uniqueName, iconId);
	}

	/**
	 * @param initiator
	 * @param target
	 *            might be null so always consider that!
	 * 
	 * @return an ActionFeedback object with all information for fine-tuning and
	 *         balancing should be returned. if the method is manually called
	 *         the ActionFeedback should be registered in the FeedBackReports
	 *         singleton
	 */
	public abstract ActionFeedback doAction(GameParticipant initiator,
			GameParticipant target);

	@Override
	public boolean update(float timeDelta, Updateable parent) {
		super.update(timeDelta, parent);
		myStatList.update(timeDelta, this);
		return true;
	}

	public StatList getStatList() {
		if (myStatList == null)
			myStatList = new StatList();
		return myStatList;
	}

	/**
	 * @param statName
	 * @return {@link Float#NaN} if the stat could not be found!
	 */
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

	public boolean setStatValue(String statName, float newStatValue) {
		if (myStatList == null)
			return false;
		Stat s = getStatList().get(statName);
		if (s == null)
			return false;
		s.setValue(newStatValue);
		return true;
	}

	public boolean addStat(Stat stat) {
		return getStatList().add(stat);
	}

}
