package campusnav;

/**
 * 公交路线。
 */
public class BusRoute extends Route {

    public BusRoute(Place from, Place to) {
        super(from, to);
    }

    @Override
    public String toText() {
        int minutes = calcMinutes(20);
        return "公交：" + getFrom().getName() + " -> " + getTo().getName()
                + "，距离" + getDistanceKm() + "km，用时" + minutes + "分钟";
    }
}
