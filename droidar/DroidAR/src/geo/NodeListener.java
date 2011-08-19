package geo;

import gl.Color;
import gl.GLCamera;
import gl.GLFactory;
import gl.MeshComponent;
import gl.MeshGroup;
import gl.animations.AnimationColorBounce;
import gl.animations.AnimationColorMorph;
import gl.animations.AnimationPulse;
import gl.animations.AnimationShrink;
import util.EfficientList;
import util.Vec;
import worldData.Obj;
import android.util.Log;

import components.Component;
import components.ProximitySensor;

public interface NodeListener {

	public class DefaultNodeListener implements NodeListener {

		private static final String LOG_TAG = "NodeListener";
		private static final float MIN_DISTANCE = 9;
		private GLCamera camera;
		MeshComponent diamond = GLFactory.getInstance().newDiamond(null);
		private Color normalDiamondColor = Color.blue();
		private Color highlightDiamondColor = Color.red();
		private Color alreadyPassedColor = Color.blackTransparent();

		public DefaultNodeListener(GLCamera glCamera) {
			camera = glCamera;
		}

		@Override
		public void addNodeToGraph(final GeoGraph targetGraph, GeoObj newNode) {
			newNode.setComp(diamond);

			/*
			 * its a geoObj so the diamont will automatically surroundet by a
			 * meshgroup. change the collor of this group:
			 */
			setNormalTransformations(newNode.getComp(MeshComponent.class));

			Log.d(LOG_TAG, "Adding obj to graph with number of nodes="
					+ targetGraph.getMyItems().myLength);
			if (targetGraph.isEmpty()) {
				Log.d(LOG_TAG, "Setting special props for first node.");
				setHighlightTransformations(newNode
						.getComp(MeshComponent.class));

				newNode.setComp(newProxiSensor(targetGraph));
			}
			targetGraph.add(newNode);
		}

		private void setNormalTransformations(MeshComponent m) {
			m.myColor = normalDiamondColor;
		}

		private void setHighlightTransformations(MeshComponent m) {
			if (m != null) {
				m.myColor = highlightDiamondColor;
				// m.addAnimation(new AnimationPulse(3, new Vec(0, 0, 0), new
				// Vec(0,
				// 0, 1), 0.2f));
			}
		}

		private void setPassedTransformationsOn(MeshComponent m) {
			//m.myColor = alreadyPassedColor;
			m.myAnimation = new AnimationShrink(4);
			m.addAnimation(new AnimationColorMorph(2,
					alreadyPassedColor));
			// m.myScale = new Vec(0.3f, 0.3f, 0.3f);
		}

		private Component newProxiSensor(final GeoGraph targetGraph) {
			return new ProximitySensor(camera, MIN_DISTANCE) {

				@Override
				public void onObjectIsCloseToCamera(GLCamera glCamera, Obj obj,
						MeshComponent meshComp, float currentDistance) {
					Log.d(LOG_TAG, "Proxim Sensor executed, close to " + obj);

					Log.d(LOG_TAG, "     meshComp=" + meshComp);
					Log.d(LOG_TAG, "     meshComp.myColor=" + meshComp.myColor);

					try {
						Log.d(LOG_TAG,
								"     ((MeshGroup)meshComp).myMeshes.get(0).myColor="
										+ ((MeshGroup) meshComp).myMeshes
												.get(0).myColor);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					setPassedTransformationsOn(meshComp);
					obj.remove(this);

					setToNextWayPoint(targetGraph, obj);

				}

			};
		}

		private void setToNextWayPoint(GeoGraph graph, Obj obj) {

			EfficientList<GeoObj> followers = graph
					.getFollowingNodesOf((GeoObj) obj);
			if (followers != null) {
				Log.d(LOG_TAG, "Obj has followers number=" + followers.myLength);
				for (int i = 0; i < followers.myLength; i++) {
					GeoObj o = followers.get(i);
					Log.d(LOG_TAG, "Objo=" + o);
					o.setComp(newProxiSensor(graph));
					setHighlightTransformations(o.getComp(MeshComponent.class));
				}
			}

		}

	}

	void addNodeToGraph(GeoGraph graph, GeoObj objectToAdd);

}