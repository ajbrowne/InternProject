package main.model;

/**
 * Created by maharb on 6/11/14.
 */
public class Test {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Test{" +
                "name='" + name + '\'' +
                '}';
    }

    public Test(String name) {
        this.name = name;
    }

    public Test() {
    }

    private String name;
}
