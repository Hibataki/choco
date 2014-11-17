package choco;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Token {
	private static final Logger LOGGER = Logger.getLogger("Token");
	private static final File PARENT_DIR = new File("token");
	private final File childDir;
	private final String key;
	private final String secret;
	private final int hash;
	private Map<String, String> prop;

	/**
	 * Configurationを生成するクラス
	 * @param key 使用するCK
	 * @param secret 使用するCS
	 * @param id 認証するID
	 */
	public Token(String key, String secret, long id) {
		this.key = key;
		this.secret = secret;
		this.hash = key.hashCode() * secret.hashCode();
		this.childDir = new File(PARENT_DIR, id + ".tok");
		load();
	}

	private void load() {
		try (BufferedReader reader = new BufferedReader(new FileReader(childDir))) {
			this.prop = reader.lines()
					.filter(x -> x.startsWith(String.valueOf(hash)) && x.contains("="))
					.collect(Collectors.toMap(
							x -> x.split("=")[0].trim(),
							x -> x.split("=")[1].trim()
					));
		} catch (IOException e) {
			LOGGER.warning(() -> e.getMessage());
		}
		getAccessToken();
	}

	public Configuration configBuild() {
		getAccessToken();
		return new ConfigurationBuilder()
		.setOAuthConsumerKey(get("key"))
		.setOAuthConsumerSecret(get("secret"))
		.setOAuthAccessToken(get("token"))
		.setOAuthAccessTokenSecret(get("tokensecret")).build();
	}

	private String get(String key) {
		return prop.get(hash + "." + key);
	}

	private void getAccessToken() {
		if (prop == null || get("key") == null || get("token") == null) {
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(key, secret);
			RequestToken req = null;

			try {
				req = twitter.getOAuthRequestToken();
				Desktop.getDesktop().browse(new URI(req.getAuthorizationURL()));
			} catch (IOException | URISyntaxException | TwitterException ignore) {
			}

			System.out.println("Enter PIN Code.");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				saveAccessToken(twitter.getOAuthAccessToken(req, reader.readLine().trim()));
			} catch (TwitterException e) {
				LOGGER.severe(() -> e.getMessage());
			} catch (IOException ignore) {
			}

			load();
		}
	}

	private void saveAccessToken(AccessToken token) {
		if (!PARENT_DIR.exists()) {
			PARENT_DIR.mkdirs();
		}

		String separator = System.lineSeparator();
		StringBuilder base = new StringBuilder().append(hash).append(".");
		StringBuilder result = new StringBuilder(base).append("key = ").append(key).append(separator)
				.append(base).append("secret = ").append(secret).append(separator)
				.append(base).append("token = ").append(token.getToken()).append(separator)
				.append(base).append("tokensecret = ").append(token.getTokenSecret()).append(separator);

		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(childDir, true), "UTF-8"))) {
			bw.write(result.toString());
			bw.flush();
		} catch (IOException e) {
			LOGGER.warning(() -> e.getMessage());
		}
	}
}
