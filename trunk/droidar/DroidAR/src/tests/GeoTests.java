package tests;

import geo.GeoGraph;
import geo.GeoObj;
import util.Vec;

public class GeoTests extends SimpleTesting {

	private static final String LOG_TEST = "Geo Tests";

	@Override
	public void run() throws Exception {
		t8();
		distanceCalcTest();
		virtualPosToGPSPosTest();
		t7();
		t6();
		t5();
		t4();
		t3();
		t2();
		t1();
	}

	/**
	 * 
	 * <pre>
	 *                 s1
	 *           pUp2
	 *            |
	 *         pUp1
	 *         |
	 *      _ pL2   
	 *  pL1     \      _ pR2
	 *   s3        pR1
	 *          s2
	 * 
	 * </pre>
	 * 
	 * @throws Exception
	 */
	private void t8() throws Exception {

		GeoGraph g = new GeoGraph();
		GeoObj pL1 = new GeoObj(53.465876, 2.895327, 0);
		GeoObj pL2 = new GeoObj(53.469146, 2.902665, 0);
		pL2.getInfoObject().setShortDescr("test");
		GeoObj pR1 = new GeoObj(53.466285, 2.907386, 0);
		GeoObj pR2 = new GeoObj(53.467613, 2.915068, 0);
		GeoObj pU1 = new GeoObj(53.47377, 2.902622, 0);
		GeoObj pU2 = new GeoObj(53.477448, 2.906785, 0);
		GeoObj s1 = new GeoObj(53.477626, 2.90597, 0);
		GeoObj s2 = new GeoObj(53.465033, 2.906399, 0);
		GeoObj s3 = new GeoObj(53.464624, 2.895799, 0);
		assertTrue(g.add(pL1));
		assertTrue(g.add(pL2));
		assertTrue(g.add(pR1));
		assertTrue(g.add(pR2));
		assertTrue(g.add(pU1));
		assertTrue(g.add(pU2));
		assertTrue(g.addEdge(pL1, pL2, null) != null);
		assertTrue(g.addEdge(pL2, pR1, null) != null);
		assertTrue(g.addEdge(pR1, pR2, null) != null);
		assertTrue(g.addEdge(pL2, pU1, null) != null);
		assertTrue(g.addEdge(pU1, pU2, null) != null);
		// System.out.println("geotest " + g.findPath(gL1, gL2).getMyItems());
		assertTrue(g.findPath(pL1, pL2).getAllItems().myLength == 2);
		assertTrue(g.findPath(pL1, pR1).getAllItems().myLength == 3);
		assertTrue(g.findPath(pL1, pR2).getAllItems().myLength == 4);
		assertTrue(g.findPath(pL2, pR2).getAllItems().myLength == 3);
		assertTrue(g.findPath(pL2, pU2).getAllItems().myLength == 3);
		assertTrue(g.findPath(g.getClosesedObjTo(s1), pL1).getAllItems().myLength == 4);
		assertTrue(g.findPath(g.getClosesedObjTo(s1), pL2).getAllItems().myLength == 3);
		assertTrue(g.findPath(g.getClosesedObjTo(s1), pR2).getAllItems().myLength == 5);
		assertTrue(g.findPath(g.getClosesedObjTo(s2), pU2).getAllItems().myLength == 4);
		assertTrue(pL2.matchesSearchTerm("test") == 1);
		assertEquals(g.findBestPointFor("test"), pL2);
		assertTrue(g.findPath(g.getClosesedObjTo(s1),
				g.findBestPointFor("test")).getAllItems().myLength == 3);
		assertTrue(g.findPath(g.getClosesedObjTo(s2),
				g.findBestPointFor("test")).getAllItems().myLength == 2);
		assertTrue(g.findPath(g.getClosesedObjTo(s3),
				g.findBestPointFor("test")).getAllItems().myLength == 2);
	}

