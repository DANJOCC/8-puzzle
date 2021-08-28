import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Board {
    private final int[][] board;
    private final int manhattan;
    private final int hamming;
    private int zi, zj;
    private Board twin;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int h = 0;
        int m = 0;
        this.board = new int[tiles.length][tiles.length];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                board[i][j] = tiles[i][j];
            }
        }
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.board[i][j] == 0) {
                    zi = i;
                    zj = j;
                }
                else if ((this.board[i][j] != i * dimension() + j + 1)) {
                    h++;
                    int ti = (this.board[i][j] - 1) / dimension();
                    int tj = (this.board[i][j] - 1) % dimension();
                    m += Math.abs(ti - i) + Math.abs(tj - j);
                }
            }
        }
        hamming = h;
        manhattan = m;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board b = (Board) y;
        return Arrays.deepEquals(this.board, b.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int i = zi;
        int j = zj;
        Queue<Board> b = new LinkedList<>();
        if (i == 0 && (j > 0 && j < dimension() - 1)) {
            b.add(new Board(swap(i, j - 1, i, j)));
            b.add(new Board(swap(i, j + 1, i, j)));
            b.add(new Board(swap(i + 1, j, i, j)));
        }
        else if (i == dimension() - 1 && (j > 0 && j < dimension() - 1)) {
            b.add(new Board(swap(i, j - 1, i, j)));
            b.add(new Board(swap(i, j + 1, i, j)));
            b.add(new Board(swap(i - 1, j, i, j)));
        }
        else if ((j > 0 && j < dimension() - 1) && (i > 0 && i < dimension() - 1)) {
            b.add(new Board(swap(i, j - 1, i, j)));
            b.add(new Board(swap(i, j + 1, i, j)));
            b.add(new Board(swap(i - 1, j, i, j)));
            b.add(new Board(swap(i + 1, j, i, j)));
        }
        else if (j == 0 && (i > 0 && i < dimension() - 1)) {
            b.add(new Board(swap(i - 1, j, i, j)));
            b.add(new Board(swap(i + 1, j, i, j)));
            b.add(new Board(swap(i, j + 1, i, j)));
        }
        else if (j == dimension() - 1 && (i > 0 && i < dimension() - 1)) {
            b.add(new Board(swap(i - 1, j, i, j)));
            b.add(new Board(swap(i + 1, j, i, j)));
            b.add(new Board(swap(i, j - 1, i, j)));
        }
        else if (i == 0 && j == 0) {
            b.add(new Board(swap(i, j + 1, i, j)));
            b.add(new Board(swap(i + 1, j, i, j)));
        }
        else if (i == 0 && j == dimension() - 1) {
            b.add(new Board(swap(i, j - 1, i, j)));
            b.add(new Board(swap(i + 1, j, i, j)));
        }
        else if (i == dimension() - 1 && j == 0) {
            b.add(new Board(swap(i - 1, j, i, j)));
            b.add(new Board(swap(i, j + 1, i, j)));
        }
        else if (i == dimension() - 1 && j == dimension() - 1) {
            b.add(new Board(swap(i, j - 1, i, j)));
            b.add(new Board(swap(i - 1, j, i, j)));
        }
        return b;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (twin == null)
            twin = new Board(getTwin());
        return twin;
    }

    // cambia celdas
    private int[][] swap(int a, int b, int c, int d) {
        int x;
        int[][] y = new int[dimension()][dimension()];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                y[i][j] = board[i][j];
            }
        }
        x = y[a][b];
        y[a][b] = y[c][d];
        y[c][d] = x;
        return y;
    }

    //obtine gemelo
    private int[][] getTwin() {
        int a, b, c, d;
        do {
            a = StdRandom.uniform(0, dimension());
            b = StdRandom.uniform(0, dimension());
            c = StdRandom.uniform(0, dimension());
            d = StdRandom.uniform(0, dimension());
        } while ((a == c && b == d) ||
                (a == zi && b == zj) ||
                (c == zi && d == zj));

        return swap(a, b, c, d);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial);
        StdOut.println(initial.twin());
    }

}
