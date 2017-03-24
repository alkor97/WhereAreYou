package info.alkor.whereareyou.settings;

import org.junit.Test;

import info.alkor.whereareyou.settings.DistanceUnit;

import static org.junit.Assert.assertEquals;
import static info.alkor.whereareyou.settings.DistanceUnit.*;

/**
 * Created by Marlena on 2017-02-19.
 */

public class DistanceUnitTest {

	private class ValueWithUnit {
		private final float value;
		private final DistanceUnit unit;

		public ValueWithUnit(float value, DistanceUnit unit) {
			this.value = value;
			this.unit = unit;
		}

		public void equalsTo(float value, DistanceUnit unit) {
			float actual = unit.from(this.value, this.unit);
			assertEquals(value, actual, 0.01);
		}
	}

	private ValueWithUnit distanceOf(float value, DistanceUnit unit) {
		return new ValueWithUnit(value, unit);
	}

	@Test
	public void test() {
		distanceOf(0.3048f, METERS).equalsTo(1f, FEET);
		distanceOf(1f, FEET).equalsTo(0.3048f, METERS);

		distanceOf(1000f, METERS).equalsTo(1f, KILOMETERS);
		distanceOf(3f, FEET).equalsTo(1f, YARDS);

		// TODO: rest of conversions, maybe algorithmic?
	}
}
