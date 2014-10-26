package choco;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hibataki.mytwitterlib.Token;

public class Main {
	private static final String KEY = "";
	private static final String SEC = "";
	private static final String NAME = "hai_choco_agano";

	public static void main(String[] args) {
		Token token = Token.getToken(KEY, SEC, NAME);
		ExecutorService exec = Executors.newFixedThreadPool(2);
		Stream stream = new Stream(Token.setStreamToken(token), new BotStreamListener(Token.setToken(token)));
		try {
			exec.execute(stream);
			exec.execute(new Command() {
				@Override
				public void exit() {
					stream.exit();
				}
			});
		} finally {
			exec.shutdown();
		}
	}

}
