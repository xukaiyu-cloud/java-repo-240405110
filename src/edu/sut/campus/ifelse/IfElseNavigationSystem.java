package edu.sut.campus.ifelse;

import edu.sut.campus.CampusMap;

/**
 * 导航系统（if-else 版本，保留作为对比）。
 *
 * 本类不使用抽象类/继承，所有导航方案都塞在同一套 if-else 分支里：
 * 每新增一种导航方式，都需要在 navigate() 中增加一个分支，
 * 并复制一份与已有分支几乎相同的处理代码（见 bike()、hybrid()）。
 *
 * 保留原因：
 *   1. 作为实验报告中“使用抽象类与不使用抽象类”的实现对比材料；
 *   2. 作为重构后的回归基准 —— 新旧两版对同样的输入必须输出同样的结果。
 */
public class IfElseNavigationSystem {

    /** 步行平均速度，单位：米/秒（约 5 公里/小时）。 */
    private static final double WALK_SPEED = 1.4;
    /** 骑行平均速度，单位：米/秒（约 14.4 公里/小时）。 */
    private static final double BIKE_SPEED = 4.0;
    /** 骑行固定借还车时间，单位：秒。 */
    private static final int BIKE_EXTRA_SECONDS = 60;
    /** 校车平均速度，单位：米/秒（约 29 公里/小时）。 */
    private static final double SHUTTLE_SPEED = 8.0;
    /** 校车固定候车时间，单位：秒。 */
    private static final int SHUTTLE_WAIT_SECONDS = 300;
    /** 混合方案的短距离阈值：不超过该距离时推荐全程步行（米）。 */
    private static final int HYBRID_WALK_LIMIT_METERS = 800;
    /** 混合方案中校车两端步行时间，单位：秒。 */
    private static final int HYBRID_WALK_ENDS_SECONDS = 120;

    private final CampusMap map;

    public IfElseNavigationSystem(CampusMap map) {
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
     * @param modeCode 导航方式代码：walk（步行）/ bike（骑行）/ shuttle（校车）/ hybrid（混合），不区分大小写
     * @return 导航提示文字；输入不合法时返回容错提示，不抛出异常
     */
    public String navigate(String from, String to, String modeCode) {
        if (from == null || from.trim().isEmpty() || to == null || to.trim().isEmpty()) {
            return "导航失败：起点和终点不能为空。";
        }
        if ("walk".equalsIgnoreCase(modeCode)) {
            return walk(from.trim(), to.trim());
        } else if ("bike".equalsIgnoreCase(modeCode)) {
            // 第 3 次迭代：新增骑行分支（与 walk 分支结构几乎相同）
            return bike(from.trim(), to.trim());
        } else if ("shuttle".equalsIgnoreCase(modeCode)) {
            return shuttle(from.trim(), to.trim());
        } else if ("hybrid".equalsIgnoreCase(modeCode)) {
            // 第 3 次迭代：新增混合分支（内部还要再按距离二选一，分支更加复杂）
            return hybrid(from.trim(), to.trim());
        } else {
            return "不支持的导航方式：“" + modeCode + "”，可选：walk / bike / shuttle / hybrid。";
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

    /** 骑行方案：速度 4 米/秒，另加 1 分钟借还车时间。 */
    private String bike(String from, String to) {
        // 与 walk() 几乎完全重复，只是速度、附加时间和提示文字不同
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
            return "【骑行导航】起点与终点相同（“" + from + "”），无需导航。";
        }
        double meters = map.pathMeters(path);
        int minutes = toMinutes(toSeconds(meters, BIKE_SPEED, BIKE_EXTRA_SECONDS));
        return "【骑行导航】从“" + from + "”到“" + to + "”，全程 " + (int) Math.round(meters)
                + " 米，预计约 " + minutes + " 分钟（含借还车约 1 分钟）。路线：" + map.describePath(path, " → ") + "。";
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

    /**
     * 混合方案：距离不超过 800 米时推荐全程步行，超过时推荐“步行 + 校车”。
     * 本方法内部又出现了一层 if-else，分支会随着规则细化继续膨胀。
     */
    private String hybrid(String from, String to) {
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
            return "【混合导航】起点与终点相同（“" + from + "”），无需导航。";
        }
        double meters = map.pathMeters(path);
        String route = map.describePath(path, " → ");
        if (meters <= HYBRID_WALK_LIMIT_METERS) {
            int minutes = toMinutes(toSeconds(meters, WALK_SPEED, 0));
            return "【混合导航】当前距离较短（" + (int) Math.round(meters) + " 米），推荐直接步行前往：从“" + from
                    + "”到“" + to + "”，预计约 " + minutes + " 分钟。路线：" + route + "。";
        } else {
            int seconds = toSeconds(meters, SHUTTLE_SPEED, SHUTTLE_WAIT_SECONDS + HYBRID_WALK_ENDS_SECONDS);
            int minutes = toMinutes(seconds);
            return "【混合导航】当前距离较远（" + (int) Math.round(meters) + " 米），推荐“步行 + 校车”组合：从“"
                    + from + "”到“" + to + "”，预计约 " + minutes + " 分钟（含候车与两端步行约 7 分钟）。路线："
                    + route + "。";
        }
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
