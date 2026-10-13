package edu.sut.campus;

/**
 * 导航方式抽象类 —— 抽象、继承、多态的核心。
 *
 * 职责划分：
 *   抽象类 NavigationMode 负责“怎么导航”的公共流程：
 *     校验输入 → 查找地点 → 求最短路 → 组装 Route → 生成提示文字；
 *   子类只负责“自己是什么方式”：
 *     方式代码、显示名称、速度、附加耗时和提示语。
 *
 * 与 if-else 版本的关键差别：
 *   新增一种导航方式时，只需新增一个子类并注册进 NavigationSystem，
 *   不需要修改任何已有代码（对扩展开放，对修改关闭）。
 */
public abstract class NavigationMode {

    /** 导航方式代码，例如 walk / bike / shuttle / hybrid。 */
    public abstract String getCode();

    /** 导航方式的显示名称，例如“步行”。 */
    public abstract String getDisplayName();

    /** 平均速度，单位：米/秒。 */
    public abstract double getSpeedMetersPerSecond();

    /** 附加固定耗时（秒），如候车、借还车时间；默认 0。 */
    public int getExtraSeconds() {
        return 0;
    }

    /** 补充提示语，说明附加耗时的构成；默认无提示。 */
    public String getNotice() {
        return "";
    }

    /**
     * 执行一次导航。公共流程统一写在抽象类中，
     * 子类通过覆写上面的抽象方法表达个性；
     * 规则特殊的方式（如混合导航）可以整体覆写本方法，这就是多态。
     */
    public String plan(CampusMap map, String startName, String endName) {
        Route route = buildRoute(map, startName, endName);
        if (route.isSamePoint()) {
            return "【" + getDisplayName() + "导航】起点与终点相同（“" + route.getStart().getName()
                    + "”），无需导航。";
        }
        return formatRoute(route);
    }

    /** 按本方式的默认规则生成提示文字，子类可按需覆写或复用。 */
    protected String formatRoute(Route route) {
        int minutes = toMinutes(estimateSeconds(route));
        return "【" + getDisplayName() + "导航】从“" + route.getStart().getName() + "”到“"
                + route.getEnd().getName() + "”，全程 " + (int) Math.round(route.getMeters())
                + " 米，预计约 " + minutes + " 分钟" + getNotice() + "。路线："
                + route.getPathText(" → ") + "。";
    }

    /** 本方式的预计耗时（秒）：距离 ÷ 速度 + 附加耗时。 */
    protected int estimateSeconds(Route route) {
        return toSeconds(route.getMeters(), getSpeedMetersPerSecond(), getExtraSeconds());
    }

    /**
     * 公共步骤：校验名称 → 查找地点 → 求最短路 → 组装 Route。
     * 所有子类共用同一条路径，容错行为天然保持一致。
     */
    protected Route buildRoute(CampusMap map, String startName, String endName) {
        if (map == null) {
            throw new IllegalArgumentException("校园地图不能为空");
        }
        if (startName == null || startName.trim().isEmpty()
                || endName == null || endName.trim().isEmpty()) {
            throw new IllegalArgumentException("起点和终点不能为空");
        }
        int from = map.findLocationIndex(startName);
        if (from < 0) {
            throw new IllegalArgumentException("未找到地点：“" + startName.trim() + "”");
        }
        int to = map.findLocationIndex(endName);
        if (to < 0) {
            throw new IllegalArgumentException("未找到地点：“" + endName.trim() + "”");
        }
        int[] path = map.findShortestPath(from, to);
        if (path == null) {
            throw new IllegalArgumentException("两地之间没有可通行的道路");
        }
        Location[] waypoints = new Location[path.length];
        for (int i = 0; i < path.length; i++) {
            waypoints[i] = map.getLocation(path[i]);
        }
        return new Route(map.getLocation(from), map.getLocation(to), waypoints, map.pathMeters(path));
    }

    /** 按“距离 ÷ 速度 + 固定耗时”估算所需秒数。 */
    protected static int toSeconds(double meters, double speed, int extraSeconds) {
        return (int) Math.round(meters / speed) + extraSeconds;
    }

    /** 秒数换算为分钟（四舍五入，不足 1 分钟按 1 分钟计）。 */
    protected static int toMinutes(int seconds) {
        return Math.max(1, (int) Math.round(seconds / 60.0));
    }
}
