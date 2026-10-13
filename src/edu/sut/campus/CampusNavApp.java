package edu.sut.campus;

import java.util.Scanner;

import edu.sut.campus.ifelse.IfElseNavigationSystem;

/**
 * 校园导航系统主程序（控制台界面）。
 *
 * 运行方式：
 *   java edu.sut.campus.CampusNavApp               进入交互式菜单（多态版本）
 *   java edu.sut.campus.CampusNavApp --demo        自动演示若干条导航（便于截图记录）
 *   java edu.sut.campus.CampusNavApp --demo-ifelse 用 if-else 旧版演示同一批用例（对比输出）
 */
public class CampusNavApp {

    /** 演示用例：{说明, 起点, 终点, 方式代码}，两种实现共用同一批用例。 */
    private static final String[][] DEMO_CASES = {
            {"图书馆 -> 第一教学楼（步行）", "图书馆", "第一教学楼", "walk"},
            {"南门 -> 第一教学楼（步行，多途经点）", "南门", "第一教学楼", "walk"},
            {"南门 -> 体育馆（校车）", "南门", "体育馆", "shuttle"},
            {"图书馆 -> 第一教学楼（骑行）", "图书馆", "第一教学楼", "bike"},
            {"图书馆 -> 第一教学楼（混合，短距离推荐步行）", "图书馆", "第一教学楼", "hybrid"},
            {"南门 -> 体育馆（混合，长距离推荐校车组合）", "南门", "体育馆", "hybrid"},
            {"方式代码不区分大小写（WALK）", "图书馆", "第一教学楼", "WALK"},
            {"起点与终点相同（边界情况）", "图书馆", "图书馆", "walk"},
            {"输入不存在的地点（容错）", "图书馆", "火星", "walk"},
            {"输入不支持的导航方式（容错）", "图书馆", "第一教学楼", "fly"},
            {"输入为空（容错）", "   ", "第一教学楼", "walk"},
    };

    public static void main(String[] args) {
        CampusMap map = createDemoCampus();
        if (args.length > 0 && "--demo".equals(args[0])) {
            runDemo(new NavigationSystem(map));
        } else if (args.length > 0 && "--demo-ifelse".equals(args[0])) {
            runDemoIfElse(new IfElseNavigationSystem(map));
        } else {
            runMenu(new NavigationSystem(map));
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

    /**
     * 交互式菜单：菜单项直接由已注册的导航方式生成，
     * 以后新增一种方式（增加一个子类并注册）时，本菜单无需修改。
     */
    private static void runMenu(NavigationSystem system) {
        NavigationMode[] modes = system.getModes();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("====== 校园导航系统 ======");
            for (int i = 0; i < modes.length; i++) {
                System.out.println("  " + (i + 1) + ". " + modes[i].getDisplayName()
                        + "导航（" + modes[i].getCode() + "）");
            }
            System.out.println("  0. 退出");
            System.out.print("请选择导航方式：");
            String choice = scanner.nextLine().trim();
            if ("0".equals(choice)) {
                System.out.println("感谢使用，再见！");
                break;
            }
            int index = -1;
            try {
                index = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                index = -1;
            }
            if (index < 1 || index > modes.length) {
                System.out.println("输入有误：请输入 0 ~ " + modes.length + " 之间的序号。");
                System.out.println();
                continue;
            }
            NavigationMode mode = modes[index - 1];
            System.out.println("已选择：" + mode.getDisplayName() + "导航");
            System.out.print("请输入起点名称：");
            String from = scanner.nextLine();
            System.out.print("请输入终点名称：");
            String to = scanner.nextLine();
            System.out.println(system.navigate(from, to, mode.getCode()));
            System.out.println();
        }
        scanner.close();
    }

    /** 演示模式：连续输出多条导航结果，便于截图记录。 */
    private static void runDemo(NavigationSystem system) {
        System.out.println("========== 校园导航系统 演示（抽象类 + 多态版本） ==========");
        for (int i = 0; i < DEMO_CASES.length; i++) {
            String[] item = DEMO_CASES[i];
            printResult(item[0], system.navigate(item[1], item[2], item[3]));
        }
    }

    /** 用 if-else 旧版执行同一批演示用例，便于对比两种实现的输出是否一致。 */
    private static void runDemoIfElse(IfElseNavigationSystem legacy) {
        System.out.println("========== 校园导航系统 演示（if-else 旧版本） ==========");
        for (int i = 0; i < DEMO_CASES.length; i++) {
            String[] item = DEMO_CASES[i];
            printResult(item[0], legacy.navigate(item[1], item[2], item[3]));
        }
    }

    private static void printResult(String title, String result) {
        System.out.println("── " + title);
        System.out.println(result);
        System.out.println();
    }
}
