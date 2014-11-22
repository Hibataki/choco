package choco;

import java.io.File;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public final class Main {
	private static final String KEY = "";
	private static final String SEC = "";
	private static final long ID = 2829561398L;
	public static final File TWEET_FILE = new File("tweet_file.txt");

	public static void main(String[] args) {
		System.out.println("Choco ver.3.0.2");
		Configuration cb = new Token(KEY, SEC, ID).configBuild();
		Twitter twitter = new TwitterFactory(cb).getInstance();
		TwitterStream stream = new TwitterStreamFactory(cb).getInstance();
		Bot r = new Bot(twitter, 600, TWEET_FILE);
		Thread t = new Thread(r);
		stream.addListener(new BotStreamListener(twitter, ID, TWEET_FILE));
		stream.user();
		t.start();
	}
}
