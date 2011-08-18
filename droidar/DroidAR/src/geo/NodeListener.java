package geo;

import util.EfficientList;
import worldData.Obj;
import android.util.Log;
import commands.Command;
import components.Component;
import components.ProximitySensor;

import gl.Color;
import gl.GLCamera;
import gl.GLFactory;
import gl.MeshComponent;
import gl.MeshGroup;

public interface NodeListener {

	public class DefaultNodeListener implements NodeListener {

		private static final String LOG_TAG = "NodeListener";
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
			MeshComponent mesh = newWaypointMesh();
			newNode.setComp(mesh);
			Log.d(LOG_TAG, "Adding obj to graph with number of nodes="
					+ targetGraph.getMyItems().myLength);
			if (targetGraph.isEmpty()) {
				Log.d(LOG_TAG, "Setting special props for first node.");
				mesh.myColor = highlightDiamondColor;
				newNode.setComp(newProxiSensor(targetGraph));
			}
			targetGraph.add(newNode);
		}

		private Component newProxiSensor(final GeoGraph targetGraph) {
			return new ProximitySensor(camera, 10) {

				@Override
				public void onObjectIsCloseToCamera(GLCamera glCamera, Obj obj,
						MeshComponent meshComp, float currentDistance) {
					Log.d(LOG_TAG, "Proxim Sensor executed, close to " + obj);

					Log.d(LOG_TAG, "     meshComp=" + meshComp);
					Log.d(LOG_TAG, "     meshComp.myColor=" + meshComp.myColor);
					meshComp.myColor = alreadyPassedColor;
					obj.remove(this);

					setToNextWayPoint(targetGraph, obj);

				}

			};
		}

		private void setToNextWayPoint(GeoGraph graph, Obj obj) {

			EfficientList<GeoObj> followers = graph
					.getFollowingNodesOf((GeoObj) obj);
			Log.d(LOG_TAG, "Obj has followers number=" + followers.myLength);
			for (int i = 0; i < followers.myLength; i++) {
				GeoObj o = followers.get(i);
				Log.d(LOG_TAG, "Objo=" + o);
				o.setComp(newProxiSensor(graph));
				o.getComp(MeshComponent.class).myColor = highlightDiamondColor;
			}

		}

		private MeshComponent newWaypointMesh() {
			MeshGroup mesh = new MeshGroup(normalDiamondColor);
			mesh.add(diamond);
			return mesh;
		}

	}

	void addNodeToGraph(GeoGraph graph, GeoObj objectToAdd);

}