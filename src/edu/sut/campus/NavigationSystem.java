package edu.sut.campus;

/**
 * 导航系统（初始版本）。
 *
 * 本版本使用 if-else 实现“步行 / 校车”两种导航方案的切换：
 * 每一种方案都写在一个判断分支里。
 */
public class NavigationSystem {

    private final CampusMap map;

    public NavigationSystem(CampusMap map) {
        this.map = map;
    }

    public CampusMap getMap() {
        return map;
    }

    /**
     * 执行一次导航。
     * @param from 起点名称
     * @param to 终点名称
     * @param modeCode 导航方式：walk（步行）/ shuttle（校车）
     * @return 导航提示文字
     */
    public String navigate(String from, String to, String modeCode) {
        if ("walk".equals(modeCode)) {
            int start = map.findLocationIndex(from);
            int end = map.findLocationIndex(to);
            if (start < 0 || end < 0) {
                return "地点不存在：" + (start < 0 ? from : to);
            }
            int[] path = map.findShortestPath(start, end);
            if (path == null) {
                return "两地之间没有可通行的道路";
            }
            double meters = map.pathMeters(path);
            int minutes = (int) Math.round(meters / 1.4 / 60);
            return "步行导航：从" + from + "到" + to + "共" + (int) meters + "米，约" + minutes
                    + "分钟；路线：" + map.describePath(path, " -> ") + "。";
        } else if ("shuttle".equals(modeCode)) {
            int start = map.findLocationIndex(from);
            int end = map.findLocationIndex(to);
            if (start < 0 || end < 0) {
                return "地点不存在：" + (start < 0 ? from : to);
            }
            int[] path = map.findShortestPath(start, end);
            if (path == null) {
                return "两地之间没有可通行的道路";
            }
            double meters = map.pathMeters(path);
            int minutes = (int) Math.round(meters / 8.0 / 60 + 5);
            return "校车导航：从" + from + "到" + to + "共" + (int) meters + "米，约" + minutes
                    + "分钟（含候车）；路线：" + map.describePath(path, " -> ") + "。";
        } else {
            return "不支持的导航方式：" + modeCode;
        }
    }
}
