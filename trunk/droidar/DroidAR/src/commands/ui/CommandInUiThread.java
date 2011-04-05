package commands.ui;

import android.os.Handler;
import android.os.Looper;

import commands.Command;

public abstract class CommandInUiThread extends Command {

	private Handler mHandler = new Handler(Looper.getMainLooper());

	@Override
	public final boolean execute() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				executeInUiThread();
			}

		});
		return true;
	}

	public abstract void executeInUiThread();

}
