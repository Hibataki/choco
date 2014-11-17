package choco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class Count {
	private static final Logger LOGGER = Logger.getLogger("Count");
	public static void main(String[] args) throws Exception {
		String[] array = {"阿賀野", "てんご", "改九", "かでんつ", "いーさん",
				"ホモビ", "垣根", "五十鈴", "げもち", "はすあ", "太刀", "ヘタレズ", "ゆきんこ"};
		List<Count> list = Count.arrayToCount(new File("tweet_file.txt"), Count.upComparator(), array);

		for (Count c : list) {
			System.out.println(c.toString());
		}
	}

	private final String name;
	private int count;

	public Count(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

	public void incrementCount() {
		this.count++;
	}

	@Override
	public String toString() {
		return name + ": " + count;
	}

	public static Comparator<Count> upComparator() {
		return (x, y) -> y.getCount() - x.getCount();
	}

	public static Comparator<Count> downComparator() {
		return (x, y) -> x.getCount() - y.getCount();
	}

	/**
	 * ファイルの1行に対象の文字列が出てくる回数を調べる
	 * @param array 対象の文字列（複数可）
	 * @param file 読み取るファイル
	 * @param comp コンパレータ
	 * @return 名前と回数を記録したCountクラスのリスト
	 */
	public static List<Count> arrayToCount(File file, Comparator<Count> comp, String... array) {
		List<Count> result = new ArrayList<>(array.length);
		for (String s : array) {
			result.add(new Count(s));
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			br.lines().forEach(x -> {
				for (Count c : result) {
					if (x.contains(c.getName())) {
						c.incrementCount();
					}
				}
			});
		} catch (IOException e) {
			LOGGER.warning(() -> e.getMessage());
		}
		Collections.sort(result, comp);
		return result;
	}
}
