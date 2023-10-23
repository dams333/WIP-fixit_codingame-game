import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int mapWidth = in.nextInt();
        int mapHeight = in.nextInt();

        // game loop
        while (true) {
            List<List<Integer>> grid;
            grid = new ArrayList<List<Integer>>(mapWidth);
            for (int i = 0; i < mapWidth; i++) {
                grid.add(new ArrayList<Integer>(mapHeight));
            }
			int myCredits = in.nextInt();
            int opponentCredits = in.nextInt();
			int spawnBug = in.nextInt(); // number of turns before the next bug appears (0 = appears at the end of current turn)
            int caseCount = in.nextInt();
            int bugMax = 0;
            for (int i = 0; i < caseCount; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                int bugLevel = in.nextInt(); // 0 = no bug / 1,2,3 = bug level on the case
				int upgradeBug = in.nextInt(); // number of turns before the bug upgrades (0 = upgrades at the end of current turn)
                grid.get(x).add(bugLevel);
                if (bugLevel > bugMax)
                    bugMax = bugLevel;
            }
            int fixerCount = in.nextInt();
			int myFixerId = -1;
            int myFixerX = -1;
            int myFixerY = -1;
            for (int i = 0; i < fixerCount; i++) {
                int id = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                int owner = in.nextInt(); // 1 = you / -1 = opponent
				if (owner == 1) {
					myFixerId = id;
                    myFixerX = x;
                    myFixerY = y;
                }
            }

            // Write an answer using System.out.println()
            // To debug: System.err.println("Debug messages...");

			if (myFixerId == -1) {
				System.out.println("HIRE 0 0"); // HIRE <x> <y>
				continue;
			}
            if (grid.get(myFixerX).get(myFixerY) > 0) {
                System.out.println("WAIT");
            } else {
				int x = 0;
            	int y = 0;
                for (int i = 0; i < mapWidth; i++) {
                    for (int j = 0; j < mapHeight; j++) {
                        if (grid.get(i).get(j) == bugMax) {
                            x = i;
                            y = j;
                        }
                    }
                }
				System.out.println("MOVE " + myFixerId + " " + x + " " + y); // MOVE <id> <x> <y>
            }
        }
    }
}