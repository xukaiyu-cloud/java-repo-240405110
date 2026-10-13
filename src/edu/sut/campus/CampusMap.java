package edu.sut.campus;

/**
 * 校园地图类：保存校园中的所有地点以及地点之间的道路。
 *
 * 数据结构说明（按实验要求不使用集合框架，全部用数组实现）：
 *   locations[]  —— 保存所有地点对象；
 *   distance[][] —— 邻接矩阵，distance[i][j] 表示地点 i 到地点 j
 *                   的道路长度（米），-1 表示两地之间没有道路。
 *
 * 查询两点间最短路线使用 Dijkstra 算法（数组实现）。
 */
public class CampusMap {

    private final Location[] locations;
    private final double[][] distance;
    private int locationCount;

    public CampusMap(int capacity) {
        locations = new Location[capacity];
        distance = new double[capacity][capacity];
        for (int i = 0; i < capacity; i++) {
            for (int j = 0; j < capacity; j++) {
                distance[i][j] = -1;
            }
        }
        locationCount = 0;
    }

    /** 添加一个地点，返回该地点的下标。 */
    public int addLocation(String name, String description) {
        locations[locationCount] = new Location(locationCount, name, description);
        locationCount++;
        return locationCount - 1;
    }

    /** 按名称查找地点下标，找不到（或名称为空）返回 -1。 */
    public int findLocationIndex(String name) {
        if (name == null) {
            return -1;
        }
        String key = name.trim();
        for (int i = 0; i < locationCount; i++) {
            if (locations[i].getName().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    /** 按下标获取地点。 */
    public Location getLocation(int index) {
        return locations[index];
    }

    /** 按名称获取地点，找不到返回 null。 */
    public Location getLocationByName(String name) {
        int index = findLocationIndex(name);
        return index < 0 ? null : locations[index];
    }

    /** 获取地点总数。 */
    public int getLocationCount() {
        return locationCount;
    }

    /**
     * 添加一条双向道路。
     * @param nameA 道路一端的地点名称
     * @param nameB 道路另一端的地点名称
     * @param meters 道路长度（米），必须大于 0
     */
    public void addRoad(String nameA, String nameB, double meters) {
        int a = findLocationIndex(nameA);
        int b = findLocationIndex(nameB);
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("道路两端的地点必须已存在：" + nameA + " - " + nameB);
        }
        if (meters <= 0) {
            throw new IllegalArgumentException("道路长度必须大于0米：" + meters);
        }
        distance[a][b] = meters;
        distance[b][a] = meters;
    }

    /** 获取两个地点之间的直接道路长度（米），没有道路返回 -1。 */
    public double getRoadMeters(int a, int b) {
        return distance[a][b];
    }

    /**
     * Dijkstra 算法：求两点间的最短路线。
     * @return 途经地点的下标数组（包含起点和终点）；两点不连通时返回 null。
     */
    public int[] findShortestPath(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= locationCount || toIndex < 0 || toIndex >= locationCount) {
            throw new IllegalArgumentException("地点下标越界：" + fromIndex + ", " + toIndex);
        }
        double[] best = new double[locationCount];   // 起点到各点的当前最短距离
        boolean[] visited = new boolean[locationCount];
        int[] prev = new int[locationCount];         // 记录前驱，用于回溯路径
        for (int i = 0; i < locationCount; i++) {
            best[i] = Double.MAX_VALUE;
            prev[i] = -1;
        }
        best[fromIndex] = 0;
        for (int step = 0; step < locationCount; step++) {
            // 从未访问的点中挑出距离最小的
            int u = -1;
            for (int i = 0; i < locationCount; i++) {
                if (!visited[i] && best[i] < Double.MAX_VALUE && (u == -1 || best[i] < best[u])) {
                    u = i;
                }
            }
            if (u == -1) {
                break; // 剩余点都不可达
            }
            visited[u] = true;
            if (u == toIndex) {
                break; // 终点已确定，提前结束
            }
            for (int v = 0; v < locationCount; v++) {
                if (visited[v] || distance[u][v] <= 0) {
                    continue;
                }
                double newDistance = best[u] + distance[u][v];
                if (newDistance < best[v]) {
                    best[v] = newDistance;
                    prev[v] = u;
                }
            }
        }
        if (best[toIndex] == Double.MAX_VALUE) {
            return null; // 两点不连通
        }
        // 从终点沿前驱回溯出完整路径
        int length = 1;
        for (int i = prev[toIndex]; i != -1; i = prev[i]) {
            length++;
        }
        int[] path = new int[length];
        int cursor = toIndex;
        for (int i = length - 1; i >= 0; i--) {
            path[i] = cursor;
            cursor = prev[cursor];
        }
        return path;
    }

    /** 计算一条路线（下标数组）的总长度，单位：米。 */
    public double pathMeters(int[] path) {
        double total = 0;
        for (int i = 0; i + 1 < path.length; i++) {
            total += distance[path[i]][path[i + 1]];
        }
        return total;
    }

    /** 把路线（下标数组）拼成文字，如：图书馆 -> 第一教学楼。 */
    public String describePath(int[] path, String separator) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            if (i > 0) {
                text.append(separator);
            }
            text.append(locations[path[i]].getName());
        }
        return text.toString();
    }
}
