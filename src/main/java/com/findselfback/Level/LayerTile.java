package com.findselfback.Level;

import lombok.Data;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

@Data
public class LayerTile {
    private TreeSet<Tile> tileTreeSet;
    Comparator<Tile> comparator = new Comparator<Tile>() {
        @Override
        public int compare(Tile t1, Tile t2) {
            int depthComparison = Integer.compare(t1.depth, t2.depth);
            if (depthComparison != 0) {
                return depthComparison;
            }
            return Integer.compare(t1.value, t2.value);
        }
    };
    public LayerTile(){
        tileTreeSet = new TreeSet<Tile>(Tile.COMPARATOR);
    }
    public void add(int tileValue, int depth){
        tileTreeSet.add(new Tile(tileValue,depth));
    }

    public boolean remove(int depth){
        Iterator<Tile> iterator = tileTreeSet.iterator();
        while (iterator.hasNext()){
            Tile currentTile = iterator.next();
            if(currentTile.depth == depth){
                tileTreeSet.remove(currentTile);
                return true;
            }
        }
        return false;
    }

    public int lastDepth(){
        Tile lastTile = tileTreeSet.last();
        return lastTile.depth;
    }

    public void removeAll(){
        tileTreeSet.removeAll(tileTreeSet);
    }
}