import java.util.Random;
import java.util.Scanner;

public class Agent1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
			int n = Integer.parseInt(input);
			System.err.println("inputs: " + n);
			for (int i = 0; i < n; i++) {
				String line = scanner.nextLine();
			}

			Random random = new Random();

			System.out.println("MOVE 0 " + random.nextInt(11) + " " + random.nextInt(4));
        }

    }
}
