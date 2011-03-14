package commands.system;

import actions.ActionCalcRelativePos;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import commands.Command;
import commands.ui.CommandShowToast;

public class CommandRequestWifiPosition extends Command {

	private boolean running = true;

	private long startTime = 0;
	private long timeToUpdate = 10000;

	private Context myTargetActivity;
	private boolean serviceConnceted = false;
	private ActionCalcRelativePos posAction;
	final private Location l = new Location("customCreated");
	private boolean isUpdating;

	public CommandRequestWifiPosition(Context c, ActionCalcRelativePos posAction) {
		myTargetActivity = c;
		this.posAction = posAction;
	}

	@Override
	public boolean execute() {
		if (!serviceConnceted) {
			// start service:
			registerWifi();
			new CommandShowToast(myTargetActivity,
					"Connecting to WiFi service..").execute();
		}
		return startUpdating();
	}

	private boolean startUpdating() {
		if (!isUpdating) {
			isUpdating = true;
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (running) {
						try {
							startTime = SystemClock.uptimeMillis();
							Intent intent = new Intent(
									"net.xenonite.wifiloc.POSITION_REQUEST");
							myTargetActivity.sendBroadcast(intent);
							if (timeToUpdate < 100)
								timeToUpdate = 5000;
							Thread.sleep(timeToUpdate);
						} catch (InterruptedException e) {
							e.printStackTrace();
							running = false;
							isUpdating = false;
						}
					}
				}
			}).start();
			return true;
		} else {
			new CommandShowToast(myTargetActivity, "Already updating!")
					.execute();
			return false;
		}
	}

	public void setServiceConnected(boolean isConnected) {
		serviceConnceted = isConnected;
	}

	private void registerWifi() {

		myTargetActivity.bindService(new Intent(
				"net.xenonite.wifiloc.IwlService"), new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				Log.d("WIFI", "WIFI Service disconnected");
				setServiceConnected(false);
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				new CommandShowToast(myTargetActivity,
						"WiFi service connected.").execute();
				setServiceConnected(true);
			}
		}, Context.BIND_AUTO_CREATE);

		myTargetActivity.registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				timeToUpdate = SystemClock.uptimeMillis() - startTime + 500;
				double lat = intent.getExtras().getDouble("lat");
				double lon = intent.getExtras().getDouble("lng");
				String roomName = intent.getExtras().getString("RoomName");
				String roomNumber = intent.getExtras().getString("RoomNumber");
				new CommandShowToast(myTargetActivity, "(" + lat + ", " + lon
						+ "); " + roomName + " nr=" + roomNumber + " speed="
						+ timeToUpdate).execute();
				l.setLatitude(lat);
				l.setLongitude(lon);
				posAction.onLocationChanged(l);
			}
		}, new IntentFilter("net.xenonite.wifiloc.POSITION_DATA"));

	}

}
