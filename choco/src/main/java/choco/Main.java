package choco;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public class Main {
	private static final String KEY = "T13mrIq6zP9qB436WeYYxQ1j3";
	private static final String SEC = "VFcHMh07yG9LUUzDV6AhmzSqzdVhOhm8870XLW59hQr4Mo9i5V";
	private static final long ID = 2829561398L;

	public static void main(String[] args) {
		System.out.println("Choco ver.1.0.0");
		Configuration cb = new Token(KEY, SEC, ID).configBuild();
		Twitter twitter = new TwitterFactory(cb).getInstance();
		TwitterStream stream = new TwitterStreamFactory(cb).getInstance();
		stream.addListener(new BotStreamListener(twitter, ID));
		stream.user();
	}
}