	private void distanceCalcTest() throws Exception {
		/*
		 * Calculated with google maps:
		 * 
		 * Bonn = 50.732979,7.086181
		 * 
		 * Frankfurt = 50.113532,8.679199
		 * 
		 * x=111366m result=113719m (error 2%)
		 * 
		 * 2% is as good as 0% because the 111366 were calculated by hand and
		 * are not absolute accurate
		 * 
		 * y=69155m result=68841m (error 0%)
		 */

		GeoObj bonn = new GeoObj(50.732979, 7.086181, 0);
		GeoObj frankfurt = new GeoObj(50.113532, 8.679199, 0);
		Vec result = bonn.getVirtualPosition(frankfurt);
		// System.out.println("bonn frankfurt");
		// System.out.println("x=" + result.x);
		// System.out.println("y=" + result.y);

		// error must be under 3%:

		System.out.println("test geoerror.x ="
				+ Math.abs(1 - (Math.abs(result.x) / 111366)));
		System.out.println("test geoerror.y ="
				+ Math.abs(1 - (Math.abs(result.y) / 69155)));

		assertTrue(Math.abs(1 - (Math.abs(result.x) / 111366)) < 0.03);
		assertTrue(Math.abs(1 - (Math.abs(result.y) / 69155)) < 0.03);

		/*
		 * small distance tests:
		 * 
		 * 50.786838,6.06514
		 * 
		 * 50.758287,6.109085
		 * 
		 * x=3128m result=3094m (error < 2%)
		 * 
		 * y=3202m result=3172m (error < 1%)
		 * 
		 * 
		 * 50.770964,6.095545
		 * 
		 * 50.768955,6.100287
		 * 
		 * x=336m result=333m (error < 1%)
		 * 
		 * y=216m result=223m (error < 3%)
		 * 
		 * 
		 * 50.769401,6.095323
		 * 
		 * 50.769164,6.095787
		 * 
		 * x=32,7m result=32,6m (error 0%)
		 * 
		 * y= 26,4m result=26,3m (error 0%)
		 */
	}

	private void virtualPosToGPSPosTest() throws Exception {
		GeoObj g = new GeoObj(50.754539489746094, 7.227184295654297, 0);

		for (int i = 0; i < 50; i++) {
			Vec rand = Vec.getNewRandomPosInXYPlane(new Vec(), 1, 50);
			float dist = rand.getLength();
			GeoObj g2 = new GeoObj();
			g2.calcGPSPosition(rand, g);
			double calculatedDist = g2.getDistance(g);
			// Log.d(LOG_TEST, "Random distance:" + dist);
			// Log.d(LOG_TEST, "    Random distance:" + calculatedDist);
			double difference = Math.abs(dist - calculatedDist);
			// Log.d(LOG_TEST, "    difference:" + difference);
			assertTrue(difference < 1);
		}

	}

	private void t7() throws Exception {
		// TODO create test to check distance calculation of GeoObj. for example
		// one left from 0 lat one right from 0 lat
	}

	private void t6() throws Exception {
		GeoGraph g = new GeoGraph();
		assertTrue(g.add(GeoObj.a1));
		assertTrue(!g.add(GeoObj.a1));
		assertTrue(g.add(GeoObj.a2));
		assertTrue(g.add(GeoObj.a3));
		assertTrue(g.add(GeoObj.n1));
		assertTrue(g.add(GeoObj.n2));
		assertTrue(g.add(GeoObj.n3));
		assertTrue(!g.add(GeoObj.n3));
		assertTrue(g.addEdge(GeoObj.a1, GeoObj.a2, null) != null);
		assertTrue(g.addEdge(GeoObj.a1, GeoObj.a2, null) == null);
		assertTrue(g.addEdge(GeoObj.a1, GeoObj.a3, null) != null);
		assertTrue(g.addEdge(GeoObj.a1, GeoObj.n1, null) != null);
		assertTrue(g.addEdge(GeoObj.n1, GeoObj.n2, null) != null);
		assertTrue(g.addEdge(GeoObj.n2, GeoObj.n3, null) != null);
		assertTrue(g.findPath(GeoObj.a3, GeoObj.a3).getAllItems().myLength == 1);
	}

	private void t5() throws Exception {
		GeoGraph g = new GeoGraph();

		assertTrue(g.add(GeoObj.iPark1));
		assertTrue(g.add(GeoObj.iPark2));
		assertTrue(g.add(GeoObj.iPark3));
		assertTrue(g.add(GeoObj.iPark4));
		assertTrue(g.add(GeoObj.rwthI9));

		assertTrue(g.addEdge(GeoObj.rwthI9, GeoObj.iPark1, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark1, GeoObj.iPark2, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark2, GeoObj.iPark3, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark3, GeoObj.iPark4, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark1, GeoObj.iPark4, null) != null);

		assertTrue(g.findPath(GeoObj.rwthI9, GeoObj.iPark4).getAllItems().myLength == 3);

	}

