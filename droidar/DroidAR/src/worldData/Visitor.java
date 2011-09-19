package worldData;

import geo.Edge;
import geo.GeoGraph;
import geo.GeoObj;
import gl.MeshComponent;
import gl.MeshGroup;
import gl.Shape;
import util.EfficientList;
import util.EfficientListQualified;
import android.util.Log;

import components.Component;
import components.PhysicsComponent;
import components.ProximitySensor;

/**
 * the concrete visitor should override the visit methods and if the visitor
 * want to change the deeper algorithm behaviour (eg search not complete graph)
 * it has to override default_visit to (and call visit by itself!)
 * 
 * TODO create default log text with hint that the subclases all an own accept
 * method
 * 
 * TODO its important that every class wich ever want to be used with the
 * visitor pattern implements its own accept method. its not enough to implement
 * it in the superclass!
 * 
 * @author Spobo
 * 
 */
public abstract class Visitor {

	public boolean default_visit(World world) {

		EfficientList<AbstractObj> list = world.getAllItems();
		final int lenght = list.myLength;
		for (int i = 0; i < lenght; i++) {
			list.get(i).accept(this);
		}
		return visit(world);
	}

	public boolean visit(World x) {
		Log.w("visitor.visit()",
				this.getClass().toString()
						+ "World: no visit action defined for classtype "
						+ x.getClass());
		return false;
	}

	public boolean default_visit(Obj obj) {
		EfficientList<Component> x = obj.myComponents;
		final int lenght = obj.myComponents.myLength;
		for (int i = 0; i < lenght; i++) {
			x.get(i).accept(this);
		}
		return visit(obj);
	}

	public boolean visit(Obj x) {
		Log.w("visitor.visit()", this.getClass().toString()
				+ "Obj: no visit action defined for classtype " + x.getClass());
		return false;
	}

	public boolean default_visit(Shape shape) {
		return visit(shape);
	}

	public boolean visit(Shape x) {
		Log.w("visitor.visit()",
				this.getClass().toString()
						+ "Shape: no visit action defined for classtype "
						+ x.getClass());
		return false;
	}

	public boolean default_visit(MeshGroup meshGroup) {
		EfficientList<MeshComponent> meshes = meshGroup.myMeshes; // this way we
																	// avoid
		// lookups
		final int meshSize = meshGroup.myMeshes.myLength;
		for (int i = 0; i < meshSize; i++) {
			meshes.get(i).accept(this);
		}
		return visit(meshGroup);
	}

	public boolean visit(MeshGroup x) {
		Log.w("visitor.visit()",
				this.getClass().toString()
						+ "MeshGroup: no visit action defined for classtype "
						+ x.getClass());
		return false;
	}

	public boolean default_visit(PhysicsComponent physicsComponent) {
		return visit(physicsComponent);
	}

	public boolean visit(PhysicsComponent x) {
		Log.w("visitor.visit()", this.getClass().toString()
				+ "PhysicsComponent: no visit action defined for classtype "
				+ x.getClass());
		return false;
	}

	// TODO remove this method here, just methods with special behaviour like
	// groups need a seperate method here! remember to update uml diagramm and
	// to do this with all the default objects here
	public boolean default_visit(GeoObj geoObj) {
		return visit(geoObj);
	}

	// TODO remove too, see above
	public boolean visit(GeoObj x) {
		Log.w("visitor.visit()",
				this.getClass().toString()
						+ "GeoObj: no visit action defined for classtype "
						+ x.getClass());
		return false;
	}

	public boolean default_visit(AbstractObj abstractObj) {
		return visit(abstractObj);
	}

	public boolean default_visit(GeoGraph geoGraph) {
		{
			EfficientListQualified<GeoObj> geoObj = geoGraph.getNodes();
			final int l = geoGraph.getNodes().myLength;
			for (int i = 0; i < l; i++) {
				geoObj.get(i).accept(this);
			}
		}
		{
			if (geoGraph.hasEdges()) {
				EfficientList<Edge> e = geoGraph.getEdges();
				final int l = geoGraph.getEdges().myLength;
				for (int i = 0; i < l; i++) {
					e.get(i).accept(this);
				}
			}
		}
		return visit(geoGraph);
	}

	public boolean visit(GeoGraph x) {
		Log.w("visitor.visit()",
				this.getClass().toString()
						+ "GeoGraph: no visit action defined for classtype "
						+ x.getClass());
		return false;
	}

	public boolean visit(AbstractObj x) {
		Log.w("visitor.visit()",
				this.getClass().toString()
						+ "AbstractObj: no visit action defined for classtype "
						+ x.getClass());
		return false;
	}

	public boolean default_visit(ProximitySensor x) {
		return visit(x);
	}

	public boolean visit(ProximitySensor x) {
		Log.w("visitor.visit()",
				this.getClass().toString()
						+ "ProximitySensor: no visit action defined for classtype "
						+ x.getClass());
		return false;
	}

}
