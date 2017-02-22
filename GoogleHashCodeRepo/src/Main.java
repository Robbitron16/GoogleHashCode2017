import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
	public static int HEIGHT;
	public static int WIDTH;
	public static int L;
	public static int H;
	public static final String FILENAME = "small.in";
	
	public static void main(String[] args) throws FileNotFoundException {
		int[] stats = new int[4];
		Cell[][] pizza = Parser.parseFile(FILENAME, stats);
		HEIGHT = stats[0]; WIDTH = stats[1]; L = stats[2]; H = stats[3];
		List<Cell> lessFrequent = getSmallerIngredient(pizza);
		Collections.sort(lessFrequent);
		List<Slice> starters = generateSlices(pizza, lessFrequent);
		
		// Extend the slices:
		int size = 0;
		int lastSize = 0;
		for (Slice slice : starters) {
			size += slice.area;
		}
		while (lastSize != size && size < HEIGHT * WIDTH) {
			lastSize = size;
			size = extendSlices(starters, pizza);
		}
		
		//printPizza(pizza, HEIGHT, WIDTH);
		System.out.println(starters.size() + " slices");
		for (Slice slice : starters) {
			System.out.println(slice);
		}
		System.out.println("Score of " + size + " out of " + WIDTH * HEIGHT);
	}
	
	public static List<Slice> generateSlices(Cell[][] pizza, List<Cell> smaller) {
		List<Slice> slices = new ArrayList<Slice>();
		Set<String> dims = getBlockSizes();
		for (Cell cell : smaller) {
			// If it's already been sliced, skip it:
			// This will happen a lot depending on L.
			if (cell.inSlice)
				continue;
			
			for (String dim : dims) {
				// In case one of the dimensions worked.
				// Try first way and see if it fits (ie 1x2)
				int dimHeight = Integer.parseInt(dim.substring(0, 1));
				int dimWidth = Integer.parseInt(dim.substring(2));
				// Init the dimensions. Have to swap them if the second case happens.
				int col = Math.max(0, cell.col - dimWidth);
				int row = Math.max(0, cell.row - dimHeight);
				int eCol = Math.min(WIDTH - 1, col + dimWidth - 1);
				int eRow = Math.min(HEIGHT - 1, row + dimHeight - 1);
				if (dimWidth <= eCol - col + 1 && dimHeight <= eRow - row + 1) {
					for (int i = row; i <= cell.row && eRow < HEIGHT; i++) {
						for (int j = col; j <= cell.col && eCol < WIDTH; j++) {
							if (verifyProposedSlice(pizza, i, j, eRow, eCol)) {
								slices.add(new Slice(i, j, eRow, eCol));
								//extendSlices(slices, pizza);
								eRow = HEIGHT;
								eCol = WIDTH;
							} else {
								eCol++;
							}
						}
						eRow++;
						eCol = Math.min(WIDTH - 1, col + dimWidth - 1);
					}
				}
				if (cell.inSlice)
					break;
				
				// Now try the 2x1 case.
				int temp = dimHeight;
				dimHeight = dimWidth;
				dimWidth = temp;
				col = Math.max(0, cell.col - dimWidth);
				row = Math.max(0, cell.row - dimHeight);
				eCol = Math.min(WIDTH - 1, col + dimWidth - 1);
				eRow = Math.min(HEIGHT - 1, row + dimHeight - 1);
				if (dimWidth <= eCol - col + 1 && dimHeight <= eRow - row + 1) {
					for (int i = row; i <= cell.row && eRow < HEIGHT; i++) {
						for (int j = col; j <= cell.col && eCol < WIDTH; j++) {
							if (verifyProposedSlice(pizza, i, j, eRow, eCol)) {
								slices.add(new Slice(i, j, eRow, eCol));
								//extendSlices(slices, pizza);
								eRow = HEIGHT;
								eCol = WIDTH;
							} else {
								eCol++;
							}
						}
						eRow++;
						eCol = Math.min(WIDTH - 1, cell.col + dimWidth - 1);
					}
				}
				if (cell.inSlice)
					break;
			}
		}
		return slices;
	}
	
	public static boolean verifyProposedSlice(Cell[][] pizza, int row, int col, int eRow, int eCol) {
		int shroomCount = 0, tomatoCount = 0;
		for (int i = row; i <= eRow; i++) {
			for (int j = col; j <= eCol; j++) {
				Cell elm = pizza[i][j];
				if (elm.inSlice)
					return false;
				if (elm.isShroom) {
					shroomCount++;
				} else {
					tomatoCount++;
				}
			}
		}
		if (shroomCount >= L && tomatoCount >= L && shroomCount + tomatoCount <= H) {
			for (int i = row; i <= eRow; i++) {
				for (int j = col; j <= eCol; j++) {
					pizza[i][j].inSlice = true;
				}
			}
			return true;
		}
		return false;
	}
	
	public static Set<String> getBlockSizes() {
		Set<String> dims = new HashSet<String>();
		switch (L) {
		case 1:
			// L = 1, H = 5
			dims.add("1x2");
			dims.add("1x3");
			dims.add("1x4");
			dims.add("1x5");
			dims.add("2x2");
			break;
		case 4:
			// L = 4, H = 12 
			dims.add("1x8");
			dims.add("1x9");
			dims.add("1x10");
			dims.add("1x11");
			dims.add("1x12");
			dims.add("2x4");
			dims.add("2x5");
			dims.add("2x6");
			dims.add("3x3");
			dims.add("3x4");
			break;
		case 6:
			// L = 6, H = 14
			dims.add("1x12");
			dims.add("1x13");
			dims.add("1x14");
			dims.add("2x6");
			dims.add("2x7");
			dims.add("3x4");
			break;
		}
		return dims;	
	}
	
	public static int extendSlices(List<Slice> slices, Cell[][] pizza) {
		int size = 0;
		for (Slice slice : slices) {
			if (slice.area < H) {
				boolean okToAdd;
				// Do left expansion
				if (slice.area + slice.rows <= H && slice.col > 0) {
					okToAdd = true;
					for (int i = slice.row; i <= slice.eRow; i++) {
						if (pizza[i][slice.col - 1].inSlice)
							okToAdd = false;
					}
					if (okToAdd) {
						slice.col--;
						for (int i = slice.row; i <= slice.eRow; i++) {
							pizza[i][slice.col].inSlice = true;
						}
						slice.recomputeArea();
					}
				}
				// Do bottom expansion
				if (slice.area + slice.cols <= H && slice.eRow < HEIGHT - 1) {
					okToAdd = true;
					for (int i = slice.col; i <= slice.eCol; i++) {
						if (pizza[slice.eRow + 1][i].inSlice)
							okToAdd = false;
					}
					if (okToAdd) {
						slice.eRow++;
						for (int i = slice.col; i <= slice.eCol; i++) {
							pizza[slice.eRow][i].inSlice = true;
						}
						slice.recomputeArea();
					}
				}
				// Do right expansion.
				if (slice.area + slice.rows <= H && slice.eCol < WIDTH - 1) {
					okToAdd = true;
					for (int i = slice.row; i <= slice.eRow; i++) {
						if (pizza[i][slice.eCol + 1].inSlice)
							okToAdd = false;
					}
					if (okToAdd) {
						slice.eCol++;
						for (int i = slice.row; i <= slice.eRow; i++) {
							pizza[i][slice.eCol].inSlice = true;
						}
						slice.recomputeArea();
					}
				}
				// Do top expansion
				if (slice.area + slice.cols <= H && slice.row > 0) {
					okToAdd = true;
					for (int i = slice.col; i <= slice.eCol; i++) {
						if (pizza[slice.row - 1][i].inSlice)
							okToAdd = false;
					}
					if (okToAdd) {
						slice.row--;
						for (int i = slice.col; i <= slice.eCol; i++) {
							pizza[slice.row][i].inSlice = true;
						}
						slice.recomputeArea();
					}
				}
			}
			size += slice.area;
		}
		Collections.sort(slices);
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
}

