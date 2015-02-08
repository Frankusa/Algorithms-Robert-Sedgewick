public class Percolation {

	private boolean[] openStatus;
	private int gridSize;
	private WeightedQuickUnionUF ufPerco;  //for percolation status check, [0] [N*N+1] are hidden sites
	private WeightedQuickUnionUF ufFull;   //for full status check, [0] is hidden site
	
	// create N-by-N grid, with all sites blocked, OpenStatus[0] maps to UFs[1]
	public Percolation(int N)
	{
		if (N <= 0) throw new IllegalArgumentException();
		this.openStatus = new boolean[N * N];
		this.gridSize = N;
		// add 2 virtual sites
		this.ufPerco = new WeightedQuickUnionUF(N * N + 2);
		this.ufFull = new WeightedQuickUnionUF(N * N + 1);
		for (int i = 0; i < N * N; i++) {
			openStatus[i] = false;
		}
		for (int i = 1; i <= N; i++) {
			ufPerco.union(0, i);
			ufFull.union(0, i);                 //avoid backwash, no bottom sites unioned for UFFull
			ufPerco.union(N*N + 1, N*N +  1- i);
		}
	}

	private void indexCheck(int i, int j) {
		if (i < 1 || i > gridSize) throw new IndexOutOfBoundsException("row index i out of bounds");	
		if (j < 1 || j > gridSize) throw new IndexOutOfBoundsException("col index j out of bounds");	
	}
	
	// open site (row i, column j) if it is not open already, union neighbor
	public void open(int i, int j) {
		indexCheck(i, j); 

		int osIndex = (i-1) * gridSize + j - 1;
		if (openStatus[osIndex]) return;
		openStatus[osIndex] = true;
		
		int ufIndex = osIndex + 1;
				
		// union left open sites
		if (j - 2 >= 0)	{
			if (isOpen(i, j - 1)) {
				ufPerco.union(ufIndex - 1, ufIndex);
				ufFull.union(ufIndex - 1, ufIndex);
			}
		}
		
		// union right open sites
		if (j < gridSize) {
			if (isOpen(i, j + 1)) {
				ufPerco.union(ufIndex,  ufIndex + 1);
				ufFull.union(ufIndex,  ufIndex + 1);
			}
		}

		// union above open sites
		if (i - 2 >= 0) {
			if (isOpen(i-1, j)) {
				ufPerco.union(ufIndex - gridSize, ufIndex);
				ufFull.union(ufIndex - gridSize, ufIndex);
			}
		}

		// union bottom open sites
		if (i < gridSize) {
			if (isOpen(i + 1, j)) {
				ufPerco.union(ufIndex, ufIndex + gridSize);
				ufFull.union(ufIndex, ufIndex + gridSize);
			}
		}
	}

	// is site (row i, column j) open?
	public boolean isOpen(int i, int j)	{
		indexCheck(i, j);
		return (openStatus[(i - 1) * gridSize + j - 1]);
	}

	// is site (row i, column j) full?
	public boolean isFull(int i, int j)	{
		indexCheck(i, j);
		return (isOpen(i, j) && ufFull.connected(0,  (i-1) * gridSize + j));
	}

	// does the system percolate?
	public boolean percolates()	{
		if (gridSize == 1)	return (openStatus[0]);
		return ufPerco.connected(0,  gridSize * gridSize + 1);
	}
}