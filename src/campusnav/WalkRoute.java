package campusnav;

/**
 * 步行路线。
 */
public class WalkRoute extends Route {

    public WalkRoute(Place from, Place to) {
        super(from, to);
    }

    @Override
    public String toText() {
        int minutes = calcMinutes(5);
        return "步行：" + getFrom().getName() + " -> " + getTo().getName()
                + "，距离" + getDistanceKm() + "km，用时" + minutes + "分钟";
    }
}
