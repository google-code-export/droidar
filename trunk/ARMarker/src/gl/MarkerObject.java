package gl;


/**
 * An object which can be registered under a marker ID. If a marker with this ID
 * is found the draw method will be called to draw the object. NOTE! The markers
 * are matched to the following coordinates:
 * 
 * <pre>
 * (-1, 1, 0)------(1, 1, 0)
 *     |                |
 *     |                |
 *     |                |
 *  (-1, -1, 0)-----(1, -1,0)
 * </pre>
 *  
 * When rendering objects this should be kept in mind.
 * 
 * @author Alex
 */
public interface MarkerObject {

	public int getMyId();

	/**
	 * This methode is called by the framework when a marker was detected
	 * @param rotMatrix A big matrix which somewhere contains the linearized rotation/translation matrix for this marker.
	 * @param start The first index of the linearized matrix.
	 * @param end The last index of the linearized matrix.
	 * @param sideAngle The angle at which the marker needs to be rotated for a correct result.
	 *            0 90 180 or 270
	 */
	public void OnMarkerPositionRecognized(float[] rotMatrix, int start, int end,
			int sideAngle);
}
