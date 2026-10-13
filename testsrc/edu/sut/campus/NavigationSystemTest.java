package edu.sut.campus;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * NavigationSystem 单元测试。
 * 覆盖三类场景：
 *   正常场景 —— 指定起终点、各导航方式正常返回；
 * 边界场景 —— 起终点相同、大小写、首尾空格；
 * 异常/容错场景 —— 空输入、未知地点、未知导航方式。
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
        assertTrue("结果应包含【步行导航】", result.contains("【步行导航】"));
        assertTrue("图书馆到第一教学楼直线距离为400米", result.contains("全程 400 米"));
        assertTrue("预计时间应为约5分钟", result.contains("约 5 分钟"));
        assertTrue("应包含完整路线", result.contains("图书馆 → 第一教学楼"));
    }

    @Test
    public void testWalkChoosesShorterRoute() {
        // 南门到第一教学楼：经图书馆 700 米，绕行政楼 1000 米，应选择前者
        String result = system.navigate("南门", "第一教学楼", "walk");
        assertTrue("应经过图书馆", result.contains("南门 → 图书馆 → 第一教学楼"));
        assertTrue("总里程应为700米", result.contains("全程 700 米"));
    }

    @Test
    public void testShuttleNavigationNormal() {
        String result = system.navigate("南门", "体育馆", "shuttle");
        assertNotNull("shuttle 模式不应返回 null", result);
        assertTrue("结果应包含【校车导航】", result.contains("【校车导航】"));
        assertTrue("南门到体育馆最短距离为1300米", result.contains("全程 1300 米"));
        assertTrue("应包含候车时间提示", result.contains("含候车约 5 分钟"));
    }

    @Test
    public void testModeCodeIsCaseInsensitive() {
        String result = system.navigate("图书馆", "第一教学楼", "WALK");
        assertTrue("方式代码 WALK 应按步行处理", result.contains("【步行导航】"));
    }

    @Test
    public void testSurroundingSpacesAreTrimmed() {
        String result = system.navigate(" 图书馆 ", "第一教学楼 ", "walk");
        assertTrue("地点名首尾空格应被忽略", result.contains("【步行导航】"));
        assertTrue("路线中应使用去除空格后的名称", result.contains("从“图书馆”"));
    }

    @Test
    public void testSameStartAndEndReturnsNoNeedMessage() {
        String result = system.navigate("图书馆", "图书馆", "walk");
        assertTrue("起终点相同时应提示无需导航", result.contains("无需导航"));
    }

    @Test
    public void testUnknownPlaceReturnsMessage() {
        String result = system.navigate("图书馆", "火星", "walk");
        assertTrue("未知地点应给出容错提示", result.contains("未找到地点"));
        assertTrue("提示中应包含错误的名称", result.contains("火星"));
    }

    @Test
    public void testUnknownModeReturnsMessage() {
        String result = system.navigate("图书馆", "第一教学楼", "fly");
        assertTrue("未知方式应给出容错提示", result.contains("不支持的导航方式"));
    }

    @Test
    public void testNullInputsReturnMessage() {
        assertTrue(system.navigate(null, "图书馆", "walk").contains("不能为空"));
        assertTrue(system.navigate("图书馆", null, "walk").contains("不能为空"));
        assertTrue(system.navigate(null, null, null).contains("不能为空"));
    }

    @Test
    public void testBlankInputsReturnMessage() {
        assertTrue(system.navigate("   ", "第一教学楼", "walk").contains("不能为空"));
        assertTrue(system.navigate("图书馆", "", "walk").contains("不能为空"));
    }
}
