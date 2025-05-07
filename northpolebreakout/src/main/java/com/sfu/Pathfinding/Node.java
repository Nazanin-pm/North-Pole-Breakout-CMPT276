package com.sfu.Pathfinding;

public class Node {

    Node parent;
    private final int column;
    private final int row;
    int gCost;
    int hCost;
    int fCost;
    boolean solid;
    boolean open;
    boolean visited;

    public Node(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
