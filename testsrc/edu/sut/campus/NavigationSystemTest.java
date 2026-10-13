package edu.sut.campus;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * NavigationSystem 单元测试（初始版本）。
 * 覆盖：指定起点、终点的正常导航，以及基本的输入容错。
 */
public class NavigationSystemTest {

    private CampusMap map;
    private NavigationSystem system;

    @Before
    public void setUp() {
        map = CampusNavApp.createDemoCampus();
        system = new NavigationSystem(map);
    }

    @Test
    public void testWalkNavigationNormal() {
        String result = system.navigate("图书馆", "第一教学楼", "walk");
        assertNotNull("walk 模式不应返回 null", result);
        assertTrue("步行结果应包含“步行”", result.contains("步行"));
        assertTrue("图书馆到第一教学楼直线距离为400米", result.contains("400"));
    }

    @Test
    public void testWalkChoosesShorterRoute() {
        // 南门到第一教学楼：直接经图书馆为 700 米，绕行政楼为 1000 米，应选择前者
        String result = system.navigate("南门", "第一教学楼", "walk");
        assertTrue("应经过图书馆", result.contains("南门 -> 图书馆 -> 第一教学楼"));
        assertTrue("总里程应为700米", result.contains("700"));
    }

    @Test
    public void testShuttleNavigationNormal() {
        String result = system.navigate("南门", "体育馆", "shuttle");
        assertNotNull("shuttle 模式不应返回 null", result);
        assertTrue("校车结果应包含“校车”", result.contains("校车"));
        assertTrue("应包含候车提示", result.contains("候车"));
        assertTrue("南门到体育馆最短距离为1300米", result.contains("1300"));
    }

    @Test
    public void testUnknownPlaceReturnsMessage() {
        String result = system.navigate("图书馆", "火星", "walk");
        assertTrue("未知地点应有容错提示", result.contains("地点不存在"));
    }

    @Test
    public void testUnknownModeReturnsMessage() {
        String result = system.navigate("图书馆", "第一教学楼", "fly");
        assertTrue("未知方式应有容错提示", result.contains("不支持的导航方式"));
    }
}
