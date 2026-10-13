package edu.sut.campus;

/**
 * 导航系统（面向对象版本）：持有校园地图和所有导航方式。
 *
 * 与 if-else 版本的对比：
 *   本类只做两件事 —— 用数组保存各导航方式、把请求转发给对应方式执行；
 *   不再关心每种方式的速度、附加耗时和提示文字，
 *   新增一种导航方式时无需修改本类的任何一行代码。
 */
public class NavigationSystem {

    private final CampusMap map;
    private final NavigationMode[] modes;

    /** 使用默认的四种导航方式：步行、骑行、校车、混合。 */
    public NavigationSystem(CampusMap map) {
        this(map, new NavigationMode[] { new WalkMode(), new BikeMode(), new ShuttleMode(), new HybridMode() });
    }

    /** 允许自由组合导航方式，便于测试与后续扩展。 */
    public NavigationSystem(CampusMap map, NavigationMode[] modes) {
        if (map == null) {
            throw new IllegalArgumentException("校园地图不能为空");
        }
        if (modes == null || modes.length == 0) {
            throw new IllegalArgumentException("至少需要一种导航方式");
        }
        this.map = map;
        this.modes = modes;
    }

    public CampusMap getMap() {
        return map;
    }

    /** 获取全部导航方式（返回副本，防止外部直接修改内部数组）。 */
    public NavigationMode[] getModes() {
        NavigationMode[] copy = new NavigationMode[modes.length];
        for (int i = 0; i < modes.length; i++) {
            copy[i] = modes[i];
        }
        return copy;
    }

    /**
     * 执行一次导航。对外接口与 if-else 版本完全一致
     * （同为 navigate(起点, 终点, 方式代码)），便于两种实现互换、对比和回归测试。
     *
     * @return 导航提示文字；输入不合法时返回容错提示，不抛出异常
     */
    public String navigate(String from, String to, String modeCode) {
        if (from == null || from.trim().isEmpty() || to == null || to.trim().isEmpty()) {
            return "导航失败：起点和终点不能为空。";
        }
        NavigationMode mode = findMode(modeCode);
        if (mode == null) {
            return "不支持的导航方式：“" + modeCode + "”，可选：" + describeModes() + "。";
        }
        try {
            return mode.plan(map, from.trim(), to.trim());
        } catch (IllegalArgumentException e) {
            return "导航失败：" + e.getMessage() + "。";
        }
    }

    /** 按代码查找导航方式（数组遍历，不使用集合框架）。 */
    private NavigationMode findMode(String modeCode) {
        if (modeCode == null) {
            return null;
        }
        String key = modeCode.trim();
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].getCode().equalsIgnoreCase(key)) {
                return modes[i];
            }
        }
        return null;
    }

    /** 列出已注册的导航方式代码，如：walk / bike / shuttle / hybrid。 */
    private String describeModes() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < modes.length; i++) {
            if (i > 0) {
                text.append(" / ");
            }
            text.append(modes[i].getCode());
        }
        return text.toString();
    }
}
