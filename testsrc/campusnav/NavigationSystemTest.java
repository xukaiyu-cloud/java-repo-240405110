package campusnav;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NavigationSystemTest {

    private NavigationSystem system;

    @Before
    public void setUp() {
        system = new NavigationSystem();
    }

    // ---------- 正常场景 ----------

    @Test
    public void testWalkMode() {
        String result = system.navigate("钟楼", "图书馆", "walk");
        assertNotNull("walk 模式不应返回 null", result);
        assertTrue("walk 模式应包含'步行'", result.contains("步行"));
    }

    @Test
    public void testBusMode() {
        String result = system.navigate("钟楼", "体育馆", "bus");
        assertNotNull("bus 模式不应返回 null", result);
        assertTrue("bus 模式应包含'公交'", result.contains("公交"));
    }

    @Test
    public void testChineseModeName() {
        String result = system.navigate("食堂", "宿舍", "步行");
        assertTrue("中文模式名应可用", result.contains("步行"));
    }

    @Test
    public void testModeIsCaseInsensitive() {
        String result = system.navigate("钟楼", "图书馆", "WALK");
        assertTrue("模式名应忽略大小写", result.contains("步行"));
    }

    // ---------- 异常场景 ----------

    @Test
    public void testStartPlaceNotFound() {
        String result = system.navigate("不存在的地方", "图书馆", "walk");
        assertNotNull(result);
        assertTrue("未知起点应提示不存在", result.contains("不存在"));
    }

    @Test
    public void testEndPlaceNotFound() {
        String result = system.navigate("钟楼", "操场", "walk");
        assertNotNull(result);
        assertTrue("未知终点应提示不存在", result.contains("不存在"));
    }

    @Test
    public void testNullOrEmptyInput() {
        String byNull = system.navigate(null, "图书馆", "walk");
        assertTrue("null 输入应提示不能为空", byNull.contains("不能为空"));
        String byEmpty = system.navigate("  ", "图书馆", "walk");
        assertTrue("空白输入应提示不能为空", byEmpty.contains("不能为空"));
    }

    @Test
    public void testUnsupportedMode() {
        String result = system.navigate("钟楼", "图书馆", "fly");
        assertNotNull(result);
        assertTrue("不支持的模式应提示", result.contains("模式"));
    }

    @Test
    public void testNullMode() {
        String result = system.navigate("钟楼", "图书馆", null);
        assertNotNull(result);
        assertTrue("null 模式应提示", result.contains("模式"));
    }

    // ---------- 边界场景 ----------

    @Test
    public void testSameStartAndEnd() {
        String result = system.navigate("钟楼", "钟楼", "walk");
        assertTrue("起点终点相同应有专门提示", result.contains("相同"));
    }

    @Test
    public void testInputWithSpaces() {
        String result = system.navigate(" 钟楼 ", " 图书馆 ", "walk");
        assertNotNull("两端空格应被容错处理", result);
        assertTrue(result.contains("步行"));
    }
}
