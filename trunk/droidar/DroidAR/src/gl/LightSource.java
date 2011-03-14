package gl;

import javax.microedition.khronos.opengles.GL10;

/**
 * the current problem with lightning might be that the normals of every shape
 * have to be calculated to enable correct lightning and when working with
 * moving and rotating objects this would cost a lot of time. so at the moment
 * its not fully implemented!
 * 
 * @author Spobo
 * 
 */
public class LightSource {

	// http://iphonedevelopment.blogspot.com/2009/05/opengl-es-from-ground-up-part-4-let.html

	/**
	 * specular light is the white dot on the ball
	 */
	private float[] lightSpecularColor = { 0.7f, 0.7f, 0.7f, 1 };

	/**
	 * diffuse light is the bright part around the white dot on the ball
	 */
	private float[] lightDiffuseColor = { 0.5f, 0.5f, 0.5f, 1 };

	/**
	 * ambient light is the light emitted by everything (has no source, is the
	 * ground level level)
	 * 
	 * the ambientLight field should only be defined once so its static and the
	 * only lightSource which can change the default value for ambient light is
	 * the first one (with glLightId==GL10.GL_LIGHT0)
	 */
	private static float[] ambientLightColor = { 0.1f, 0.1f, 0.1f, 1 };

	// default material values which should be overwritten by each mesh later:
	private float materialAmbient[] = new float[] { 0.6f, 0.6f, 0.6f, 1 };
	private float materialDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1 };

	private float[] position = { 1, 1, 1, 0 };

	private boolean isSpotlight = false;
	private float[] direction = { 0, 0, -1 };
	private float cutoffAngle = 45;

	public void switchOn(GL10 gl, int glLightId) {

		gl.glEnable(glLightId);

		if (glLightId == GL10.GL_LIGHT0) {
			gl.glLightfv(glLightId, GL10.GL_AMBIENT, ambientLightColor, 0);

			// a default material should be defined:
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
					materialAmbient, 0);
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
					materialDiffuse, 0);
		}

		gl.glLightfv(glLightId, GL10.GL_DIFFUSE, lightDiffuseColor, 0);

		gl.glLightfv(glLightId, GL10.GL_SPECULAR, lightSpecularColor, 0);

		gl.glLightfv(glLightId, GL10.GL_POSITION, position, 0);

		if (isSpotlight) {
			gl.glLightfv(glLightId, GL10.GL_SPOT_DIRECTION, GLUtilityClass
					.createAndInitFloatBuffer(direction));
			gl.glLightf(glLightId, GL10.GL_SPOT_CUTOFF, cutoffAngle);
		}

		// use the colors of the meshes and calc
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
	}

}
