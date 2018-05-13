
public class Solution {
	//二维数组中的查找
	public boolean Find(int target, int[][] array) {
		if (array == null) {
			return false;
		}
		int rows = array.length;
		int columns = array[0].length;
		int currColumn = columns - 1;
		int currRow = 0;
		while (currRow < rows && currColumn >= 0) {
			if (array[currRow][currColumn] == target) {
				return true;
			} else if (array[currRow][currColumn] < target) {
				currRow++;
			} else {
				currColumn--;
			}
		}
		return false;
	}
}
