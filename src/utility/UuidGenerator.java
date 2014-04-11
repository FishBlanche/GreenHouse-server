package utility;

import java.util.UUID;


public final class UuidGenerator {


	synchronized final public static String generate36UUID(){
		return UUID.randomUUID().toString().toUpperCase();
	}
	synchronized final public static String generate32UUID(){
		return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	}
}
