package edu.sut.campus;

/**
 * 导航系统：根据用户选择的方式计算路线并生成提示文字。
 *
 * 当前版本用 if-else 实现“步行 / 校车”两种方案的切换。
 */
public class NavigationSystem {

    /** 步行平均速度，单位：米/秒（约 5 公里/小时）。 */
    private static final double WALK_SPEED = 1.4;
    /** 校车平均速度，单位：米/秒（约 29 公里/小时）。 */
    private static final double SHUTTLE_SPEED = 8.0;
    /** 校车固定候车时间，单位：秒。 */
    private static final int SHUTTLE_WAIT_SECONDS = 300;

    private final CampusMap map;

    public NavigationSystem(CampusMap map) {
        this.map = map;
    }

    public CampusMap getMap() {
        return map;
    }

    /**
     * 执行一次导航。
     *
     * @param from     起点名称，不允许为空
     * @param to       终点名称，不允许为空
     * @param modeCode 导航方式代码：walk（步行）/ shuttle（校车），不区分大小写
     * @return 导航提示文字；输入不合法时返回容错提示，不抛出异常
     */
    public String navigate(String from, String to, String modeCode) {
        if (from == null || from.trim().isEmpty() || to == null || to.trim().isEmpty()) {
            return "导航失败：起点和终点不能为空。";
        }
        if ("walk".equalsIgnoreCase(modeCode)) {
            return walk(from.trim(), to.trim());
        } else if ("shuttle".equalsIgnoreCase(modeCode)) {
            return shuttle(from.trim(), to.trim());
        } else {
            return "不支持的导航方式：“" + modeCode + "”，可选：walk / shuttle。";
        }
    }

    /** 步行方案：速度 1.4 米/秒。 */
    private String walk(String from, String to) {
        int start = map.findLocationIndex(from);
        if (start < 0) {
            return "导航失败：未找到地点：“" + from + "”。";
        }
        int end = map.findLocationIndex(to);
        if (end < 0) {
            return "导航失败：未找到地点：“" + to + "”。";
        }
        int[] path = map.findShortestPath(start, end);
        if (path == null) {
            return "导航失败：两地之间没有可通行的道路。";
        }
        if (path.length == 1) {
            return "【步行导航】起点与终点相同（“" + from + "”），无需导航。";
        }
        double meters = map.pathMeters(path);
        int minutes = toMinutes(toSeconds(meters, WALK_SPEED, 0));
        return "【步行导航】从“" + from + "”到“" + to + "”，全程 " + (int) Math.round(meters)
                + " 米，预计约 " + minutes + " 分钟。路线：" + map.describePath(path, " → ") + "。";
    }

    /** 校车方案：速度 8 米/秒，另加 5 分钟候车时间。 */
    private String shuttle(String from, String to) {
        int start = map.findLocationIndex(from);
        if (start < 0) {
            return "导航失败：未找到地点：“" + from + "”。";
        }
        int end = map.findLocationIndex(to);
        if (end < 0) {
            return "导航失败：未找到地点：“" + to + "”。";
        }
        int[] path = map.findShortestPath(start, end);
        if (path == null) {
            return "导航失败：两地之间没有可通行的道路。";
        }
        if (path.length == 1) {
            return "【校车导航】起点与终点相同（“" + from + "”），无需导航。";
        }
        double meters = map.pathMeters(path);
        int minutes = toMinutes(toSeconds(meters, SHUTTLE_SPEED, SHUTTLE_WAIT_SECONDS));
        return "【校车导航】从“" + from + "”到“" + to + "”，全程 " + (int) Math.round(meters)
                + " 米，预计约 " + minutes + " 分钟（含候车约 5 分钟）。路线：" + map.describePath(path, " → ") + "。";
    }

    /** 按“距离 ÷ 速度 + 固定耗时”估算所需秒数。 */
    private static int toSeconds(double meters, double speed, int extraSeconds) {
        return (int) Math.round(meters / speed) + extraSeconds;
    }

    /** 秒数换算为分钟（四舍五入，不足 1 分钟按 1 分钟计）。 */
    private static int toMinutes(int seconds) {
        return Math.max(1, (int) Math.round(seconds / 60.0));
    }
}
