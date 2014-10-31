package choco;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Token {
	private final File parentDir = new File("token");
	private File childDir;
	private Map<String, String> prop;
	private String key;
	private String secret;
	private int hash;

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
		this.childDir = new File(parentDir, id + ".tok");
		load();
	}

	private String get(String key) {
		return prop.get(hash + "." + key);
	}

	private void load() {
		try (BufferedReader reader = new BufferedReader(new FileReader(childDir))) {
			this.prop = reader.lines().filter(x -> x.contains("=") && x.startsWith(String.valueOf(hash))
					).collect(Collectors.toMap(x -> x.split("=")[0].trim(), x -> x.split("=")[1].trim()));
		} catch (IOException e) {
		}

		if (prop == null || !prop.containsKey(hash + "." + "key")) {
			try {
				getAccessToken();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			load();
		}
	}

	private void getAccessToken() throws TwitterException {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(key, secret);
		RequestToken req = twitter.getOAuthRequestToken();

		try {
			Desktop.getDesktop().browse(new URI(req.getAuthorizationURL()));
		} catch (IOException | URISyntaxException ignore) {
		}

		System.out.println("Enter PIN Code.");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			saveAccessToken(twitter.getOAuthAccessToken(req, reader.readLine().trim()));
		} catch (IOException ignore) {
		}
	}

	private void saveAccessToken(AccessToken token) throws IOException {
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(childDir, true));) {
			StringBuilder base = new StringBuilder().append(hash).append('.');
			StringBuilder lkey = new StringBuilder("key = ").append(key);
			StringBuilder lsecret = new StringBuilder("secret = ").append(secret);
			StringBuilder ltoken = new StringBuilder("token = ").append(token.getToken());
			StringBuilder ltokensecret = new StringBuilder("tokensecret = ").append(token.getTokenSecret());
			Stream.of(lkey, lsecret, ltoken, ltokensecret).forEach(x -> {
				try {
					writer.write(new StringBuilder(base).append(x).toString());
					writer.newLine();
				} catch (Exception e) {
				}
			});
		}
	}

	public Configuration configBuild() {
		if (get("key") == null) {
			try {
				getAccessToken();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			load();
		}
		return new ConfigurationBuilder()
		.setOAuthConsumerKey(get("key"))
		.setOAuthConsumerSecret(get("secret"))
		.setOAuthAccessToken(get("token"))
		.setOAuthAccessTokenSecret(get("tokensecret")).build();
	}
}
