package actions;

import actions.algos.Algo;
import actions.algos.BufferAlgo1;

public class DefaultUpdateListener extends Action {

	private final Algo accelBufferAlgo = new BufferAlgo1(0.1f, 4);
	private final Algo magnetBufferAlgo = new BufferAlgo1(0.1f, 4);

	@Override
	public boolean onCamAccelerationUpdate(float[] target, float[] values,
			float timeDelta) {
		return accelBufferAlgo.execute(target, values, timeDelta);
	}

	@Override
	public boolean onCamMagnetometerUpdate(float[] target, float[] values,
			float timeDelta) {
		return magnetBufferAlgo.execute(target, values, timeDelta);
	}

}
