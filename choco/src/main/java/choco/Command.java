package choco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Command implements ExecBase {
	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			for (;;) {
				String line = reader.readLine();
				switch (line) {
				case "exit":
					exit();
					break;
				case "":
					continue;
				default:
					notFound();
					continue;
				}
				break;
			}
		} catch (IOException e) {
		}
	}

	@Override
	public abstract void exit();

	private void notFound() {
		System.out.println("Not Found Command");
	}

}
