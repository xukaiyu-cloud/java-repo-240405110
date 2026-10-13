package edu.sut.campus;

/** 骑行导航方式：速度 4 米/秒（约 14.4 公里/小时），另加 1 分钟借还车时间。 */
public class BikeMode extends NavigationMode {

    private static final double BIKE_SPEED = 4.0;
    private static final int BIKE_EXTRA_SECONDS = 60;

    @Override
    public String getCode() {
        return "bike";
    }

    @Override
    public String getDisplayName() {
        return "骑行";
    }

    @Override
    public double getSpeedMetersPerSecond() {
        return BIKE_SPEED;
    }

    @Override
    public int getExtraSeconds() {
        return BIKE_EXTRA_SECONDS;
    }

    @Override
    public String getNotice() {
        return "（含借还车约 1 分钟）";
    }
}
