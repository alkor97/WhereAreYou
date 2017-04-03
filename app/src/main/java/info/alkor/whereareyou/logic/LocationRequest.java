package info.alkor.whereareyou.logic;

import android.support.annotation.NonNull;

/**
 * Location request.
 * Created by Marlena on 2017-03-30.
 */
public class LocationRequest {

	private final String originAddress;
	private final Type type;

	LocationRequest(@NonNull String originAddress, @NonNull Type type) {
		this.originAddress = originAddress;
		this.type = type;
	}

	public String getOriginAddress() {
		return originAddress;
	}

	public Type getType() {
		return type;
	}

	/**
	 * Location request types.
	 * Created by Marlena on 2017-04-01.
	 */
	public enum Type {
		SINGLE
	}
}
