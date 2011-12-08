package geo;

import gl.scenegraph.MeshComponent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import commands.Command;

public class GeoObjWrapper extends OverlayItem {

	public GeoObj myGeoObj;
	private Drawable myMarker;

	public GeoObjWrapper(GeoObj o) {
		super(GMap.toGeoPoint(o), o.getInfoObject().getShortDescr(), o
				.getInfoObject().getLongDescrAsString());
		myGeoObj = o;
		myMarker = myGeoObj.getInfoObject().getOverlayIcon();
		if (myMarker != null)
			CustomItemizedOverlay.setMarkerBottomCenterd(myMarker);

	}

	@Override
	public GeoPoint getPoint() {
		return GMap.toGeoPoint(myGeoObj);
	}

	@Override
	public Drawable getMarker(int markerNr) {
		if (myMarker != null) {
			// myMarker.setState(android.graphics.drawable.Drawable.class,stateBitset);
			return myMarker;
		}
		// if no marker is set, load default marker:
		return super.getMarker(markerNr);
	}

	/**
	 * this method is called if the {@link GeoObj} is contained in a
	 * {@link CustomItemizedOverlay} and selected in a {@link MapView}
	 * 
	 * @return true if the geoObj reacted on this event
	 */
	public boolean onTab() {
		if (myGeoObj.getGraphicsComponent() instanceof MeshComponent) {
			Command command = (myGeoObj.getGraphicsComponent())
					.getOnMapClickCommand();
			if (command != null) {
				return command.execute(myGeoObj);
			}
		}
		return false;
	}

}