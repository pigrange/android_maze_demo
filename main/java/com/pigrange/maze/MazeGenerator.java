package com.pigrange.maze;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class MazeGenerator {
    private int size;
    private final int ROAD = 1;
    private int maze[][];    //二维数组的迷宫
    private List<Node> mazeRef = new ArrayList<>();    //迷宫的映射列表

    public MazeGenerator(int size) {
        this.size = size;
    }

    public int[][] getMazeArray() {
        if (maze != null) {
            return maze;
        }
        maze = new int[size][size];
        initMazeRef();
        generateMaze();
        return maze;
    }

    public void refresh() {
        mazeRef.clear();
        maze = null;
    }

    //上下左右移动
    private Node move[] = {new Node(-1, 0), new Node(0, 1), new Node(1, 0), new Node(0, -1)};

    //随机得到一个0到t-1的整数
    private int rand(int num) {
        return (int) (Math.random() * num);
    }

    //初始化迷宫的映射列表
    private void initMazeRef() {
        for (int y = 0; y < size / 2; y++) {
            for (int x = 0; x < size / 2; x++) {
                mazeRef.add(new Node(y, x));
            }
        }
    }

    //选择列表中的一个随机的点
    private Node getRandNode() {
        int temp = rand(mazeRef.size());
        return mazeRef.get(temp);
    }

    //寻找可以移动的方向
    private int findWay(Node node) {
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < 4; i++) {

            int toX = node.x + move[i].x;
            int toY = node.y + move[i].y;

            if (toX < 0 || toX >= size / 2 || toY < 0 || toY >= size / 2) {
                continue;
            }
            if (maze[toY * 2 + 1][toX * 2 + 1] == ROAD) {
                continue;
            }
            temp.add(i);
        }

        //如果没有可以移动的方向，则判定为死点，将其四周全部打通
        if (temp.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                int tempX = node.x * 2 + 1 + move[i].x;
                int tempY = node.y * 2 + 1 + move[i].y;

                if (tempX > 0 && tempX < size - 1 && tempY > 0 && tempY < size - 1) {
                    maze[tempY][tempX] = ROAD;
                }
            }

            for (int i = 0; i <= 1; i++) {
                for (int j = 0; j <= 1; j++) {
                    int tempY = node.x * 2 + 1 + i;
                    int tempX = node.y * 2 + 1 + j;

                    if (tempY > 0 && tempY < size - 1 && tempX > 0 && tempX < size - 1) {
                        maze[tempY][tempX] = ROAD;
                    }
                }
            }
            return -1;
        } else {
            return temp.get(rand(temp.size())); //返回可走的方向中的随机一个
        }
    }

    //去掉某一个节点
    private void removeNode(Node node) {
        int left = -1, right = mazeRef.size();
        while (right - left > 1) {
            int temp = (left + right) / 2;

            //先用二分法找到node的行，再用二分发找到node存在的列
            if (mazeRef.get(temp).y > node.y || (mazeRef.get(temp).y == node.y && mazeRef.get(temp).x > node.x)) {
                right = temp;
            } else {
                left = temp;
            }
        }
        mazeRef.remove(left);
    }

    //生成迷宫
    private void generateMaze() {
        while (!mazeRef.isEmpty()) {

            Node t = getRandNode();
            Queue<Node> queue = new PriorityQueue<>();
            queue.add(t);

            while (!queue.isEmpty()) {
                Node tempNode = queue.remove();
                removeNode(tempNode);
                maze[tempNode.y * 2 + 1][tempNode.x * 2 + 1] = ROAD;

                int vocation = findWay(tempNode);
                if (vocation == -1) {
                    break;
                }

                Node direct = move[vocation];
                int x = tempNode.x * 2 + direct.x + 1;
                int y = tempNode.y * 2 + direct.y + 1;

                maze[y][x] = ROAD;
                queue.add(new Node(tempNode.y + direct.y, tempNode.x + direct.x));
            }
        }
    }


    private static class Node {
        int x, y;

        Node(int y, int x) {
            this.x = x;
            this.y = y;
        }
    }
}
