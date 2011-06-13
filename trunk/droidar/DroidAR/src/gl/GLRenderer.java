package gl;

import gl.textures.TextureManager;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import util.EfficientList;
import util.Vec;
import worldData.Renderable;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.Log;

/**
 * This is the OpenGL renderer used for the {@link CustomGLSurfaceView}
 * 
 * @author Spobo
 * 
 */
public class GLRenderer implements Renderer {

	public static float LENSE_ANGLE = 35.0f; // 25 before, marker recog 39 TODO
	public static float minViewDistance = 0.1f;
	public static float maxViewDistance = 700.0f;
	public static float halfWidth;
	public static float halfHeight;
	public static float height;
	public static float nearHeight;
	public static float aspectRatio;

	/**
	 * lightning wont work yet. the normals of all meshes are required to
	 * illuminate them correctly. this has to be implemented first! (relevance 4
	 * of 10)
	 */
	private static final boolean USE_LIGHTS = false;
	public EfficientList<LightSource> myLights; // =newEfficientList<LightSource>();

	/**
	 * TODO Fog isn't fully supported yet because the color picking mechanism
	 * wont work with fog enabled. fog should be disabled for the picking
	 * frames. this has to be implemented first
	 */
	private static final boolean USE_FOG = false;
	private static final float FOG_END_DISTANCE = 25.0f;
	private static final float FOG_START_DISTANCE = 2.0f;
	private static final FloatBuffer FOG_COLOR = new Color(0, 0, 0, 0)
			.toFloatBuffer();
	private static final boolean FLASH_SCREEN = false;

	/**
	 * TODO change to something more abstract like a GlDrawable interface? would
	 * loose some speed though..
	 */
	private EfficientList<Renderable> elementsToRender = new EfficientList<Renderable>();

	private boolean pauseRenderer;
	private boolean readyToPickPixel;

	public void onDrawFrame(GL10 gl) {

		if (pauseRenderer)
			startPauseLoop();

		if (ObjectPicker.readyToDrawWithColor) {
			readyToPickPixel = true;
		}

		// first check if there are new textures to load into openGL:
		TextureManager.getInstance().updateTextures(gl); // TODO optimize? check
															// boolean
		boolean repeat;
		do {

			// Clears the screen and depth buffer.
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			for (int i = 0; i < elementsToRender.myLength; i++) {
				// Reset the modelview matrix
				gl.glLoadIdentity();
				elementsToRender.get(i).draw(gl);
			}

			repeat = false;
			if (readyToPickPixel) {
				ObjectPicker.getInstance().pickObject(gl);
				readyToPickPixel = false;
				// first time in life i would like to have a goto in Java;)
				if (!FLASH_SCREEN) {
					repeat = true;
				}
			}
		} while (repeat);

	}

	/**
	 * do not kill the rendering thread, instead pause it this way because
	 * otherwise the opengl resources would be released and the thread cant be
	 * resatarted!
	 */
	private void startPauseLoop() {
		Log.d("OpenGL", "Renderer paused");
		while (pauseRenderer) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Log.d("OpenGL", "Renderer woken up");
	}

	/**
	 * If lightning is enables this method will add the light-sources to the
	 * scene.
	 * 
	 * @param gl
	 */
	private void addLights(GL10 gl) {
		gl.glEnable(GL10.GL_LIGHTING);

		ArrayList<Integer> indexList = new ArrayList<Integer>();
		indexList.add(GL10.GL_LIGHT0);
		indexList.add(GL10.GL_LIGHT1);
		indexList.add(GL10.GL_LIGHT2);
		indexList.add(GL10.GL_LIGHT3);
		indexList.add(GL10.GL_LIGHT4);
		indexList.add(GL10.GL_LIGHT5);
		indexList.add(GL10.GL_LIGHT6);
		indexList.add(GL10.GL_LIGHT7);

		for (int i = 0; i < myLights.myLength; i++) {
			if (i > 7) {
				Log.e("OpenGL", "There were to many lights defined! "
						+ "Only 8 lights allowed");
				return;
			}
			myLights.get(i).switchOn(gl, indexList.get(0));
			indexList.remove(0);
		}
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {

		Log.d("Activity", "GLSurfaceView.onSurfaceChanged");

		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);

		/*
		 * Select the projection matrix which transforms the point from view
		 * space to homogeneous clipping space. Clip space is a right-handed
		 * coordinate system (+Z into the screen) contained within a canonical
		 * clipping volume extending from (-1,-1,-1) to (+1,+1,+1):
		 */
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();

		/*
		 * GLU.gluPerspective parameters (see
		 * http://www.zeuscmd.com/tutorials/opengles/12-Perspective.php):
		 * 
		 * fovy - This specifies the field of view. A 90 degree angle means that
		 * you can see everything directly to the left right around to the right
		 * of you. This is not how humans see things. 45 degrees is a good value
		 * to start.
		 * 
		 * aspect - This specifies that aspect ratio that you desire. This is
		 * usually specified as the width divided by the height of the window.
		 * 
		 * zNear and zFar - This specifies the near and far clipping planes as
		 * normal.
		 */
		GLRenderer.halfWidth = width / 2;
		GLRenderer.halfHeight = height / 2;
		GLRenderer.height = height;
		GLRenderer.nearHeight = minViewDistance
				* (float) Math.tan((GLRenderer.LENSE_ANGLE * Vec.deg2rad) / 2);
		GLRenderer.aspectRatio = (float) width / (float) height;
		GLU.gluPerspective(gl, LENSE_ANGLE, aspectRatio, minViewDistance,
				maxViewDistance);
		// TODO what is a good value??

		/*
		 * Select the modelview matrix which transforms a point from model space
		 * to view space, using a right-handed coordinate system with +Y up, +X
		 * to the right, and -Z into the screen:
		 */
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		if (USE_LIGHTS)
			addLights(gl);

		if (USE_FOG)
			addFog(gl);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		Log.d("Activity", "GLSurfaceView.onSurfaceCreated");

		// Set the background color to black (and alpha to 0) ( rgba ).
		gl.glClearColor(0, 0, 0, 0);
		/*
		 * To enable flat shading use gl.glShadeModel(GL10.GL_FLAT); default is
		 * GL_SMOOTH and GL_FLAT renders faces always with the same color,
		 * shading... so its a little cheaper then GL_SMOOTH but the polygons
		 * wont look realistic!
		 */
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_DITHER);

		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		// gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		// Transparancy
		// important: transparent objects have to be drawn last!
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

	}

	private void addFog(GL10 gl) {
		// TODO extract constants
		gl.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
		gl.glFogf(GL10.GL_FOG_START, FOG_START_DISTANCE);
		gl.glFogf(GL10.GL_FOG_END, FOG_END_DISTANCE);
		gl.glHint(GL10.GL_FOG_HINT, GL10.GL_NICEST);
		gl.glFogfv(GL10.GL_FOG_COLOR, FOG_COLOR);
		gl.glEnable(GL10.GL_FOG);

	}

	public void addRenderElement(Renderable elementToRender) {
		elementsToRender.add(elementToRender);
	}

	public boolean removeRenderElement(Renderable elementToRemove) {
		return elementsToRender.remove(elementToRemove);
	}

	public void resume() {
		pauseRenderer(false);
	}

	private synchronized void pauseRenderer(boolean pauseRenderer) {
		this.pauseRenderer = pauseRenderer;
	}

	public void pause() {
		this.pauseRenderer(true);
	}

}
