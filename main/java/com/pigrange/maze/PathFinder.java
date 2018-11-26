package com.pigrange.maze;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    private int[][] map;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public PathFinder(int[][] map, int startY, int startX, int endY, int endX) {
        this.map = map;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public PathNode findPath() {
        PathNode endNode = null;
        List<PathNode> openList = new ArrayList<>();
        List<PathNode> closeList = new ArrayList<>();

        PathNode startNode = new PathNode(startY, startX);
        openList.add(startNode);

        int tryCount = 0;
        while (!openList.isEmpty()) {
            tryCount++;
            PathNode curMinFNode = getMinFPathNode(openList);
            openList.remove(curMinFNode);
            closeList.add(curMinFNode);
            if (curMinFNode.y == endY && curMinFNode.x == endX) {
                endNode = curMinFNode;
                break;
            } else {
                //东
                HandleChildNode(curMinFNode, curMinFNode.y, curMinFNode.x + 1, openList, closeList);
                //南
                HandleChildNode(curMinFNode, curMinFNode.y + 1, curMinFNode.x, openList, closeList);
                //西
                HandleChildNode(curMinFNode, curMinFNode.y, curMinFNode.x - 1, openList, closeList);
                //北
                HandleChildNode(curMinFNode, curMinFNode.y - 1, curMinFNode.x, openList, closeList);
            }
        }
        return endNode;
    }

    private void HandleChildNode(PathNode curMinFNode, int y, int x, List<PathNode> openList, List<PathNode> closeList) {
        PathNode child;
        if (isPass(y, x)) {
            child = new PathNode(y, x);
            if (!isExistList(child, closeList) && !isExistList(child, openList)) {
                child.F = getF(y, x);
                child.fatherNode = curMinFNode;
                openList.add(child);
            }
        }
    }

    private int getF(int y, int x) {
        int g = Math.abs(startX - y) + Math.abs(startX - x);
        int h = Math.abs(endX - y) + Math.abs(endY - x);
        return g + h;
    }

    private boolean isExistList(PathNode child, List<PathNode> closeList) {
        for (PathNode node : closeList) {
            if (node.y == child.y && node.x == child.x) {
                return true;
            }
        }
        return false;
    }

    private boolean isPass(int y, int x) {
        int rowCount = map.length;
        int colCount = map[0].length;

        if (y >= 1 && y < rowCount - 1 && x >= 1 && x < colCount - 1) {
            return map[y][x] != 0;
        } else {
            return false;
        }
    }

    private PathNode getMinFPathNode(List<PathNode> openList) {
        int pos = 0;
        int minF = openList.get(pos).F;
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).F < minF) {
                minF = openList.get(i).F;
                pos = i;
            }
        }
        return openList.get(pos);
    }

    public static class PathNode {
        PathNode(int y, int x) {
            this.x = x;
            this.y = y;
        }

        int y;
        int x;
        int F = Integer.MAX_VALUE;
        PathNode fatherNode;
    }
}
