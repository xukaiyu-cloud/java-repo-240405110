package campusnav;

/**
 * 路线抽象类：封装起点、终点、距离。
 * 子类只需提供出行方式名称与速度，统一路线文案由 describe() 生成。
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

    public abstract String getModeName();

    public abstract int getSpeedKmh();

    public String describe() {
        int minutes = calcMinutes(getSpeedKmh());
        return "【" + getModeName() + "】" + getFrom().getName() + " → " + getTo().getName()
                + "：距离 " + String.format("%.1f", distanceKm) + " km，预计耗时 "
                + minutes + " 分钟";
    }

    private int calcMinutes(int speedKmh) {
        return (int) Math.ceil(distanceKm / speedKmh * 60);
    }
}
