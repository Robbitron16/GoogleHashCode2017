import java.util.ArrayList;
import java.util.List;

public class Main {
	public static final int HEIGHT = 6;
	public static final int WIDTH = 7;
	public static final int L = 1;
	public static final int S = 5;
	public static void main(String[] args) {
		boolean[][] input = {{false, true, true, true, false, false, false},
							 {true, true, true, true, false, true, true},
							 {false, false, true, false, false, true, false},
							 {false, true, true, false, true, true, true},
							 {false, false, false, false, false, false, true},
							 {false, false, false, false, false, false, true}};
		Cell[][] pizza = new Cell[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				pizza[i][j] = new Cell(i, j, input[i][j]);
			}
		}
		printPizza(pizza, HEIGHT, WIDTH);
		List<Cell> smaller = getSmallerIngredient(pizza);
		List<Slice> slices = generateSlices(pizza, smaller);
		List<Slice> reversed = new ArrayList<Slice>(slices);
		Cell[][] reversedPizza = new Cell[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				Cell elm = pizza[i][j];
				reversedPizza[i][j] = new Cell(elm.row, elm.col, elm.isShroom);
				reversedPizza[i][j].inSlice = elm.inSlice;
			}
		}
		for (int i = 0; i < reversed.size() / 2; i++) {
			Slice temp = reversed.get(i);
			reversed.set(i, reversed.get(reversed.size() - i - 1));
			reversed.set(reversed.size() - 1 - i, temp);
		}
		
		// Compute clockwise slices.
		int size = extendSlices(slices, pizza);
		int lastSize = 0;
		while (size < WIDTH * HEIGHT && lastSize != size) {
			lastSize = size;
			size = extendSlices(slices, pizza);
		}
		
		// Compute counter-clockwise slices
		int reversedSize = extendSlices(reversed, reversedPizza);
		lastSize = 0;
		while (reversedSize < WIDTH * HEIGHT && lastSize != reversedSize) {
			lastSize = reversedSize;
			reversedSize = extendSlices(reversed, reversedPizza);
		}
		
		List<Slice> finalList;
		if (reversedSize > size) {
			finalList = reversed;
		} else {
			finalList = slices;
		}
		System.out.println(finalList.size() + " slices");
		for (Slice slice : finalList) {
			System.out.println(slice);
		}
		System.out.println("Score of " + Math.max(size, reversedSize) + " out of " + WIDTH * HEIGHT);	
	}
	
	public static List<Slice> generateSlices(Cell[][] pizza, List<Cell> smaller) {
		List<Slice> slices = new ArrayList<Slice>();
		for (Cell cell : smaller) {
			// Left, top, right, bot
			if (cell.col > 0 && !pizza[cell.row][cell.col - 1].inSlice && !pizza[cell.row][cell.col - 1].isShroom) {
				slices.add(new Slice(cell.row, cell.col - 1, cell.row, cell.col));
				pizza[cell.row][cell.col].inSlice = true;
				pizza[cell.row][cell.col - 1].inSlice = true;
			} else if (cell.row > 0 && !pizza[cell.row - 1][cell.col].inSlice && !pizza[cell.row - 1][cell.col].isShroom) {
				slices.add(new Slice(cell.row - 1, cell.col, cell.row, cell.col));
				pizza[cell.row][cell.col].inSlice = true;
				pizza[cell.row - 1][cell.col].inSlice = true;
			} else if (cell.col < WIDTH - 1 && !pizza[cell.row][cell.col + 1].inSlice && !pizza[cell.row][cell.col + 1].isShroom) {
				slices.add(new Slice(cell.row, cell.col, cell.row, cell.col + 1));
				pizza[cell.row][cell.col].inSlice = true;
				pizza[cell.row][cell.col + 1].inSlice = true;
			} else if (cell.row < HEIGHT - 1 && !pizza[cell.row + 1][cell.col].inSlice && !pizza[cell.row + 1][cell.col].isShroom) {
				slices.add(new Slice(cell.row, cell.col, cell.row + 1, cell.col));
				pizza[cell.row][cell.col].inSlice = true;
				pizza[cell.row + 1][cell.col].inSlice = true;
			}
		}
		return slices;
	}
	
	public static int extendSlices(List<Slice> slices, Cell[][] pizza) {
		int size = 0;
		for (Slice slice : slices) {
			if (slice.area < S) {
				boolean okToAdd;
				// Do left expansion
				if (slice.area + slice.rows <= S && slice.col > 0) {
					okToAdd = true;
					for (int i = slice.row; i < slice.row + slice.rows; i++) {
						if (pizza[i][slice.col - 1].inSlice)
							okToAdd = false;
					}
					if (okToAdd) {
						slice.col--;
						for (int i = slice.row; i < slice.row + slice.rows; i++) {
							pizza[i][slice.col].inSlice = true;
						}
						slice.recomputeArea();
					}
				}
				// Do right expansion.
				if (slice.area + slice.rows <= S && slice.eCol < WIDTH - 1) {
					okToAdd = true;
					for (int i = slice.row; i < slice.row + slice.rows; i++) {
						if (pizza[i][slice.eCol + 1].inSlice)
							okToAdd = false;
					}
					if (okToAdd) {
						slice.eCol++;
						for (int i = slice.row; i < slice.row + slice.rows; i++) {
							pizza[i][slice.eCol].inSlice = true;
						}
						slice.recomputeArea();
					}
				}
				// Do top expansion
				if (slice.area + slice.cols <= S && slice.row > 0) {
					okToAdd = true;
					for (int i = slice.col; i < slice.col + slice.cols; i++) {
						if (pizza[slice.row - 1][i].inSlice)
							okToAdd = false;
					}
					if (okToAdd) {
						slice.row--;
						for (int i = slice.col; i < slice.col + slice.cols; i++) {
							pizza[slice.row][i].inSlice = true;
						}
						slice.recomputeArea();
					}
				}
				// Do bottom expansion
				if (slice.area + slice.cols <= S && slice.eRow < HEIGHT - 1) {
					okToAdd = true;
					for (int i = slice.col; i < slice.col + slice.cols; i++) {
						if (pizza[slice.eRow + 1][i].inSlice)
							okToAdd = false;
					}
					if (okToAdd) {
						slice.eRow++;
						for (int i = slice.col; i < slice.col + slice.cols; i++) {
							pizza[slice.eRow][i].inSlice = true;
						}
						slice.recomputeArea();
					}
				}
			}
			size += slice.area;
		}
		return size;
	}
	
	public static List<Cell> getSmallerIngredient(Cell[][] pizza) {
		List<Cell> shrooms = new ArrayList<Cell>();
		List<Cell> tomatoes = new ArrayList<Cell>();
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (pizza[i][j].isShroom)
					shrooms.add(pizza[i][j]);
				else
					tomatoes.add(pizza[i][j]);
			}
		}
		return shrooms.size() > tomatoes.size() ? tomatoes : shrooms;
	}
	
	public static void printPizza(Cell[][] pizza, int h, int w) {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				System.out.print(pizza[i][j]);
			}
			System.out.println();
		}
	}
	
	private static class Cell {
		public boolean inSlice;
		public boolean isShroom;
		public int row;
		public int col;
		
		public Cell(int row, int col, boolean isShroom) {
			this.row = row;
			this.col = col;
			this.isShroom = isShroom;
			this.inSlice = false;
		}
		
		public String toString() {
			return this.isShroom ? "M" : "T";
		}
	}
	
	private static class Slice {
		public int row;
		public int col;
		public int eCol;
		public int eRow;
		public int area;
		public int rows;
		public int cols;
		
		public Slice(int row, int col, int eRow, int eCol) {
			this.row = row;
			this.col = col;
			this.eCol = eCol;
			this.eRow = eRow;
			this.area = Math.abs((eRow - row + 1) * (eCol - col + 1));
			this.rows = eRow - row + 1;
			this.cols = eCol - col + 1;
		}
		
		public void recomputeArea() {
			this.rows = eRow - row + 1;
			this.cols = eCol - col + 1;
			this.area = Math.abs((eRow - row + 1) * (eCol - col + 1));
		}
		
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Slice)) {
				return false;
			}
			Slice oth = (Slice)other;
			return oth.row == this.row && oth.col == this.col && oth.eRow == this.eRow && oth.eCol == this.eRow;
		}
		
		public String toString() {
			return this.row + " " + this.col + " " + (this.eRow) + " " + (this.eCol) + " size: " + this.area;
		}
	}
}

