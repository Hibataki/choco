package choco;

import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public class Stream implements ExecBase {
	private TwitterStream stream;
	private StatusListener listener;

	public Stream(TwitterStream stream, StatusListener listener) {
		this.stream = stream;
		this.listener = listener;
	}

	@Override
	public void run() {
		stream.addListener(listener);
		stream.user();
	}

	@Override
	public void exit() {
		stream.shutdown();
	}
}
