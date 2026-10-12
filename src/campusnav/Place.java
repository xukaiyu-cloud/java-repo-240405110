package campusnav;

/**
 * 校园地点：封装名称与坐标，提供距离计算。
 */
public class Place {
    private String name;
    private double x;
    private double y;

    public Place(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(Place other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
