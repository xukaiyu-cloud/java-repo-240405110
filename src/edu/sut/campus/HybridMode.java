package edu.sut.campus;

/**
 * 混合导航方式：距离不超过 800 米时推荐全程步行，超过时推荐“步行 + 校车”。
 *
 * 由于决策规则比其它方式特殊，本子类整体覆写了 plan() 方法，
 * 复用抽象类提供的 buildRoute() 公共流程，只替换“怎么推荐”的判断，
 * 这正是多态能带来的灵活性。
 */
public class HybridMode extends NavigationMode {

    /** 短距离阈值：不超过该距离时推荐全程步行（米）。 */
    private static final int WALK_LIMIT_METERS = 800;
    private static final double WALK_SPEED = 1.4;
    private static final double SHUTTLE_SPEED = 8.0;
    private static final int SHUTTLE_WAIT_SECONDS = 300;
    /** 混合方案中校车两端步行时间（秒）。 */
    private static final int WALK_ENDS_SECONDS = 120;

    @Override
    public String getCode() {
        return "hybrid";
    }

    @Override
    public String getDisplayName() {
        return "混合";
    }

    /** 混合方式没有单一速度，这里取校车速度作为偏保守的参考值。 */
    @Override
    public double getSpeedMetersPerSecond() {
        return SHUTTLE_SPEED;
    }

    @Override
    public int getExtraSeconds() {
        return SHUTTLE_WAIT_SECONDS + WALK_ENDS_SECONDS;
    }

    @Override
    public String plan(CampusMap map, String startName, String endName) {
        Route route = buildRoute(map, startName, endName);
        if (route.isSamePoint()) {
            return "【" + getDisplayName() + "导航】起点与终点相同（“" + route.getStart().getName()
                    + "”），无需导航。";
        }
        int meters = (int) Math.round(route.getMeters());
        if (route.getMeters() <= WALK_LIMIT_METERS) {
            int minutes = toMinutes(toSeconds(route.getMeters(), WALK_SPEED, 0));
            return "【混合导航】当前距离较短（" + meters + " 米），推荐直接步行前往：从“"
                    + route.getStart().getName() + "”到“" + route.getEnd().getName()
                    + "”，预计约 " + minutes + " 分钟。路线：" + route.getPathText(" → ") + "。";
        }
        int minutes = toMinutes(toSeconds(route.getMeters(), SHUTTLE_SPEED,
                SHUTTLE_WAIT_SECONDS + WALK_ENDS_SECONDS));
        return "【混合导航】当前距离较远（" + meters + " 米），推荐“步行 + 校车”组合：从“"
                + route.getStart().getName() + "”到“" + route.getEnd().getName()
                + "”，预计约 " + minutes + " 分钟（含候车与两端步行约 7 分钟）。路线："
                + route.getPathText(" → ") + "。";
    }
}
