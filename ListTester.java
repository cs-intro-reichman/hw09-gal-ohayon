public class ListTester {
    public static void main(String[] args) {
        String methodName = args[0];
        boolean result = false;

        switch (methodName) {
            case "addFirst":
                result = testAddFirst();
                System.out.println("Test addFirst result: " + (result ? "PASSED" : "FAILED"));
                break;
            case "toString":
                result = testToString();
                System.out.println("Test toString result: " + (result ? "PASSED" : "FAILED"));
                break;
            case "indexOf":
                result = testIndexOf();
                System.out.println("Test indexOf result: " + (result ? "PASSED" : "FAILED"));
                break;
            case "get":
                result = testGet();
                System.out.println("Test get result: " + (result ? "PASSED" : "FAILED"));
                break;
            case "remove":
                result = testRemove();
                System.out.println("Test remove result: " + (result ? "PASSED" : "FAILED"));
                break;
            case "update":
                result = testUpdate();
                System.out.println("Test update result: " + (result ? "PASSED" : "FAILED"));
                break;
            default:
                break;
        }
    }

    public static boolean testAddFirst() {
        List list = new List();
        list.addFirst('a');
        list.addFirst('b');
        return list.getSize() == 2 && list.getFirst().chr == 'b';
    }

    public static boolean testToString() {
        List list = new List();
        if (!list.toString().equals("()")) return false;
        list.addFirst('a');
        String str = list.toString();
        return str.contains("a") && str.length() >= 3;
    }

    public static boolean testIndexOf() {
        List list = new List();
        list.addFirst('a');
        list.addFirst('b');
        return list.indexOf('a') == 1 && list.indexOf('b') == 0 && list.indexOf('c') == -1;
    }

    public static boolean testGet() {
        List list = new List();
        list.addFirst('a');
        return list.get(0).chr == 'a';
    }

    public static boolean testRemove() {
        List list = new List();
        list.addFirst('a');
        list.remove('a');
        return list.getSize() == 0;
    }

    public static boolean testUpdate() {
        List list = new List();
        list.update('a');
        list.update('a');
        return list.getSize() == 1 && list.get(0).count == 2;
    }
}
