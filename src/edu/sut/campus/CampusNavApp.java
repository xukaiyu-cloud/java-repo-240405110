package edu.sut.campus;

import java.util.Scanner;

/**
 * 校园导航系统主程序（控制台界面）。
 *
 * 运行方式：
 *   java edu.sut.campus.CampusNavApp          进入交互式菜单
 *   java edu.sut.campus.CampusNavApp --demo   自动演示若干条导航（便于截图记录）
 */
public class CampusNavApp {

    public static void main(String[] args) {
        CampusMap map = createDemoCampus();
        NavigationSystem system = new NavigationSystem(map);
        if (args.length > 0 && "--demo".equals(args[0])) {
            runDemo(system);
        } else {
            runMenu(system);
        }
    }

    /**
     * 构建演示用校园地图：10 个地点、14 条道路。
     * 测试代码同样使用本方法构造地图，保证程序与测试数据一致。
     */
    public static CampusMap createDemoCampus() {
        CampusMap map = new CampusMap(12);
        map.addLocation("南门", "学校正门");
        map.addLocation("图书馆", "藏书与自习中心");
        map.addLocation("第一教学楼", "本科课程主要教学楼");
        map.addLocation("第二教学楼", "实验与小班教室");
        map.addLocation("实验楼", "专业实验室");
        map.addLocation("体育馆", "室内运动场馆");
        map.addLocation("第一食堂", "学生餐厅");
        map.addLocation("学生宿舍", "本科生宿舍区");
        map.addLocation("行政楼", "教务与行政办公");
        map.addLocation("校医院", "校医院门诊部");

        map.addRoad("南门", "图书馆", 300);
        map.addRoad("南门", "行政楼", 250);
        map.addRoad("行政楼", "图书馆", 350);
        map.addRoad("图书馆", "第一教学楼", 400);
        map.addRoad("第一教学楼", "第二教学楼", 250);
        map.addRoad("图书馆", "第一食堂", 450);
        map.addRoad("第一食堂", "第二教学楼", 300);
        map.addRoad("第一食堂", "学生宿舍", 500);
        map.addRoad("学生宿舍", "体育馆", 400);
        map.addRoad("体育馆", "第二教学楼", 600);
        map.addRoad("图书馆", "实验楼", 550);
        map.addRoad("实验楼", "第一教学楼", 300);
        map.addRoad("学生宿舍", "南门", 900);
        map.addRoad("第二教学楼", "校医院", 350);
        return map;
    }

    /** 交互式菜单：由用户选择导航方式并输入起点、终点。 */
    private static void runMenu(NavigationSystem system) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("====== 校园导航系统 ======");
            System.out.println("  1. 步行导航（walk）");
            System.out.println("  2. 校车导航（shuttle）");
            System.out.println("  0. 退出");
            System.out.print("请选择导航方式：");
            String choice = scanner.nextLine().trim();
            if ("0".equals(choice)) {
                System.out.println("感谢使用，再见！");
                break;
            }
            String modeCode;
            if ("1".equals(choice)) {
                modeCode = "walk";
                System.out.println("已选择：步行导航");
            } else if ("2".equals(choice)) {
                modeCode = "shuttle";
                System.out.println("已选择：校车导航");
            } else {
                System.out.println("输入有误：请输入 0 ~ 2 之间的序号。");
                System.out.println();
                continue;
            }
            System.out.print("请输入起点名称：");
            String from = scanner.nextLine();
            System.out.print("请输入终点名称：");
            String to = scanner.nextLine();
            System.out.println(system.navigate(from, to, modeCode));
            System.out.println();
        }
        scanner.close();
    }

    /** 演示模式：连续输出多条导航结果，便于截图记录。 */
    private static void runDemo(NavigationSystem system) {
        System.out.println("========== 校园导航系统 演示 ==========");
        printResult("演示1：图书馆 -> 第一教学楼（步行）",
                system.navigate("图书馆", "第一教学楼", "walk"));
        printResult("演示2：南门 -> 第一教学楼（步行，多途经点）",
                system.navigate("南门", "第一教学楼", "walk"));
        printResult("演示3：南门 -> 体育馆（校车）",
                system.navigate("南门", "体育馆", "shuttle"));
        printResult("演示4：方式代码不区分大小写（WALK）",
                system.navigate("图书馆", "第一教学楼", "WALK"));
        printResult("演示5：起点与终点相同（边界情况）",
                system.navigate("图书馆", "图书馆", "walk"));
        printResult("演示6：输入不存在的地点（容错）",
                system.navigate("图书馆", "火星", "walk"));
        printResult("演示7：输入不支持的导航方式（容错）",
                system.navigate("图书馆", "第一教学楼", "fly"));
        printResult("演示8：输入为空（容错）",
                system.navigate("   ", "第一教学楼", "walk"));
    }

    private static void printResult(String title, String result) {
        System.out.println("── " + title);
        System.out.println(result);
        System.out.println();
    }
}
