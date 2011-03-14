package commands.ui;

import gl.Color;
import gui.MetaInfos;
import listeners.ItemSelectedListener;
import util.Wrapper;
import worldData.HasInfosInterface;

import commands.Command;

public class CommandSetSelected extends Command {

	/**
	 * 0=remove Selection 1=select with given {@link MetaInfos} object 2=select
	 * with defined values
	 */
	private int mode;

	private Wrapper myTarget;
	private MetaInfos myInfo;

	private String myTextPrefix;

	private String myTextPostfix;

	private Color myColor;

	private boolean setTextInfixUppercase;

	private ItemSelectedListener myListener;

	/**
	 * @param target
	 * @param info
	 *            if null the selection is removed
	 * @param unselect
	 *            if true the selection is removed
	 */
	public CommandSetSelected(Wrapper target, MetaInfos info, boolean unselect) {
		mode = 0;
		myTarget = target;
		if (info != null && !unselect) {
			mode = 1;
			myInfo = info;
		}
	}

	/**
	 * @param target
	 * @param listener
	 *            if null the selection is removed
	 */
	public CommandSetSelected(Wrapper target, ItemSelectedListener listener) {
		mode = 0;
		myTarget = target;
		if (listener != null) {
			mode = 3;
			myListener = listener;
		}
	}

	public CommandSetSelected(Wrapper target, String textPrefix,
			String textPostfix, Color color, boolean textInfixUppercase) {
		mode = 2;
		myTarget = target;
		myTextPrefix = textPrefix;
		myTextPostfix = textPostfix;
		myColor = color;
		setTextInfixUppercase = textInfixUppercase;
	}

	@Override
	public boolean execute() {
		if (myTarget == null)
			return false;
		if (myTarget.getObject() instanceof HasInfosInterface) {

			MetaInfos info = ((HasInfosInterface) myTarget.getObject())
					.getInfoObject();

			if (mode == 0)
				info.setDeselected();
			if (mode == 1)
				info.setSelected(myInfo);
			if (mode == 2)
				info.setSelected(myTextPrefix, myTextPostfix, myColor,
						setTextInfixUppercase);
			if (mode == 3)
				return info.setSelected(myListener);
			return true;
		}
		return false;
	}

}
