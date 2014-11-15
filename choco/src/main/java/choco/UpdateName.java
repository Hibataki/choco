package choco;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class UpdateName {

	public static String name(Twitter twitter, String text) {
		String name = text.substring(0, text.indexOf("(@"));
		if (name.length() > 20) {
			return "長すぎる";
		} else {
			try {
				twitter.updateProfile(name, null, null, null);
				return "「" + name + "に名前を変更しました";
			} catch (TwitterException e) {
				return "失敗した";
			}
		}
	}
}
