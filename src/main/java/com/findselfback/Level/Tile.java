package com.findselfback.Level;

import java.util.Comparator;
import java.util.Objects;

public class Tile implements Comparable<Tile> {
    public int value, depth;
    public Tile(){

    }
    public Tile(int value, int depth){
        this.value = value;
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return value == tile.value || depth == tile.depth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, depth);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "value=" + value +
                ", depth=" + depth +
                '}';
    }

    // Comparator tích hợp trong lớp Tile
    public static final Comparator<Tile> COMPARATOR = new Comparator<Tile>() {
        @Override
        public int compare(Tile t1, Tile t2) {
            // Nếu độ sâu hoặc giá trị trùng lặp, coi chúng là bằng nhau
            if (t1.depth == t2.depth || t1.value == t2.value) {
                return 0;
            }
            // Nếu không, so sánh theo độ sâu
            int depthComparison = Integer.compare(t1.depth, t2.depth);
            if (depthComparison != 0) {
                return depthComparison;
            }
            // So sánh theo giá trị nếu độ sâu bằng nhau (điều này có thể không xảy ra)
            return Integer.compare(t1.value, t2.value);
        }
    };

    @Override
    public int compareTo(Tile o) {
        return COMPARATOR.compare(this, o);
    }
}
