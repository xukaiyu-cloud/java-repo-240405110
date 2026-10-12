package campusnav;

/**
 * 校园导航系统演示入口。
 */
public class Main {

    public static void main(String[] args) {
        NavigationSystem system = new NavigationSystem();

        System.out.println(system.navigate("钟楼", "图书馆", "walk"));
        System.out.println(system.navigate("钟楼", "体育馆", "bus"));
        System.out.println(system.navigate("食堂", "宿舍", "步行"));
        System.out.println(system.navigate("钟楼", "不存在的地方", "walk"));
        System.out.println(system.navigate("钟楼", "钟楼", "walk"));
        System.out.println(system.navigate("钟楼", "图书馆", "fly"));
    }
}
