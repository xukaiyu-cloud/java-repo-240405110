package edu.sut.campus;

/** 校车导航方式：速度 8 米/秒（约 29 公里/小时），另加 5 分钟候车时间。 */
public class ShuttleMode extends NavigationMode {

    private static final double SHUTTLE_SPEED = 8.0;
    private static final int SHUTTLE_WAIT_SECONDS = 300;

    @Override
    public String getCode() {
        return "shuttle";
    }

    @Override
    public String getDisplayName() {
        return "校车";
    }

    @Override
    public double getSpeedMetersPerSecond() {
        return SHUTTLE_SPEED;
    }

    @Override
    public int getExtraSeconds() {
        return SHUTTLE_WAIT_SECONDS;
    }

    @Override
    public String getNotice() {
        return "（含候车约 5 分钟）";
    }
}
