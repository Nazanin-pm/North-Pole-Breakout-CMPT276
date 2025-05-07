package com.sfu.Pathfinding;

import com.sfu.Game;

import java.util.ArrayList;

public class Pathfinder {

    Game game;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean reached = false;
    int step = 0;

    int considerXColumns = 7;
    int considerYRows = 6;

    public Pathfinder(Game game) {
        this.game = game;
        instantiateNodes();
    }

    private void instantiateNodes(){

        node = new Node[game.getRows()][game.getColumns()];

        for(int row = 0; row < game.getRows(); row++){
            for(int column = 0; column < game.getColumns(); column++){
                node[row][column] = new Node(row, column);
            }
        }
    }

    public void resetNodes(int bottomNodeIndex, int topNodeIndex, int leftNodeIndex, int rightNodeIndex){

        for(int row = bottomNodeIndex; row <= topNodeIndex; row++) {
            for (int column = leftNodeIndex; column <= rightNodeIndex; column++) {
                node[row][column].open = false;
                node[row][column].visited = false;
                node[row][column].solid = false;
            }
        }

        openList.clear();
        pathList.clear();
        reached = false;
        step = 0;
    }

    public void setNodes(int startColumn, int startRow, int goalColumn, int goalRow){

        // these indexes can possibly < 0 or > rows/columns, but since there are barrier walls
        // enemies will always be 5 rows away from the edge and 10 columns away from the edge
        int bottomNodeIndex = startRow - considerYRows;
        int topNodeIndex = startRow + considerYRows;
        int leftNodeIndex = startColumn - considerXColumns;
        int rightNodeIndex = startColumn + considerXColumns;

        if (bottomNodeIndex < 0){
            bottomNodeIndex = 0;
        }

        if (leftNodeIndex < 0){
            leftNodeIndex = 0;
        }

        if (rightNodeIndex >= game.getColumns()){
            rightNodeIndex = game.getColumns() - 1;
        }

        if (topNodeIndex >= game.getRows()){
            topNodeIndex = game.getRows() - 1;
        }



        resetNodes(bottomNodeIndex, topNodeIndex, leftNodeIndex, rightNodeIndex);

        startNode = node[startRow][startColumn];
        currentNode = startNode;
        goalNode = node[goalRow][goalColumn];
        openList.add(startNode);



        for(int row = bottomNodeIndex; row <= topNodeIndex; row++){
            for(int column = leftNodeIndex; column < rightNodeIndex; column++){
                node[row][column].solid = game.locationNotTraversable(row, column);
                setCost(node[row][column]);
            }
        }
    }

    private void setCost(Node node) {

        int xDistance = Math.abs(node.getColumn() - startNode.getColumn());
        int yDistance = Math.abs(node.getRow() - startNode.getRow());
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.getColumn() - goalNode.getColumn());
        yDistance = Math.abs(node.getRow() - goalNode.getRow());
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;
    }

    public boolean search(){

        while(!reached && step < 500){

            int column = currentNode.getColumn();
            int row = currentNode.getRow();

            currentNode.visited = true;
            openList.remove(currentNode);

            if(row - 1 >= 0){
                openNode(node[row-1][column]);
            }
            if(column - 1 >= 0){
                openNode(node[row][column-1]);
            }
            if(row + 1 < game.getRows()){
                openNode(node[row+1][column]);
            }
            if(column + 1 < game.getColumns()){
                openNode(node[row][column+1]);
            }

            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for(int i = 0; i < openList.size(); i++){
                Node node = openList.get(i);
                if(node.fCost < bestNodeFCost){
                    bestNodeIndex = i;
                    bestNodeFCost = node.fCost;
                }
                else if(node.fCost == bestNodeFCost){
                    if(node.gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }

            if(openList.isEmpty()){
                break;
            }

            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode){
                reached = true;
                trackThePath();
            }
            step++;
        }
        return reached;
    }

    private void openNode(Node node){

        if(!node.open && !node.visited && !node.solid){
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath() {
        Node current = goalNode;
        while(current != startNode){
            pathList.add(0,current);
            current = current.parent;
        }
    }

    public ArrayList<Node> getPathList() {
        return pathList;
    }
}
