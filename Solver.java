/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public class Solver {
    private final int moves;
    private Node finalnode;
    private boolean solveable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        int m = 0;
        Board actualBoard = initial;
        Board twinActualBoard = initial.twin();
        Board last = null;
        Board twinLast = null;


        Node node = new Node(null, 0, actualBoard);
        Node twinnode = new Node(null, 0, twinActualBoard);
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> twinpq = new MinPQ<>();
        while (!actualBoard.isGoal() && !twinActualBoard.isGoal()) {
            m = node.moves + 1;
            Iterable<Board> nextBoards = actualBoard.neighbors();
            Iterable<Board> twinnextBoards = actualBoard.neighbors();
            for (Board boards : nextBoards) {
                if (!boards.equals(last)) {
                    pq.insert(new Node(node, m, boards));
                }
            }
            for (Board boards : twinnextBoards) {
                if (!boards.equals(twinLast)) {

                    twinpq.insert(new Node(twinnode, m, boards));
                }
            }

            node = pq.delMin();
            last = node.last.now;
            actualBoard = node.now;
            twinnode = twinpq.delMin();
            twinLast = twinnode.last.now;
            twinActualBoard = twinnode.now;
        }
        moves = m;
        if (!actualBoard.isGoal() && twinActualBoard.isGoal())
            solveable = false;
        else {
            finalnode = node;
            solveable = true;
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solveable;

    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solveable)
            return moves;
        else
            return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solveable)
            return null;
        if (finalnode == null)
            return null;
        Node node = finalnode;
        Stack<Board> solution = new Stack<>();
        solution.push(finalnode.now);
        while (node != null) {
            solution.push(node.now);
            node = node.last;
        }
        return solution;
    }

    // clase nodo
    private class Node implements Comparable<Node> {
        private final Node last;
        private final Board now;
        private final int priority;
        private final int moves;

        public Node(Node last, int move, Board now) {
            this.last = last;
            this.now = now;
            this.moves = move;
            this.priority = moves + now.manhattan();
        }

        public int compareTo(Node that) {
            return Integer.compare(this.priority - that.priority, 0);
        }
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
