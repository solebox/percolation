/**
 * Created by sole on 11/8/16.
 */
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] threshHolds;
    private int _trials;
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials){
        this._trials = trials;
        this.threshHolds = new double[trials];
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            int open_counter = 0;
            while (!percolation.percolates()){
                open_counter++;
                int row = StdRandom.uniform(1,n+1);
                int col = StdRandom.uniform(1,n+1);
                percolation.open(row, col);
            }
            this.threshHolds[i] = open_counter/(double)(n*n);
        }
    }
    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(this.threshHolds);
    }
    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(this.threshHolds);
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo(){
        return (mean()-stddev())/Math.sqrt(this._trials);
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return (mean()+stddev())/Math.sqrt(this._trials);
    }
    // test client (described below)
    public static void main(String[] args){
        PercolationStats ps = new PercolationStats(20, 10);
        System.out.println("mean: " + ps.mean());
        System.out.println("stddev: " + ps.stddev());
        System.out.println("confidence: " + "[" + ps.confidenceLo() + "," + ps.confidenceHi() + "]");
    }
}