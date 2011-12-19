package gamelogic;

public abstract class GameAction extends GameElement {

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

}
