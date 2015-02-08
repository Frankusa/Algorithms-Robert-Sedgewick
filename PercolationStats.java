public class PercolationStats {
	private double[] percoThreshold;
	private double percoMean;
	private double percoStdDev;
	private int numberOfExperiment;
	
	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T) {
		if (N <= 0  || T <= 0)
			throw new java.lang.IllegalArgumentException();
		
		this.percoThreshold = new double[T];
		this.numberOfExperiment = T;
		
		for (int i = 0; i < T; i++) {
			int count = 0;
			Percolation percolation = new Percolation(N);
			boolean percolateStatus = false;
			
			while (!percolateStatus) {
				int newI = StdRandom.uniform(1, N + 1);
				int newJ = StdRandom.uniform(1, N + 1);
				if (!percolation.isOpen(newI, newJ)) {
					percolation.open(newI,  newJ);
					count++;
					percolateStatus = percolation.percolates();
				}
			}
			percoThreshold[i] = (double) count / (double) (N * N);
		}
		
		this.percoMean = StdStats.mean(percoThreshold);
		this.percoStdDev = StdStats.stddev(percoThreshold);
		
	}
	
	// sample mean of percolation threshold	
	public double mean() {
		return percoMean;
	}
	
	// sample standard deviation of percolation threshold
	public double stddev() {
		return percoStdDev;
	}
	
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return percoMean - 1.96*percoStdDev / Math.sqrt(numberOfExperiment);
    }
   
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return percoMean + 1.96*percoStdDev / Math.sqrt(numberOfExperiment);
    }
    
	// test client, described below
	public static void main(String[] args)  
	{
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		
		PercolationStats percolationStats = new PercolationStats(N, T);
		StdOut.println("mean                    = " + percolationStats.mean());
		StdOut.println("stddev                  = " + percolationStats.stddev());
		StdOut.println("95% confidence interval = " + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi());
	}
}
