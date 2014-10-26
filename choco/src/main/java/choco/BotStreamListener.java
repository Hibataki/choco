package choco;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserStreamAdapter;

final class BotStreamListener extends UserStreamAdapter {
	private static final String CHOCO = "ﾊｨ ﾁｮｺ(＊´∀`)ノ■ 阿賀野を可愛がってくれてありがと！ これからも頑張っちゃうからよろしくねっ！ きらり～ん☆彡";
	private Twitter twitter;
	private String name;

	public BotStreamListener(Twitter twitter) {
		this.twitter = twitter;
		try {
			this.name = twitter.getScreenName();
		} catch (TwitterException e) {
			System.err.println("名前の取得に失敗しました");
		}
	}

	@Override
	public void onStatus(Status status) {
		if (status.getText().contains("@hai_choco_agano チョコ")) {
			try {
				twitter.updateStatus(new StatusUpdate("@" + status.getUser().getScreenName() + " " + CHOCO).inReplyToStatusId(status.getId()));
			} catch (TwitterException e) {
			}
		}
	}

	@Override
	public void onFollow(User source, User from) {
		try {
			if ((name != null && !source.getScreenName().equals(name)) || name == null) { // ScreenNameがnullの場合, ScreenNameがnullでなくなおかつsourceのユーザーとScreenNameが一致していない場合にtrueを返す
				twitter.createFriendship(source.getId());
			}
		} catch (TwitterException e) {
		}
	}

}
