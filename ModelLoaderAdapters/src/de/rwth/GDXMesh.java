package de.rwth;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import worldData.Obj;
import worldData.Visitor;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedAnimation;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;

public class GDXMesh extends MeshComponent {

	private static final String LOGTAG = "GDXShape";
	private Model model;
	private Texture texture;
	private KeyframedAnimation anim;
	private float animTime;

	public GDXMesh(Model model, Texture texture) {
		super(null);
		this.model = model;
		this.texture = texture;

		try {
			anim = (KeyframedAnimation) ((KeyframedModel) model)
					.getAnimations()[0];
		} catch (Exception e) {
		}
	}

	@Override
	public boolean accept(Visitor visitor) {
		return false;
	}

	@Override
	public void draw(GL10 gl) {

		gl.glEnable(GL10.GL_CULL_FACE);

		if (model != null) {
			if (texture != null) {
				gl.glEnable(GL10.GL_TEXTURE_2D);
				// Gdx.gl.glEnable(GL10.GL_BLEND);
				// Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA,
				// GL10.GL_ONE_MINUS_SRC_ALPHA);
				texture.bind();
				model.render();
				gl.glDisable(GL10.GL_TEXTURE_2D);
			} else {
				model.render();
			}
		} else
			Log.e(LOGTAG, "No model object existend");
	}

	@Override
	public synchronized void update(float timeDelta, Obj obj) {
		super.update(timeDelta, obj);
		if (anim != null && graficAnimationActive) {
			animTime += timeDelta;
			if (animTime > anim.totalDuration - anim.frameDuration) {
				animTime = 0;
			}
			((KeyframedModel) model).setAnimation(anim.name, animTime, true);
		}
	}

}
