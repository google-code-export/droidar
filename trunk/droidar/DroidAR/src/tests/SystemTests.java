package tests;

import gl.MeshComponent;
import gl.MeshGroup;
import gl.Shape;
import util.Calculus;
import util.Wrapper;
import worldData.Obj;

import components.Component;
import components.ProximitySensor;

public class SystemTests extends SimpleTesting {

	@Override
	public void run() throws Exception {
		wrapperTests();
		vecTests();
		objTests();
		utilTests();
	}

	private void utilTests() throws Exception {
		for (int i = 0; i < 20; i++) {
			assertTrue(Calculus.getRandomFloat(0, 2) >= 0);
			assertTrue(Calculus.getRandomFloat(0, 2) <= 2);
		}
	}

	private void objTests() throws Exception {
		Obj o = new Obj();
		o.setComp(new Shape());
		assertTrue(o.hasComponent(Shape.class));
		assertFalse(o.hasComponent(MeshGroup.class));
		assertTrue(o.hasComponent(MeshComponent.class));
		assertTrue(o.hasComponent(Component.class));
		assertFalse(o.hasComponent(ProximitySensor.class));

	}

	private void vecTests() throws Exception {
		// TODO write tests for vec here
	}

	private void wrapperTests() throws Exception {
		Wrapper w = new Wrapper();
		assertTrue(w.getType() == Wrapper.Type.None);
		w.setTo(true);
		assertTrue(w.getType() == Wrapper.Type.Bool);
		assertTrue(!w.equals(false));
		w.setTo(1.0f);
		assertTrue(w.getType() == Wrapper.Type.Float);
	}

}
