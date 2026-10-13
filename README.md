# java-repo-240405110

沈阳工业大学《Java 面向对象编程实践》实验仓库 —— **校园导航系统**（Lab Practice 2）。

- 作者：许凯玉　学号：240405110　班级：计算机科学与技术 2401
- 开发环境：JDK 8 及以上（本机验证使用 JDK 25）、JUnit 4.13.2、Eclipse / IDEA
- 实验要求：仅使用封装、继承、多态、抽象类等面向对象基本原理，不使用集合框架、设计模式等高级技术。

## 功能简介

- 以邻接矩阵保存校园地点与道路，用 Dijkstra 算法计算两点间最短路线（数组实现，不依赖集合框架）；
- 用 if-else 实现多方案导航（步行 / 校车），返回全程里程、预计时间与途经点；
- 控制台菜单交互，并支持 `--demo` 演示模式。

## 目录结构

```
java-repo-240405110
├── src/edu/sut/campus      业务源码
│   ├── Location.java           地点实体（私有字段 + 访问方法）
│   ├── CampusMap.java          校园地图（邻接矩阵 + Dijkstra）
│   ├── NavigationSystem.java   导航系统（if-else 多方案）
│   └── CampusNavApp.java       主程序（控制台菜单 + 演示模式）
├── testsrc/edu/sut/campus  JUnit 4 单元测试
├── lib/                    junit-4.13.2.jar、hamcrest-core-1.3.jar
└── README.md
```

## 编译与运行

```bash
# 编译
javac -encoding UTF-8 -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" -d bin $(find src testsrc -name "*.java")

# 运行主程序（交互式菜单）
java -cp bin edu.sut.campus.CampusNavApp

# 演示模式（连续输出多条导航结果）
java -cp bin edu.sut.campus.CampusNavApp --demo
```

在 Eclipse 中：项目右键 → New → Source Folder 建立 `testsrc`，Properties → Java Build Path → Libraries → Add JARs 引入 `lib` 下两个 jar。

> 提示：如果控制台中文乱码，请将 IDE 的文本编码设置为 UTF-8（Eclipse：Window → Preferences → General → Workspace → Text file encoding → UTF-8）。

## 运行单元测试

```bash
java -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore edu.sut.campus.CampusMapTest edu.sut.campus.NavigationSystemTest
```

## 版本迭代记录

| 提交 | 说明 |
| ---- | ---- |
| 1 | init: 校园导航系统初始版本（if-else 实现步行/校车两种方案） |
| 2 | style: 优化提示语格式与代码结构，增强输入容错（空输入/大小写/首尾空格），补充边界测试 |
