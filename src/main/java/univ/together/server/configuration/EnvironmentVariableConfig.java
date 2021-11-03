package univ.together.server.configuration;

public class EnvironmentVariableConfig {

	private static final String photo_url = "http://101.101.216.93:8080/images/";
	private static final String down_url = "http://101.101.216.93:8080/file_down/";
	
	private EnvironmentVariableConfig() {}
	
	public static String getPhotoUrl() {
		return photo_url;
	}
	
	public static String getDownUrl() {
		return down_url;
	}
	
}
