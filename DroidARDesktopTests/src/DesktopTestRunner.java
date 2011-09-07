import tests.EfficientListTests;
import tests.GeoTests;
import tests.SystemTests;



public class DesktopTestRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new SystemTests().run();
			new EfficientListTests().run();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Testing done");

	}

}
