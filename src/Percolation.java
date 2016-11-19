/**
 * Created by sole on 11/7/16.
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // create n-by-n grid, with all sites blocked
    private int _n;
    private int _top;
    private int _bottom;
    private Tile[][] _grid;
    private WeightedQuickUnionUF _uf_grid;
    private boolean _percolates;

    public Percolation(int n){
        this._n = n;
        this._grid = new Tile[n][n];
        this._top = n*n;
        this._bottom = n*n+1;
        this._percolates = false;
        this._uf_grid = new WeightedQuickUnionUF(n*n+2);
        Tile temp_tile;

        for (int i = 0; i < n*n; i++){
            temp_tile = new Tile(i);
            this._grid[i/n][i%n] = temp_tile;
        }
        for (int i = 0; i < n; i++) {
            this._uf_grid.union(this._top, this._grid[0][i].getValue());
            this._uf_grid.union(this._bottom, this._grid[this._n - 1][i].getValue());

        }
    }
    // open site (row, col) if it is not open already
    public void open(int row, int col){
        row -= 1;
        col -= 1;

        this._grid[row][col].open();

        checkIndexInBounds(row, col);
        boolean madeAConnection = this.bindToOpenNeighbours(row, col);
        Tile this_tile = this._grid[row][col];
        if (madeAConnection) {
            if (this._uf_grid.connected(this_tile.getValue(), this._top))
                this_tile.fill();
            this._percolates = this._uf_grid.connected(this._top, this._bottom);
        }

    }
    private void checkIndexInBounds(int row, int col){
        if (row < 0 || col < 0 || row >= this._n || col >= this._n)
            throw new IndexOutOfBoundsException();
    }
    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        row -= 1;
        col -= 1;
        checkIndexInBounds(row, col);
        return this._grid[row][col].isOpen();
    }
    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        row -= 1;
        col -= 1;
        checkIndexInBounds(row, col);
        return this._grid[row][col].isFilled();
    }
    // does the system percolate?
    public boolean percolates() {
        return this._percolates;
    }

    private boolean bindToOpenNeighbours(int row, int col){
        // create connections to neighboring open tiles, return true if at least on connection made
        Tile me = this._grid[row][col];
        Tile[] around_me = new Tile[4];
        boolean connected = false;
        around_me[0] = col - 1 >= 0 ? this._grid[row][col-1] : null;
        around_me[1] = col + 1 < this._n ? this._grid[row][col+1] : null;
        around_me[2] = row + 1 < this._n ? this._grid[row+1][col] : null;
        around_me[3] = row - 1 >= 0 ? this._grid[row-1][col] : null;



        for (Tile anAround_me : around_me) {
            if (anAround_me != null && anAround_me.isOpen()) {
                this._uf_grid.union(me.getValue(), anAround_me.getValue());
                connected = true;
            }
        }
        if (this._uf_grid.connected(me.getValue(), this._top))
            me.fill();
        return connected;

    }

//    public String toString(){
//        String result = "";
//        for (int i = 0; i < this._n; i++){
//            for (int j = 0; j < this._n; j++){
//                if (j < this._n-1)
//                    result = result + "," + this._grid[i][j].getState();
//                else
//                    result = result + "," +this._grid[i][j].getState() + "\n";
//            }
//        }
//        return result;
//    }
    // test client (optional)
    public static void main(String[] args){
        int n = 10;
        Percolation percolation = new Percolation(n);
        for (int i = 1; i <= n/2; i++)
            percolation.open(i, 1);
        for (int i = n/2; i <= n;i++)
            percolation.open(i, 2);
        System.out.println(percolation);
        System.out.print(percolation.percolates());

    }
    private class Tile {

        private int _value;
        private State _state;

        public Tile(int value){
            this._value = value;
            this._state = State.BLOCKED;
        }


        public boolean isBlocked(){
            return (this._state == State.BLOCKED);
        }

        public boolean isOpen(){
            return (this._state == State.OPEN || this._state == State.FILLED);
        }

        public boolean isFilled(){
            return (this._state == State.FILLED);
        }


        public void open(){
            this.setState(State.OPEN);
        }

        public void block(){
            this.setState(State.BLOCKED);
        }

        public  void fill(){
            this.setState(State.FILLED);
        }

        public int getValue(){
            return this._value;
        }

        private void setState(State state){
            this._state = state;
        }

        public State getState(){
            return this._state;
        }


    }

    private enum State {
        BLOCKED, OPEN, FILLED
    }


}
