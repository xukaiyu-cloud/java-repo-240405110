package edu.sut.campus;

/**
 * 导航结果类：封装一次导航的起点、终点、完整途经点和总里程。
 * 把“结果数据”与“文字拼装”从各导航方式中抽出来，便于所有子类复用。
 */
public class Route {

    private final Location start;
    private final Location end;
    private final Location[] waypoints;
    private final double meters;

    public Route(Location start, Location end, Location[] waypoints, double meters) {
        if (start == null || end == null || waypoints == null || waypoints.length == 0) {
            throw new IllegalArgumentException("导航结果参数不完整");
        }
        this.start = start;
        this.end = end;
        this.waypoints = waypoints;
        this.meters = meters;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    /** 全程里程，单位：米。 */
    public double getMeters() {
        return meters;
    }

    /** 途经点个数（含起点和终点）。 */
    public int getStopCount() {
        return waypoints.length;
    }

    /** 起点与终点是否为同一个地点。 */
    public boolean isSamePoint() {
        return start.equals(end);
    }

    /** 把途经点拼成文字，如：图书馆 → 第一教学楼。 */
    public String getPathText(String separator) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < waypoints.length; i++) {
            if (i > 0) {
                text.append(separator);
            }
            text.append(waypoints[i].getName());
        }
        return text.toString();
    }
}
