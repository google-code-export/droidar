package net.xenonite.wifiloc.container;

import android.os.Parcel;
import android.os.Parcelable;

public class PointOfInterest implements Parcelable {
	/**
	 * the id of the POI
	 */
	private int id;

	/**
	 * latitude of the POI
	 */
	private double lat;

	/**
	 * longitude of the POI
	 */
	private double lng;

	/**
	 * distance of the POI to the last known position
	 */
	private double distance;

	/**
	 * title of the POI
	 */
	private String title;

	/**
	 * description of the POI
	 */
	private String description;

	/**
	 * constructor
	 * 
	 * @param id
	 * @param lat
	 * @param lng
	 * @param distance
	 * @param title
	 * @param description
	 */
	public PointOfInterest(int id, double lat, double lng, double distance,
			String title, String description) {
		this.id = id;
		this.lat = lat;
		this.lng = lng;
		this.distance = distance;
		this.title = title;
		this.description = description;
	}

	/**
	 * returns the id of the POI
	 * 
	 * @return the id of the POI
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * returns the latitude of the POI
	 * 
	 * @return the latitude of the POI
	 */
	public double getLat() {
		return this.lat;
	}

	/**
	 * returns the longitude of the POI
	 * 
	 * @return the longitude of the POI
	 */
	public double getLng() {
		return this.lng;
	}

	/**
	 * returns the distance of the POI to the last known position
	 * 
	 * @return distance
	 */
	public double getDistance() {
		return this.distance;
	}

	/**
	 * returns the title of the POI
	 * 
	 * @return the title of the POI
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * retruns the description of the POI
	 * 
	 * @return the description of the POI
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * sets the distance value
	 * 
	 * @param the
	 *            distance in meter
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * parcelable interface
	 */
	public static final Parcelable.Creator<PointOfInterest> CREATOR = new Parcelable.Creator<PointOfInterest>() {
		public PointOfInterest createFromParcel(Parcel in) {
			PointOfInterest poi = new PointOfInterest(in.readInt(), in
					.readDouble(), in.readDouble(), in.readDouble(), in
					.readString(), in.readString());

			return poi;
		}

		public PointOfInterest[] newArray(int size) {
			return new PointOfInterest[size];
		}
	};

	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(this.id);
		arg0.writeDouble(this.lat);
		arg0.writeDouble(this.lng);
		arg0.writeDouble(this.distance);
		arg0.writeString(this.title);
		arg0.writeString(this.description);
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
