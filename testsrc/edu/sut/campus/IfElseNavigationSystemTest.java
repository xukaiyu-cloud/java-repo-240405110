package edu.sut.campus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.sut.campus.ifelse.IfElseNavigationSystem;

/**
 * IfElseNavigationSystem 单元测试。
 * 保留 if-else 旧版实现的意义：
 *   1. 确保旧版功能仍然可用，作为实现对比的基准；
 *   2. 用等价性测试验证重构后的 NavigationSystem 输出与旧版完全一致，
 *      即“功能不变”的回归验证。
 */
public class IfElseNavigationSystemTest {

    private CampusMap map;
    private IfElseNavigationSystem legacy;
    private NavigationSystem oop;

    @Before
    public void setUp() {
        map = CampusNavApp.createDemoCampus();
        legacy = new IfElseNavigationSystem(map);
        oop = new NavigationSystem(map);
    }

    @Test
    public void testLegacyWalkStillWorks() {
        String result = legacy.navigate("图书馆", "第一教学楼", "walk");
        assertTrue("旧版步行导航应正常返回", result.contains("【步行导航】"));
        assertTrue("里程应与地图数据一致", result.contains("全程 400 米"));
    }

    @Test
    public void testLegacyBikeAndHybridStillWork() {
        assertTrue(legacy.navigate("图书馆", "第一教学楼", "bike").contains("【骑行导航】"));
        assertTrue(legacy.navigate("南门", "体育馆", "hybrid").contains("步行 + 校车"));
    }

    @Test
    public void testLegacyHandlesBadInput() {
        assertTrue(legacy.navigate(null, "图书馆", "walk").contains("不能为空"));
        assertTrue(legacy.navigate("图书馆", "火星", "walk").contains("未找到地点"));
        assertTrue(legacy.navigate("图书馆", "第一教学楼", "fly").contains("不支持的导航方式"));
    }

    @Test
    public void testLegacyOutputEqualsOopVersion() {
        // 重构后的新版本与旧版本，对同一批输入必须输出完全相同的文字
        String[][] cases = {
                {"图书馆", "第一教学楼", "walk"},
                {"南门", "第一教学楼", "walk"},
                {"南门", "体育馆", "shuttle"},
                {"图书馆", "第一教学楼", "bike"},
                {"图书馆", "第一教学楼", "hybrid"},
                {"南门", "体育馆", "hybrid"},
                {"图书馆", "图书馆", "walk"},
                {"图书馆", "火星", "walk"},
                {"图书馆", "第一教学楼", "fly"},
                {"   ", "第一教学楼", "walk"},
        };
        for (int i = 0; i < cases.length; i++) {
            String[] oneCase = cases[i];
            assertEquals("第" + (i + 1) + "组用例新旧两版输出应完全一致",
                    legacy.navigate(oneCase[0], oneCase[1], oneCase[2]),
                    oop.navigate(oneCase[0], oneCase[1], oneCase[2]));
        }
    }
}
