package gui.v2.simpleUi.uiDecoration;

public interface UiDecoratable {

	/**
	 * @param decorator
	 * @return true if the decorator could be assigned to all children and their
	 *         sub-children
	 */
	public boolean assignNewDecorator(UiDecorator decorator);

}
