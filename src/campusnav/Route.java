package campusnav;

/**
 * 路线抽象类：封装起点、终点、距离，由子类决定出行方式与文案。
 */
public abstract class Route {
    private Place from;
    private Place to;
    private double distanceKm;

    public Route(Place from, Place to) {
        this.from = from;
        this.to = to;
        this.distanceKm = from.distanceTo(to);
    }

    public Place getFrom() {
        return from;
    }

    public Place getTo() {
        return to;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    protected int calcMinutes(int speedKmh) {
        return (int) Math.ceil(distanceKm / speedKmh * 60);
    }

    public abstract String toText();
}
