package campusnav;

/**
 * 校园导航系统演示入口。
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("========== 校园导航系统 ==========");
        NavigationSystem system = new NavigationSystem();

        print(system.navigate("钟楼", "图书馆", "walk"));
        print(system.navigate("钟楼", "体育馆", "bus"));
        print(system.navigate("食堂", "宿舍", "步行"));
        print(system.navigate("钟楼", "不存在的地方", "walk"));
        print(system.navigate("钟楼", "钟楼", "walk"));
        print(system.navigate("钟楼", "图书馆", "fly"));
    }

    private static void print(String result) {
        System.out.println(result);
    }
}
