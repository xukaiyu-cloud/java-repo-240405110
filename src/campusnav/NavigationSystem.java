package campusnav;

/**
 * 校园导航系统：维护地点数组，按出行模式用 if-else 分发到对应路线对象。
 * 不使用集合框架，地点用数组存储。
 */
public class NavigationSystem {
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

    public String navigate(String start, String end, String mode) {
        if (start == null || end == null || start.trim().isEmpty() || end.trim().isEmpty()) {
            return "起点和终点不能为空";
        }
        Place from = findPlace(start);
        Place to = findPlace(end);
        if (from == null) {
            return "地点不存在：" + start.trim();
        }
        if (to == null) {
            return "地点不存在：" + end.trim();
        }
        if (from == to) {
            return "起点和终点相同，无需导航";
        }
        if (mode == null) {
            return "没有这种模式";
        }

        Route route;
        if (mode.equalsIgnoreCase("walk") || mode.equals("步行")) {
            route = new WalkRoute(from, to);
        } else if (mode.equalsIgnoreCase("bus") || mode.equals("公交")) {
            route = new BusRoute(from, to);
        } else {
            return "没有这种模式：" + mode;
        }
        return route.toText();
    }
}
