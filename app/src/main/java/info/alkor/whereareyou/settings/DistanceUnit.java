package info.alkor.whereareyou.settings;

import android.support.annotation.NonNull;

/**
 * Distance unit.
 * Created by Marlena on 2017-02-19.
 */
public enum DistanceUnit {

	METERS {
		protected float getMeterMultiplier() {
			return 1.0f;
		}
	},

	KILOMETERS {
		protected float getMeterMultiplier() {
			return 1000.0f;
		}
	},

	FEET {
		protected float getMeterMultiplier() {
			return 0.3048f;
		}
	},

	YARDS {
		protected float getMeterMultiplier() {
			return 0.9144f;
		}
	},

	MILES {
		protected float getMeterMultiplier() {
			return 1609.344f;
		}
	};

	public final float from(float value, @NonNull DistanceUnit unit) {
		return unit.toMeters(value) / getMeterMultiplier();
	}

	private float toMeters(float value) {
		return value * getMeterMultiplier();
	}

	protected float getMeterMultiplier() {
		throw new AbstractMethodError();
	}
}
