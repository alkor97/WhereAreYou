package info.alkor.whereareyou.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to declare requirements.
 * Created by Marlena on 2017-03-30.
 */
@Target({ElementType.METHOD})
public @interface Requirements {
	String[] value();
}
