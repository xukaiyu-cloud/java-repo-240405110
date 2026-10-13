package edu.sut.campus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * CampusMap 单元测试（初始版本）。
 * 覆盖：地点增查、道路添加、最短路径的正常 / 边界 / 异常场景。
 */
public class CampusMapTest {

    private CampusMap map;

    @Before
    public void setUp() {
        map = CampusNavApp.createDemoCampus();
    }

    @Test
    public void testAddAndFindLocation() {
        CampusMap small = new CampusMap(2);
        int index = small.addLocation("测试点", "用于测试");
        assertEquals(0, index);
        assertEquals(1, small.getLocationCount());
        assertEquals("测试点", small.getLocation(0).getName());
        assertNotNull(small.getLocationByName("测试点"));
    }

    @Test
    public void testFindUnknownLocationReturnsMinusOne() {
        assertEquals(-1, map.findLocationIndex("不存在的地方"));
        assertEquals(-1, map.findLocationIndex(null));
        assertNull(map.getLocationByName("不存在的地方"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateLocationThrows() {
        map.addLocation("图书馆", "重复添加");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBlankNameThrows() {
        map.addLocation("   ", "空白名称");
    }

    @Test(expected = IllegalStateException.class)
    public void testAddLocationBeyondCapacityThrows() {
        CampusMap small = new CampusMap(1);
        small.addLocation("A", "");
        small.addLocation("B", ""); // 超出容量 1
    }

    @Test
    public void testAddRoadIsTwoWay() {
        CampusMap small = new CampusMap(2);
        small.addLocation("A", "");
        small.addLocation("B", "");
        small.addRoad("A", "B", 100);
        assertEquals(100.0, small.getRoadMeters(0, 1), 0.001);
        assertEquals(100.0, small.getRoadMeters(1, 0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRoadWithUnknownPlaceThrows() {
        map.addRoad("图书馆", "不存在的地方", 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRoadWithNonPositiveMetersThrows() {
        map.addRoad("图书馆", "第一教学楼", 0);
    }

    @Test
    public void testShortestPathPrefersDirectRoadWhenShorter() {
        CampusMap small = new CampusMap(3);
        small.addLocation("A", "");
        small.addLocation("B", "");
        small.addLocation("C", "");
        small.addRoad("A", "B", 100);
        small.addRoad("B", "C", 100);
        small.addRoad("A", "C", 150); // 直达更短，应走 A -> C
        int[] path = small.findShortestPath(0, 2);
        assertEquals(2, path.length);
        assertEquals(0, path[0]);
        assertEquals(2, path[1]);
        assertEquals(150.0, small.pathMeters(path), 0.001);
    }

    @Test
    public void testShortestPathUnreachableReturnsNull() {
        CampusMap small = new CampusMap(3);
        small.addLocation("A", "");
        small.addLocation("B", "");
        small.addLocation("隔离区", "没有道路连接");
        small.addRoad("A", "B", 100);
        assertNull(small.findShortestPath(0, 2));
    }

    @Test
    public void testSamePointPathHasSingleNode() {
        int index = map.findLocationIndex("图书馆");
        int[] path = map.findShortestPath(index, index);
        assertEquals(1, path.length);
        assertEquals(0.0, map.pathMeters(path), 0.001);
    }

    @Test
    public void testPathMetersSumsRoads() {
        // 南门 -> 图书馆 -> 第一教学楼 = 300 + 400 = 700 米
        int[] path = map.findShortestPath(map.findLocationIndex("南门"), map.findLocationIndex("第一教学楼"));
        assertEquals(700.0, map.pathMeters(path), 0.001);
        assertTrue(map.describePath(path, " -> ").contains("图书馆"));
    }
}
