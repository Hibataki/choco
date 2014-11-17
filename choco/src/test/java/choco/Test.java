package choco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Test {
	private static final Logger logger = Logger.getLogger("Test");

	public static void main(String[] args) {
		checkTextError(new File("tweet_file.txt"));
	}

	/**
	 * ファイルの文字数チェック
	 * @param args
	 */
	public static void checkTextError(File file) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			br.lines().filter(Test::replaceOver).forEach(System.out::println);
		} catch (IOException e) {
			logger.warning(() -> e.getMessage());
		}
	}

	/**
	 * 140文字以上の場合true
	 * @param x
	 * @return
	 */
	private static boolean replaceOver(String x) {
		return 140 < x.replaceAll("\\\\n", "\n").length();
	}
}
