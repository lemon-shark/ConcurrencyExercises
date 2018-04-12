import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Callable;

public class GameCharacter implements GameElement, Callable<Object> {
    /** Static */

    private static GameGrid gameGrid;

    public static synchronized void setGameGrid(GameGrid gameGrid)
    { GameCharacter.gameGrid = gameGrid; }

    /** Instance */

    public int numMoves;

    private int rowPos, colPos; // current position on gameGrid
    private int rowDest, colDest; // current destination on gameGrid
    private long moveDelay, runTime;
    private boolean verbose;

    public GameCharacter(int rowPos, int colPos, long moveDelay, long runTime) {
        this.rowPos    = rowPos;
        this.colPos    = colPos;
        this.moveDelay = moveDelay;
        this.runTime   = runTime;
        setRandomNearbyDest();
    }

    public GameCharacter(int rowPos, int colPos, long moveDelay, long runTime, boolean verbose) {
        this.rowPos    = rowPos;
        this.colPos    = colPos;
        this.moveDelay = moveDelay;
        this.runTime   = runTime;
        this.verbose = verbose;
        setRandomNearbyDest();
    }

    public int[] getPos() {
        return new int[] {rowPos, colPos};
    }

    @Override
    public Object call() throws Exception {
        boolean didMove;
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                didMove = moveTowardDest();
                Thread.sleep(moveDelay);

                if (System.currentTimeMillis() - startTime > runTime)
                    throw new InterruptedException();

                if (!didMove) {
                    setRandomNearbyDest();
                }
                else if (rowPos == rowDest && colPos == colDest)
                    setRandomNearbyDest();

                numMoves++;
            }
        }
        catch(InterruptedException ie) // if interrupeted, stop running
        { if (verbose) System.out.println("GameCharacter Interrupted"); }

        return null;
    }

    private boolean moveTowardDest() {
        int rowMove = Integer.signum(rowDest - rowPos);
        int colMove = Integer.signum(colDest - colPos);

        boolean didMove = gameGrid.moveGameCharacter(rowPos,colPos, rowPos+rowMove,colPos+colMove);
        if (didMove)
            setPos(new int[] {rowPos+rowMove, colPos+colMove});

        return didMove;
    }

    private void setRandomNearbyDest() {
        int[] rowMinMaxColMinMax = getInboundsNeighbourhood();
        int rowMin = rowMinMaxColMinMax[0];
        int rowMax = rowMinMaxColMinMax[1];
        int colMin = rowMinMaxColMinMax[2];
        int colMax = rowMinMaxColMinMax[3];

        do {
            rowDest = ThreadLocalRandom.current().nextInt(rowMin, rowMax+1);
            colDest = ThreadLocalRandom.current().nextInt(colMin, colMax+1);
        } while ((rowDest == rowPos && colDest == colPos) || gameGrid.isObstacle(rowDest, colDest));
    }

    private void setPos(int[] pos) {
        rowPos = pos[0];
        colPos = pos[1];
    }

    private int[] getInboundsNeighbourhood() {
        int gridSize = gameGrid.size();
        int rowMin = rowPos - 4 >= 0? rowPos - 4: 0;
        int rowMax = rowPos + 4 < gridSize? rowPos + 4: gridSize-1;
        int colMin = colPos - 4 >= 0? colPos - 4: 0;
        int colMax = colPos + 4 < gridSize? colPos + 4: gridSize-1;

        return new int[] {rowMin, rowMax, colMin, colMax};
    }
}
