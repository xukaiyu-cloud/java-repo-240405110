package edu.sut.campus;

/** 步行导航方式：速度 1.4 米/秒（约 5 公里/小时），无附加耗时。 */
public class WalkMode extends NavigationMode {

    private static final double WALK_SPEED = 1.4;

    @Override
    public String getCode() {
        return "walk";
    }

    @Override
    public String getDisplayName() {
        return "步行";
    }

    @Override
    public double getSpeedMetersPerSecond() {
        return WALK_SPEED;
    }
}
