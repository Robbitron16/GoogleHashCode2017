
public class Slice implements Comparable<Slice> {
	public int row;
	public int col;
	public int eCol;
	public int eRow;
	public int area;
	public int rows;
	public int cols;
	public double dist;
	
	public Slice(int row, int col, int eRow, int eCol) {
		this.row = row;
		this.col = col;
		this.eCol = eCol;
		this.eRow = eRow;
		this.area = Math.abs((eRow - row + 1) * (eCol - col + 1));
		this.rows = eRow - row + 1;
		this.cols = eCol - col + 1;
		this.dist = Math.sqrt(col * col + row * row);
	}
	
	public void recomputeArea() {
		this.rows = eRow - row + 1;
		this.cols = eCol - col + 1;
		this.area = Math.abs((eRow - row + 1) * (eCol - col + 1));
		this.dist = Math.sqrt(col * col + row * row);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Slice)) {
			return false;
		}
		Slice oth = (Slice)other;
		return oth.row == this.row && oth.col == this.col && oth.eRow == this.eRow && oth.eCol == this.eRow;
	}
	
	@Override
	public int compareTo(Slice other) {
		int res = Integer.compare(this.area, other.area);
		if (res != 0) {
			return res;
		} else {
			return Double.compare(this.dist, other.dist);
		}
	}
	
	public String toString() {
		return this.row + " " + this.col + " " + (this.eRow) + " " + (this.eCol) + " size: " + this.area;
	}
}
