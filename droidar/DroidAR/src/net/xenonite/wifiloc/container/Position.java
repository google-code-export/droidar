package net.xenonite.wifiloc.container;

import android.os.Parcel;
import android.os.Parcelable;

public class Position implements Parcelable {
	/**
	 * latitude
	 */
	private double lat;

	/**
	 * longitude
	 */
	private double lng;

	/**
	 * floor number
	 */
	private int floor;

	/**
	 * UNIX timestamp of the position
	 */
	private long time;

	/**
	 * constructor
	 * 
	 * @param lat
	 * @param lng
	 * @param floor
	 * @param time
	 */
	public Position(double lat, double lng, int floor, long time) {
		this.lat = lat;
		this.lng = lng;
		this.floor = floor;
		this.time = time;
	}

	/**
	 * constructor, auto creating timestamp
	 * 
	 * @param lat
	 * @param lng
	 * @param floor
	 */
	public Position(double lat, double lng, int floor) {
		this(lat, lng, floor, System.currentTimeMillis());
	}

	/**
	 * returns the latitude
	 * 
	 * @return latitude
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * returns the longitude
	 * 
	 * @return longitude
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * returns the floor number, where 0 denotes the ground floor, and i the
	 * i'th floor. negative values denote basement levels
	 * 
	 * @return floor number
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * returns the UNIX timestamp
	 * 
	 * @return timestamp
	 */
	public long getTime() {
		return time;
	}

	/**
	 * parcelable creator
	 */
	public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
		public Position createFromParcel(Parcel in) {
			Position position = new Position(in.readDouble(), in.readDouble(),
					in.readInt(), in.readLong());

			return position;
		}

		public Position[] newArray(int size) {
			return new Position[size];
		}
	};

	/**
	 * write to parcel
	 */
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeDouble(this.lat);
		arg0.writeDouble(this.lng);
		arg0.writeInt(this.floor);
		arg0.writeLong(this.time);
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