	private void t4() throws Exception {
		GeoGraph g = new GeoGraph();
		assertTrue(g.add(GeoObj.rwthI9));
		assertTrue(g.add(GeoObj.iPark1));
		assertTrue(g.add(GeoObj.iPark2));
		assertTrue(g.add(GeoObj.iPark3));
		assertTrue(g.add(GeoObj.iPark4));

		assertTrue(g.addEdge(GeoObj.rwthI9, GeoObj.iPark1, null) != null);
		assertTrue(g.findPath(GeoObj.iPark1, GeoObj.rwthI9).getAllItems().myLength == 2);

		assertTrue(g.addEdge(GeoObj.iPark1, GeoObj.iPark2, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark2, GeoObj.iPark3, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark3, GeoObj.iPark4, null) != null);

		assertTrue(g.findPath(GeoObj.iPark4, GeoObj.iPark2).getAllItems().myLength == 3);

	}

	private void t1() throws Exception {
		GeoGraph g = new GeoGraph();
		assertTrue(g.add(GeoObj.iPark1));
		assertTrue(g.add(GeoObj.iPark2));
		assertTrue(g.add(GeoObj.rwthI9));
		assertTrue(g.add(GeoObj.iPark3));
		assertTrue(g.add(GeoObj.iPark4));
		assertTrue(!g.add(GeoObj.iPark4));
		assertTrue(g.getAllItems().myLength == 5);

		assertTrue(g.addEdge(GeoObj.rwthI9, GeoObj.iPark1, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark1, GeoObj.iPark2, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark2, GeoObj.iPark3, null) != null);
		assertTrue(g.addEdge(GeoObj.rwthI9, GeoObj.iPark3, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark3, GeoObj.iPark4, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark1, GeoObj.iPark4, null) != null);
		// g.addEdge(GeoObj.infZentPark, GeoObj.infZentPark4);

		assertTrue(g.findPath(GeoObj.rwthI9, GeoObj.iPark1).getAllItems().myLength == 2);
		assertTrue(g.findPath(GeoObj.iPark1, GeoObj.iPark2).getAllItems().myLength == 2);
		assertTrue(g.findPath(GeoObj.rwthI9, GeoObj.iPark3).getAllItems().myLength == 2);

		assertTrue(g.findPath(GeoObj.rwthI9, GeoObj.iPark4) != null);
		assertTrue(g.findPath(GeoObj.rwthI9, GeoObj.iPark4).getAllItems() != null);
		assertTrue(g.findPath(GeoObj.rwthI9, GeoObj.iPark4).getAllItems().myLength == 3);
	}

	private void t3() throws Exception {
		GeoGraph g = new GeoGraph();
		assertTrue(g.add(GeoObj.iPark1));
		assertTrue(g.add(GeoObj.iPark2));
		assertTrue(g.add(GeoObj.rwthI9));
		assertTrue(g.add(GeoObj.iPark3));
		assertTrue(g.add(GeoObj.iPark4));
		// assertTrue(g.add(GeoObj.zollern));

		assertTrue(g.addEdge(GeoObj.rwthI9, GeoObj.iPark1, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark1, GeoObj.iPark2, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark2, GeoObj.iPark3, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark3, GeoObj.iPark4, null) != null);

		assertTrue(g.findPath(GeoObj.rwthI9, GeoObj.iPark4) != null);
		assertTrue(g.findPath(GeoObj.rwthI9, GeoObj.iPark4).getAllItems() != null);
		assertTrue(g.findPath(GeoObj.rwthI9, GeoObj.iPark4).getAllItems().myLength == 5);
	}

	private void t2() throws Exception {
		GeoGraph g = new GeoGraph();
		assertTrue(g.add(GeoObj.iPark2));
		assertTrue(g.add(GeoObj.iPark3));
		assertTrue(g.add(GeoObj.iPark4));
		assertTrue(g.add(GeoObj.rwthI9));

		assertTrue(g.addEdge(GeoObj.iPark2, GeoObj.iPark3, null) != null);
		assertTrue(g.addEdge(GeoObj.iPark3, GeoObj.iPark4, null) != null);

		GeoGraph path = g.findPath(GeoObj.iPark2, GeoObj.iPark4);
		assertTrue(path != null);
		assertTrue(path.getAllItems() != null);
		assertTrue(path.getAllItems().myLength == 3);
	}

}
