package campusnav;

/**
 * 步行路线：速度 5 km/h。
 */
public class WalkRoute extends Route {
    private static final int SPEED_KMH = 5;

    public WalkRoute(Place from, Place to) {
        super(from, to);
    }

    @Override
    public String getModeName() {
        return "步行";
    }

    @Override
    public int getSpeedKmh() {
        return SPEED_KMH;
    }
}
