package campusnav;

/**
 * 校园导航系统：维护校园地点数组，按出行模式用 if-else 分发到对应路线对象。
 * 不使用集合框架，地点信息用数组存储。
 */
public class NavigationSystem {
    private static final String MODE_HINT = "可用模式：步行(walk)、公交(bus)、骑行(bike)";

    private Place[] places;

    public NavigationSystem() {
        places = new Place[] {
                new Place("钟楼", 0.0, 0.0),
                new Place("图书馆", 0.4, 0.3),
                new Place("教学楼", 1.0, 0.2),
                new Place("食堂", 0.9, -0.6),
                new Place("宿舍", -0.5, -0.8),
                new Place("体育馆", 0.2, -1.2)
        };
    }

    private Place findPlace(String name) {
        String trimmed = name.trim();
        for (int i = 0; i < places.length; i++) {
            if (places[i].getName().equals(trimmed)) {
                return places[i];
            }
        }
        return null;
    }

    private String placeNames() {
        String names = "";
        for (int i = 0; i < places.length; i++) {
            if (i > 0) {
                names += "、";
            }
            names += places[i].getName();
        }
        return names;
    }

    /**
     * 按出行模式规划路线。
     *
     * @return 导航结果或错误提示（始终不返回 null）
     */
    public String navigate(String start, String end, String mode) {
        if (start == null || end == null || start.trim().isEmpty() || end.trim().isEmpty()) {
            return "[校园导航] 起点或终点不能为空";
        }

        Place from = findPlace(start);
        Place to = findPlace(end);
        if (from == null) {
            return "[校园导航] 地点不存在：「" + start.trim() + "」，可用地点：" + placeNames();
        }
        if (to == null) {
            return "[校园导航] 地点不存在：「" + end.trim() + "」，可用地点：" + placeNames();
        }
        if (from == to) {
            return "[校园导航] 起点与终点相同（" + from.getName() + "），无需导航";
        }

        if (mode == null || mode.trim().isEmpty()) {
            return "[校园导航] 出行模式不能为空，" + MODE_HINT;
        }
        String normalizedMode = mode.trim();

        Route route;
        if (normalizedMode.equalsIgnoreCase("walk") || normalizedMode.equals("步行")) {
            route = new WalkRoute(from, to);
        } else if (normalizedMode.equalsIgnoreCase("bus") || normalizedMode.equals("公交")) {
            route = new BusRoute(from, to);
        } else if (normalizedMode.equalsIgnoreCase("bike") || normalizedMode.equals("骑行")) {
            route = new BikeRoute(from, to);
        } else {
            return "[校园导航] 不支持的出行模式「" + normalizedMode + "」，" + MODE_HINT;
        }
        return route.describe();
    }
}
