package gamelogic;

import system.ParentStack;
import worldData.Updateable;
import gui.simpleUI.ModifierGroup;
import gui.simpleUI.modifiers.InfoText;
import gui.simpleUI.modifiers.PlusMinusModifier;
import de.rwth.R;

public class ActionThrowFireball extends GameAction {

	private static int myIconId = R.drawable.elephant64;
	private int myLevel = 1;

	public static final String FIREBALL_ACTION = "Throw fireball";

	public ActionThrowFireball() {
		super(FIREBALL_ACTION, myIconId);
	}

	public ActionThrowFireball(String uniqueName) {
		super(uniqueName, myIconId);
	}

	@Override
	public ActionFeedback doAction(GameParticipant initiator,
			GameParticipant target) {
		ActionFeedback feedback = new ActionFeedback("Fireball");
		if (target == null) {
			feedback.addInfo("Can't attack, no enemy selected!");
			return feedback;
		}

		Stat i = initiator.getStatList().get(Stat.INTELLIGENCE);
		float damage = 0;
		if (i != null)
			damage = myLevel * i.getValue();
		feedback.addInfo("damage", damage);

		float defence = 0;
		Stat f = target.getStatList().get(Stat.FIRE_RESISTANCE);
		if (f != null)
			defence = f.getValue();
		feedback.addInfo("fire resistance of target", defence);

		damage -= defence;
		if (damage < 0)
			damage = 0;
		feedback.addInfo("final damage", damage);
		Stat hp = target.getStatList().get(Stat.HP);
		if (hp != null) {
			float newHp = hp.getValue();
			feedback.addInfo("Target HP before damage", newHp);
			newHp -= damage;
			feedback.addInfo("Target HP after damage", newHp);
			hp.setValue(newHp);
			feedback.setActionCorrectExecuted(true);
		}

		return feedback;
	}

	@Override
	public void generateViewGUI(ModifierGroup s) {
		s.addModifier(new InfoText("Fireball Level", "" + myLevel));
	}

	@Override
	public void generateEditGUI(ModifierGroup s) {
		s.addModifier(new PlusMinusModifier(R.drawable.minuscirclegray,
				R.drawable.pluscirclegray) {

			@Override
			public boolean save(double currentValue) {
				myLevel = (int) currentValue;
				return true;
			}

			@Override
			public double plusEvent(double currentValue) {
				return currentValue + 1;
			}

			@Override
			public double minusEvent(double currentValue) {
				if (currentValue - 1 < myLevel)
					return myLevel;
				return currentValue - 1;
			}

			@Override
			public double load() {
				return myLevel;
			}

			@Override
			public String getVarName() {
				return myName;
			}
		});
	}

}
