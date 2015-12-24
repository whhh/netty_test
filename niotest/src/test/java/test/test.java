package test;

import java.util.LinkedList;

/**
 * Created by Administrator on 2015/12/24.
 */
public class test {
    public static void main(String[] args) {
        LinkedList  list = new LinkedList<String>();
        LinkedList  list1 = new LinkedList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        System.out.println(list.size());
        System.out.println(list.removeFirst());
        System.out.println(list.size());
        System.out.println(list1.isEmpty());

    }
}
