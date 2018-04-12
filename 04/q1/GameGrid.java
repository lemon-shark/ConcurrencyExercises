import java.util.concurrent.ThreadLocalRandom;

public class GameGrid {
    /** Static */

    private static GameGrid gameGrid;

    public static synchronized GameGrid getGameGridSingleton(int gridSize, int sleepTime) {
        if (gameGrid != null)
            return gameGrid;

        gameGrid = new GameGrid(gridSize, sleepTime);
        return gameGrid;
    }

    /** Instance */

    private final int SLEEP_TIME;

    private GameGridCell[][] gameGridCells;

    private GameGrid(int gridSize, int sleepTime) {
        this.SLEEP_TIME = sleepTime;

        gameGridCells = new GameGridCell[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++)
            for (int j = 0; j < gridSize; j++)
                gameGridCells[i][j] = new GameGridCell();
    }

    public void addObstacles(int numObstacles) {
        int size = size();
        int[] pos;
        int row, col;

        for (int i = 0; i < numObstacles; i++) {
            pos = getUnusedCell();
            row = pos[0];
            col = pos[1];

            gameGridCells[row][col].occupant = new GameObstacle();
        }
    }

    // return int[]{row, col} of some unused cell in gameGrid
    public int[] getUnusedCell() {
        int size = size();
        int row, col;

        do {
            row = ThreadLocalRandom.current().nextInt(0,size);
            col = ThreadLocalRandom.current().nextInt(0,size);
        } while (gameGridCells[row][col].occupant != null);

        return new int[] {row, col};
    }

    public boolean addGameCharacter(GameCharacter gameCharacter) {
        int[] pos = gameCharacter.getPos();
        int row = pos[0];
        int col = pos[1];

        if (gameGridCells[row][col].occupant != null)
            return false;

        gameGridCells[row][col].occupant = gameCharacter;
        return true;
    }

    // true if move successful else false
    public boolean moveGameCharacter(int rowFrom, int colFrom, int rowTo, int colTo) {
        GameGridCell cellFrom = gameGridCells[rowFrom][colFrom];
        GameGridCell cellTo = gameGridCells[rowTo][colTo];

        cellFrom.lock();
        try {
            if (cellTo.tryLock() ) {
                try {
                    if (cellTo.occupant != null)
                        return false;

                    // do move
                    GameElement gameCharacter = cellFrom.occupant;
                    cellFrom.occupant = null;
                    cellTo.occupant = gameCharacter;

                    return true;
                }
                finally {
                    cellTo.unlock();
                }
            }
            else {
                return false;
            }
        }
        finally {
            cellFrom.unlock();
        }
    }

    public boolean isObstacle(int row, int col) {
        if (gameGridCells[row][col].occupant instanceof GameObstacle)
            return true;
        return false;
    }

    public int size() {
        return gameGridCells.length;
    }

    /** Misc Utilities */

    public void printGrid() {
        StringBuilder str = new StringBuilder();
        int size = size();

        str.append("   ");
        for (int i = 0; i < size; i++)
            str.append(twoDigitNumber(i) + " ");
        str.append("\n");
        for (int i = 0; i < size; i++) {
            str.append(twoDigitNumber(i) + " ");
            for (int j = 0; j < size; j++) {
                GameElement occupant = gameGridCells[i][j].occupant;
                if (occupant instanceof GameCharacter)
                    str.append("😃");
                else if (occupant instanceof GameObstacle)
                    str.append("◼");
                else // occupant == null
                    str.append("□");

                str.append("  ");
            }
            str.append("\n");
        }

        System.out.println(str);
    }

    private String twoDigitNumber(int n) {
        if (n < 10)
            return " " + n;
        else
            return String.valueOf(n);
    }
}
