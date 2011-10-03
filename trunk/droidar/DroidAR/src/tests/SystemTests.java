package tests;

import gl.MeshComponent;
import gl.RenderGroup;
import gl.Shape;
import util.Calculus;
import util.Vec;
import util.Wrapper;
import worldData.Entity;
import worldData.Obj;
import android.util.Log;

import components.ProximitySensor;

public class SystemTests extends SimpleTesting {

	private static final String LOG_TAG = "SystemTests";

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
		assertFalse(o.hasComponent(RenderGroup.class));
		assertTrue(o.hasComponent(MeshComponent.class));
		assertTrue(o.hasComponent(Entity.class));
		assertFalse(o.hasComponent(ProximitySensor.class));

	}

	private void vecTests() throws Exception {
		normalCalculationTest();
		assertEquals(Vec.rotatedVecInXYPlane(10, 90), new Vec(0, 10, 0));
		vecRoundingTest();
	}

	private void vecRoundingTest() throws Exception {
		Vec v = new Vec(0.12345678f, 0.12345678f, 0.12345678f);
		v.round(100);
		assertTrue(v.x == 0.12f);
	}

	private void normalCalculationTest() throws Exception {
		Vec a = new Vec(5, 0, 0);
		Vec b = new Vec(0, 5, 0);
		Vec c = new Vec(5, -5, 0);
		Vec n1 = Vec.calcNormalVec(a, b).normalize();
		Vec n2 = Vec.calcNormalVec(a, c).normalize();
		Vec n3 = Vec.calcNormalVec(b, c).normalize();
		assertTrue(n1.equals(n2) || n1.equals(Vec.mult(-1, n2)));
		assertTrue(n2.equals(n3) || n2.equals(Vec.mult(-1, n3)));
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
