package edu.sut.campus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * NavigationMode 抽象类与多态单元测试。
 * 重点验证“对扩展开放、对修改关闭”：
 * 新增一种导航方式只需要新增一个子类，不必修改 NavigationSystem 已有代码。
 */
public class NavigationModeTest {

    private CampusMap map;

    @Before
    public void setUp() {
        map = CampusNavApp.createDemoCampus();
    }

    @Test
    public void testEachModeExposesItsOwnCodeAndName() {
        NavigationMode[] modes = { new WalkMode(), new BikeMode(), new ShuttleMode(), new HybridMode() };
        assertEquals("walk", modes[0].getCode());
        assertEquals("步行", modes[0].getDisplayName());
        assertEquals("bike", modes[1].getCode());
        assertEquals("骑行", modes[1].getDisplayName());
        assertEquals("shuttle", modes[2].getCode());
        assertEquals("校车", modes[2].getDisplayName());
        assertEquals("hybrid", modes[3].getCode());
        assertEquals("混合", modes[3].getDisplayName());
    }

    @Test
    public void testPolymorphicPlanProducesOwnFormat() {
        // 同一个 plan() 调用点，运行期分发到不同子类，输出各自方式的提示语
        NavigationMode[] modes = { new WalkMode(), new BikeMode(), new ShuttleMode(), new HybridMode() };
        for (int i = 0; i < modes.length; i++) {
            String result = modes[i].plan(map, "图书馆", "第一教学楼");
            assertNotNull(result);
            assertTrue("每种方式的输出都应包含自己的名称",
                    result.contains("【" + modes[i].getDisplayName() + "导航】"));
        }
    }

    /** 测试用自定义导航方式：滑板，用于验证扩展能力。 */
    private static class SkateboardMode extends NavigationMode {

        @Override
        public String getCode() {
            return "skate";
        }

        @Override
        public String getDisplayName() {
            return "滑板";
        }

        @Override
        public double getSpeedMetersPerSecond() {
            return 3.0;
        }
    }

    @Test
    public void testNewModeCanBePluggedInWithoutChangingSystem() {
        // 不修改 NavigationSystem / WalkMode 等任何已有代码，只注册新子类即可使用
        NavigationSystem system = new NavigationSystem(map, new NavigationMode[] { new SkateboardMode() });
        String result = system.navigate("图书馆", "第一教学楼", "skate");
        assertTrue("新方式应能正常导航", result.contains("【滑板导航】"));
        assertTrue("新方式应自动复用公共流程计算里程", result.contains("全程 400 米"));
        assertTrue("新方式应自动复用公共流程输出路线", result.contains("图书馆 → 第一教学楼"));
    }

    @Test
    public void testSingleModeSystemRejectsUnregisteredCodes() {
        NavigationSystem system = new NavigationSystem(map, new NavigationMode[] { new WalkMode() });
        String result = system.navigate("图书馆", "第一教学楼", "bike");
        assertTrue("未注册的方式应给出容错提示", result.contains("不支持的导航方式"));
        assertTrue("可选列表应反映实际注册的方式", result.contains("可选：walk。"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSystemRejectsEmptyModeArray() {
        new NavigationSystem(map, new NavigationMode[0]);
    }
}
