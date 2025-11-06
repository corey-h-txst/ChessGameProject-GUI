package Model;

public class Position {
    public int col;
    public int row;
    public Position(int row, int col) {
        this.col = col;
        this.row = row;
    }

    @Override
    // Helper function that converts position into chess notation
    public String toString() {
        return ("" +  (char)('A' + col) + (8 - row));
    }

    @Override
    // Overrides equals logic to aid in comparison of positions
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;
        Position other = (Position)obj;
        return this.row == other.row && this.col == other.col;
    }

}
