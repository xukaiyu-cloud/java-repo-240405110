package edu.sut.campus;

/**
 * 校园地点实体类。
 * 体现封装思想：地点的编号、名称、描述都设为私有字段，
 * 只能通过构造方法和公共访问方法修改、读取。
 */
public class Location {

    private final int id;             // 地点编号
    private final String name;        // 地点名称
    private final String description; // 地点描述

    public Location(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Location other = (Location) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
