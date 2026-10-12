package campusnav;

/**
 * 公交路线：速度 20 km/h。
 */
public class BusRoute extends Route {
    private static final int SPEED_KMH = 20;

    public BusRoute(Place from, Place to) {
        super(from, to);
    }

    @Override
    public String getModeName() {
        return "公交";
    }

    @Override
    public int getSpeedKmh() {
        return SPEED_KMH;
    }
}
