package campusnav;

/**
 * 骑行路线：速度 15 km/h。
 */
public class BikeRoute extends Route {
    private static final int SPEED_KMH = 15;

    public BikeRoute(Place from, Place to) {
        super(from, to);
    }

    @Override
    public String getModeName() {
        return "骑行";
    }

    @Override
    public int getSpeedKmh() {
        return SPEED_KMH;
    }
}
