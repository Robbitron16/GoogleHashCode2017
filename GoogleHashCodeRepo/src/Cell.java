
public class Cell implements Comparable<Cell> {
	public boolean inSlice;
	public boolean isShroom;
	public int row;
	public int col;
	public double dist;
	
	public Cell(int row, int col, boolean isShroom) {
		this.row = row;
		this.col = col;
		this.isShroom = isShroom;
		this.inSlice = false;
		this.dist = Math.sqrt(row * row + col * col);
	}
	
	public String toString() {
		return this.isShroom ? "M" : "T";
	}
	
	public int compareTo(Cell other) {
		return Double.compare(this.dist, other.dist);
	}
}
