package puzzles.common;

/**
 * Used to represent a Coordinate (row, col) which is like an immutable
 * dataclass in Python.
 *
 * @author RIT CSdds
 *
 * coord1: Coordinate[row=1, col=4]
 * row: 1, col: 4
 * coord2: Coordinate[row=3, col=4]
 * coord3: Coordinate[row=1, col=4]
 * coord1.equals(coord2)? false
 * coord3.equals(coord1)? true
 * coord1 hashCode: 35
 * coord2 hashCode: 97
 * coord3 hashCode: 35
 *
 * @param row the row
 * @param col the col
 */
public record Coordinate(int row, int col) {

    /**
     * Usage demonstration.
     * @param args not used
     */
    public static void main(String[] args) {
        Coordinate coord1 = new Coordinate(1, 4);
        System.out.println("coord1: " + coord1);
        int r = coord1.row();
        int c = coord1.col();
        System.out.println("row: " + r + ", col: " + c);

        Coordinate coord2 = new Coordinate(3, 4);
        Coordinate coord3 = new Coordinate(1, 4);
        System.out.println("coord2: " + coord2);
        System.out.println("coord3: " + coord3);

        System.out.println("coord1.equals(coord2): " + coord1.equals(coord2));
        System.out.println("coord3.equals(coord1): " + coord3.equals(coord1));

        System.out.println("coord1 hashCode: " + coord1.hashCode());
        System.out.println("coord2 hashCode: " + coord2.hashCode());
        System.out.println("coord3 hashCode: " + coord3.hashCode());
    }
}

