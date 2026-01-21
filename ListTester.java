public class ListTester {
    public static void main(String[] args) {
        testAddFirst();
        testToString();
        testIndexOf();
        testGet();
        testRemove();
        testUpdate();
    }

    public static void testAddFirst() {
        List list = new List();
        list.addFirst('a');
        list.addFirst('b');
        if (list.getSize() == 2 && list.getFirst().chr == 'b') {
            System.out.println("Test addFirst result: PASSED");
        } else {
            System.out.println("Test addFirst result: FAILED");
        }
    }

    public static void testToString() {
        List list = new List();
        if (list.toString().equals("()")) {
            list.addFirst('a');
            String str = list.toString();
            if (str.contains("a") && str.length() >= 3) { 
                System.out.println("Test toString result: PASSED");
                return;
            }
        }
        System.out.println("Test toString result: FAILED");
    }

    public static void testIndexOf() {
        List list = new List();
        list.addFirst('a');
        list.addFirst('b');
        if (list.indexOf('a') == 1 && list.indexOf('b') == 0 && list.indexOf('c') == -1) {
            System.out.println("Test indexOf result: PASSED");
        } else {
            System.out.println("Test indexOf result: FAILED");
        }
    }

    public static void testGet() {
        List list = new List();
        list.addFirst('a');
        if (list.get(0).chr == 'a') {
            System.out.println("Test get result: PASSED");
        } else {
            System.out.println("Test get result: FAILED");
        }
    }

    public static void testRemove() {
        List list = new List();
        list.addFirst('a');
        list.remove('a');
        if (list.getSize() == 0) {
            System.out.println("Test remove result: PASSED");
        } else {
            System.out.println("Test remove result: FAILED");
        }
    }

    public static void testUpdate() {
        List list = new List();
        list.update('a');
        list.update('a');
        if (list.getSize() == 1 && list.get(0).count == 2) {
            System.out.println("Test update result: PASSED");
        } else {
            System.out.println("Test update result: FAILED");
        }
    }
}