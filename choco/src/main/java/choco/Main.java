package choco;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public final class Main {
	private static final String KEY = "";
	private static final String SEC = "";
	private static final long ID = 2829561398L;
	private static final Logger LOGGER = Logger.getLogger("Main");
	public static final File TWEET_FILE = new File("tweet_file.txt");

	public static void main(String[] args) {
		System.out.println("Choco ver.3.1.0");
		Configuration cb = new Token(KEY, SEC, ID).configBuild();
		Twitter twitter = new TwitterFactory(cb).getInstance();
		TwitterStream stream = new TwitterStreamFactory(cb).getInstance();
		Bot r = new Bot(twitter, 600, TWEET_FILE);
		ExecutorService exec = Executors.newSingleThreadExecutor();
		stream.addListener(new BotStreamListener(twitter, ID, TWEET_FILE));
		stream.user();

		boolean closed = false;

		try {
			Future<List<String>> future = exec.submit(r);
			while (!closed) {
				r.listPost(future.get());
			}
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.warning(e.getMessage());
		} finally {
			exec.shutdown();
			stream.shutdown();
		}
	}
}
