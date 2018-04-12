import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static final int GRID_SIZE = 30;

    private static int n, p, r, k;
    private static GameGrid gameGrid;
    private static boolean verbose;

    public static void main(String[] args) {
        // command-line args
        try {
            n = Integer.parseInt(args[0]); // number of threads
            p = Integer.parseInt(args[1]); // number of minutes to run program
            r = Integer.parseInt(args[2]); // number of obstacles
            k = Integer.parseInt(args[3]); // baseline moveDelay for gameCharacters (ms)

            if (args.length == 5)
                verbose = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        long timeToRunMillis = TimeUnit.MINUTES.toMillis(p);

        // init gameGrid with obstacles
        gameGrid = GameGrid.getGameGridSingleton(GRID_SIZE, (int)(k * 2.5));
        gameGrid.addObstacles(r);

        // create GameCharacters and add them to the board
        GameCharacter.setGameGrid(gameGrid);
        ArrayList<GameCharacter> gameCharacters = new ArrayList<>(n);
        int[] pos;
        int row, col;
        int delay;
        for (int i = 0; i < n; i++) {
            pos = gameGrid.getUnusedCell();
            row = pos[0];
            col = pos[1];
            delay = k * ThreadLocalRandom.current().nextInt(1, 5);

            gameCharacters.add(i, new GameCharacter(row, col, delay, timeToRunMillis, verbose));
            gameGrid.addGameCharacter(gameCharacters.get(i));
        }

        if (verbose)
            gameGrid.printGrid();

        long startTime = System.currentTimeMillis();

        ExecutorService threadPool = Executors.newFixedThreadPool(n);
        try {
            threadPool.invokeAll(gameCharacters);
            threadPool.shutdown();
        }
        catch (Exception e) { e.printStackTrace(); }

        if (verbose) {
            System.out.println();
            gameGrid.printGrid();
        }

        // print results
        StringBuilder str = new StringBuilder();
        str.append(n+","+p+","+r+","+k+",");
        str.append(gameCharacters.get(0).numMoves);
        for (int i = 1; i < n; i++) {
            str.append("," + gameCharacters.get(i).numMoves);
        }
        System.out.println(str);
    }
}
