/* *****************************************************************************
 *  name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // the size of the grid
    private final int n;

    // virtual top site
    private final int virtualTop;

    // virtual bottom site
    private final int virtualBottom;

    // open sites number
    private int openSites;

    // the n-by-n grid, to modle the percolation system
    private boolean[] isOpen;

    // using UF to solve this problem
    private final WeightedQuickUnionUF modelUF;
    private final WeightedQuickUnionUF fullUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n need to > 0");
        }

        this.n = n;

        // all sites initially blocked, so openSites is 0
        openSites = 0;

        virtualTop = 0;
        virtualBottom = n * n + 1;

        // all sites initially blocked
        isOpen = new boolean[n * n + 1];

        // n * n sites and two virtual sites
        modelUF = new WeightedQuickUnionUF(n * n + 2);

        // another UF to find whether the site is full
        fullUF = new WeightedQuickUnionUF(n * n + 1);
    }

    // if row and col is valid
    private void checkRange(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException("the row or col is out of Range");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRange(row, col);

        int ID = to1D(row, col);

        // if is open already
        if (isOpen[ID]) {
            return;
        }

        // open the site
        isOpen[ID] = true;

        if (col > 1 && isOpen[ID - 1]) {
            fullUF.union(ID, ID - 1);
            modelUF.union(ID, ID - 1);
        }
        if (col < n && isOpen[ID + 1]) {
            fullUF.union(ID, ID + 1);
            modelUF.union(ID, ID + 1);
        }

        if (row == 1) {
            fullUF.union(ID, virtualTop);
            modelUF.union(ID, virtualTop);
        }
        else if (row > 1 && isOpen[ID - n]) {
            fullUF.union(ID, ID - n);
            modelUF.union(ID, ID - n);
        }

        if (row == n) {
            modelUF.union(ID, virtualBottom);
        }
        else if (row < n && isOpen[ID + n]) {
            fullUF.union(ID, ID + n);
            modelUF.union(ID, ID + n);
        }

        ++openSites;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return isOpen[to1D(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRange(row, col);
        return fullUF.find(virtualTop) == fullUF.find(to1D(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return modelUF.find(virtualTop) == modelUF.find(virtualBottom);
    }

    // get UF ID from the site (row, col)
    private int to1D(int row, int col) {
        return n * (row - 1) + col;
    }

}
