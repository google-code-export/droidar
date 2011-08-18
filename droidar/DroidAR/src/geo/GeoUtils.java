package geo;

import geo.EdgeListener.DefaultEdgeListener;
import geo.NodeListener.DefaultNodeListener;
import gl.GLCamera;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import util.Wrapper;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class GeoUtils {

	private static final String LOG_TAG = "Geo Utils";
	private Geocoder myGeoCoder;
	private Context myContext;

	private NodeListener defaultNodeListener;
	private EdgeListener defaultEdgeListener;

	public GeoUtils(Context context, GLCamera glCamera) {
		myContext = context;
		myGeoCoder = new Geocoder(context, Locale.getDefault());
		defaultNodeListener = new DefaultNodeListener(glCamera);
		defaultEdgeListener = new DefaultEdgeListener();
	}
	
	

	/**
	 * This method returns the best match for a specified position. It could for
	 * example be used to calculate the closest address to your current
	 * location.
	 * 
	 * @param location
	 * @return the closest address to the {@link GeoObj}
	 */
	public Address getBestAddressForLocation(GeoObj location) {
		try {
			List<Address> locations = myGeoCoder.getFromLocation(
					location.getLatitude(), location.getLongitude(), 1);
			if (locations.size() > 0) {
				return locations.get(0);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the position of an specified address (Streetname e.g.)
	 * 
	 * @param address
	 * @return null if the address could not be found
	 */
	public GeoObj getBestLocationForAddress(String address) {
		try {
			List<Address> addresses = myGeoCoder
					.getFromLocationName(address, 5);
			if (addresses.size() > 0) {
				GeoObj g = new GeoObj(addresses.get(0));
				g.getInfoObject().setShortDescr(
						address + " (" + g.getInfoObject().getShortDescr()
								+ ")");
				return g;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This will search for a specified address and return the found results
	 * 
	 * @param address
	 * @param maxResults
	 *            number of results
	 * @return a {@link GeoGraph} with maxResults many {@link GeoObj}s as
	 *         specified
	 */
	public GeoGraph getLocationListForAddress(String address, int maxResults) {
		try {
			List<Address> addresses = myGeoCoder.getFromLocationName(address,
					maxResults);
			if (addresses.size() > 0) {
				GeoGraph result = new GeoGraph();
				for (int i = 0; i < addresses.size(); i++) {
					result.add(new GeoObj(addresses.get(i)));
				}
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getStreetFor(GeoObj geoPos) {
		try {
			return getBestAddressForLocation(geoPos).getAddressLine(0);
		} catch (Exception e) {
		}
		return null;
	}

	public String getCityFor(GeoObj currentPos) {
		try {
			return getBestAddressForLocation(currentPos).getAddressLine(1)
					.split(" ")[1];
		} catch (Exception e) {
		}
		return null;
	}

	public static Location getCurrentLocation(Context context) {
		Location l = getCurrentLocation(context, Criteria.ACCURACY_FINE);
		if (l == null) {
			Log.e(LOG_TAG, "Fine accuracy position could not be detected!");
			l = GeoUtils.getCurrentLocation(context, Criteria.ACCURACY_COARSE);
			if (l != null)
				Log.i(LOG_TAG, "Coarse accuracy position detected");
		}
		return l;
	}

	public Location getCurrentLocation() {
		Location l = GeoUtils.getCurrentLocation(myContext,
				Criteria.ACCURACY_FINE);
		if (l == null) {
			Log.e(LOG_TAG,
					"Fine accuracy position could not be detected! Will use coarse location.");
			l = GeoUtils
					.getCurrentLocation(myContext, Criteria.ACCURACY_COARSE);
			if (l == null) {
				Log.e(LOG_TAG,
						"Coarse accuracy position could not be detected! Last try..");
				try {
					LocationManager lm = ((LocationManager) myContext
							.getSystemService(Context.LOCATION_SERVICE));
					Log.d(LOG_TAG, "Searching through "
							+ lm.getAllProviders().size()
							+ " location providers");
					for (int i = lm.getAllProviders().size() - 1; i >= 0; i--) {
						l = lm.getLastKnownLocation(lm.getAllProviders().get(i));
						if (l != null)
							break;
					}
				} catch (Exception e) {
				}
			}
		}
		Log.d(LOG_TAG, "current position=" + l);
		return l;
	}

	public static Location getCurrentLocation(Context context, int accuracy) {
		if (context != null) {
			try {
				LocationManager lm = (LocationManager) context
						.getSystemService(Context.LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				criteria.setAccuracy(accuracy);
				return lm.getLastKnownLocation(lm.getBestProvider(criteria,
						true));
			} catch (Exception e) {
				Log.e(LOG_TAG, "Could not receive the current location");
				e.printStackTrace();
				return null;
			}
		}
		Log.e(LOG_TAG, "The passed activity was null!");
		return null;
	}

	/**
	 * @param startPos
	 * @param destPos
	 * @param myResultingPath
	 *            in this Wrapper the resulting path will be stored
	 * @param byWalk
	 * @return
	 */
	public boolean getPathFromAtoB(GeoObj startPos, GeoObj destPos,
			Wrapper myResultingPath, boolean byWalk) {
		GeoGraph result = getPathFromAtoB(startPos, destPos, byWalk);
		if (result != null) {
			Log.d(LOG_TAG, "Found way on maps!");
			Log.d(LOG_TAG, "Path infos: " + result.toString());
			myResultingPath.setTo(result);
			return true;
		}
		Log.d(LOG_TAG, "No way on maps found :(");
		return false;
	}

	public GeoGraph getPathFromAtoB(GeoObj startPos, GeoObj destPos,
			boolean byWalk) {
		return getPathFromAtoB(startPos, destPos, byWalk, null, null);
	}

	/**
	 * Uses google maps to calculate the way from the start pos to the
	 * destination pos
	 * 
	 * @param startPos
	 * @param destPos
	 * @param byWalk
	 * @param nodeListener
	 * @param edgeListener
	 * @return
	 */
	public GeoGraph getPathFromAtoB(GeoObj startPos, GeoObj destPos,
			boolean byWalk, NodeListener nodeListener, EdgeListener edgeListener) {

		if (startPos == null || destPos == null) {
			Log.d(LOG_TAG,
					"Gmap getPathFromAtoB error: startPoint or target were null");
			return null;
		}

		// try to open the url:
		try {
			String url = generateUrl(startPos, destPos, byWalk);
			Document kml = getDocumentFromUrl(url);

			if (kml.getElementsByTagName("GeometryCollection").getLength() > 0) {

				String path = kml.getElementsByTagName("GeometryCollection")
						.item(0).getFirstChild().getFirstChild()
						.getFirstChild().getNodeValue();

				final String[] pairs = path.split(" ");
				GeoGraph result = new GeoGraph();
				result.getInfoObject().setShortDescr(
						"Resulting graph for "
								+ destPos.getInfoObject().getShortDescr());
				result.setIsPath(true);

				if (nodeListener != null) {
					nodeListener.addNodeToGraph(result, startPos);
				} else {
					defaultNodeListener.addNodeToGraph(result, startPos);
				}

				GeoObj lastPoint = startPos;
				for (int i = 1; i < pairs.length; i++) {
					String[] geoCords = pairs[i].split(",");

					GeoObj geoObj = new GeoObj(Double.parseDouble(geoCords[1]),
							Double.parseDouble(geoCords[0]),
							Double.parseDouble(geoCords[2]));

					if (nodeListener != null) {
						nodeListener.addNodeToGraph(result, geoObj);
					} else {
						defaultNodeListener.addNodeToGraph(result, geoObj);
					}
					if (edgeListener != null) {
						edgeListener.addEdgeToGraph(result, lastPoint, geoObj);
					} else {
						defaultEdgeListener.addEdgeToGraph(result, lastPoint,
								geoObj);
					}
					lastPoint = geoObj;

					Log.d(LOG_TAG, "     + adding Waypoint:" + pairs[i]);
				}
				if (lastPoint != null && !lastPoint.hasSameCoordsAs(destPos)) {
					result.addEdge(lastPoint, destPos, null);
				}
				result.add(destPos);

				/*
				 * an alternative for adding the edges would be to call
				 * result.addEdgesToCreatePath(); but this would be a bit
				 * slower..
				 */

				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Document getDocumentFromUrl(String url) throws IOException,
			MalformedURLException, ProtocolException,
			FactoryConfigurationError, ParserConfigurationException,
			SAXException {
		HttpURLConnection urlConnection = (HttpURLConnection) new URL(url)
				.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.connect();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		// get the kml file. And parse it to get the coordinates(direction
		// route):
		Document doc = db.parse(urlConnection.getInputStream());
		return doc;
	}

	private String generateUrl(GeoObj startPos, GeoObj destPos, boolean byWalk) {
		// build the url string:
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.google.com/maps?f=d&hl=en");
		if (byWalk) {
			urlString.append("&dirflg=w");
		}
		urlString.append("&saddr=");// from
		urlString.append(Double.toString(startPos.getLatitude()));
		urlString.append(",");
		urlString.append(Double.toString(startPos.getLongitude()));
		urlString.append("&daddr=");// to
		urlString.append(Double.toString(destPos.getLatitude()));
		urlString.append(",");
		urlString.append(Double.toString(destPos.getLongitude()));
		urlString.append("&;ie=UTF8&0&om=0&output=kml");
		return urlString.toString();
	}

}
