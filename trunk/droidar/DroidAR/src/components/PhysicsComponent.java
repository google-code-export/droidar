package components;

import gl.MeshComponent;
import system.ParentStack;
import util.Vec;
import worldData.Obj;
import worldData.Updateable;
import worldData.Visitor;

/**
 * currently only for testing! not functional jet
 * 
 * @author Spobo
 * 
 */
public class PhysicsComponent implements Component {

	public static final float FRICTION = 1; // TODO make world dependant
	public static final Vec GRAVITY = new Vec(0, 0, 0);

	Boolean physicsActive = true;
	public Vec force = new Vec();
	private Vec velocity = new Vec();
	private Vec accel = new Vec();
	private float mass = 1;

	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (physicsActive) {
			Obj obj = (Obj) parent;
			final MeshComponent v = obj.getGraphicsComponent();

			updateAccel(timeDelta);
			velocityVerletIntegration(timeDelta, v.myPosition);
			updateSpeed(timeDelta);
			iterateCollisions(timeDelta, obj);
		}
		return true;
	}

	private void updateSpeed(float td) {
		velocity.mult(1 / FRICTION); // TODO correct with dt this way?
	}

	private void updateAccel(float timeDelta) {
		accel.setToZero();
		accel.add(GRAVITY);
	}

	/**
	 * velocity verlet integration, should be by rigid body simulation.. <a
	 * href="http://en.wikipedia.org/wiki/Verlet_integration">Wiki text
	 * 
	 * @param dt
	 * 
	 * @param x
	 */
	private void velocityVerletIntegration(float dt, Vec x) {
		// step 1:
		x.add(Vec.mult(dt, velocity).add(Vec.mult(0.5f * dt * dt, accel)));
		// step 2:
		Vec velocHalf = Vec.add(velocity, Vec.mult(0.5f * dt, accel));
		// step 3:
		addForceToAcceleration(dt);
		// step 4:
		velocity.setToVec(Vec.add(velocHalf, Vec.mult(0.5f * dt, accel)));
	}

	private void addForceToAcceleration(float dt) {
		if (!force.isNullVector()) {
			accel.add(Vec.mult(1 / mass, force));
		}
		force.setToZero();
	}

	private void iterateCollisions(float timeDelta, Obj obj) {
		// check for collisions and solve them. do this several times to avoid
		// clipping bugs etc
	}

}
