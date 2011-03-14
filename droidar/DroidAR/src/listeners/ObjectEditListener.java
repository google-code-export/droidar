package listeners;

import util.Wrapper;

public interface ObjectEditListener {

	/**
	 * @param wrapper
	 *            the wrapper which contains the object that should be changed
	 * @param passedObject
	 *            an command might pass an useful object but normally this will
	 *            be null
	 * @return true if the object could be modified correctly!
	 */
	boolean onChangeWrapperObject(Wrapper wrapper, Object passedObject);

}
