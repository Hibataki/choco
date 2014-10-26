package choco;

/**
 * Runnable拡張インターフェース
 *
 */
public interface ExecBase extends Runnable {

	/**
	 * スレッドを終了する時に実行するメソッド
	 */
	public void exit();

}
