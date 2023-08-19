public class App {

    public static void main(String[] args) throws Exception {
        TwoFourTree tft = new TwoFourTree();
        tft.addValue(2);
        tft.addValue(3);
        tft.addValue(5);
        tft.addValue(7);
        tft.addValue(11);
        tft.addValue(13);
        tft.addValue(17);
        tft.addValue(19);
        tft.addValue(23);
        tft.addValue(29);
        tft.addValue(31);
        tft.addValue(37);
        tft.addValue(41);
        tft.addValue(43);
        tft.addValue(47);
        tft.addValue(53);
        tft.addValue(59);
        tft.addValue(67);
        tft.addValue(71);
        tft.addValue(73);
        tft.addValue(79);
        tft.addValue(83);
        tft.addValue(89);
        tft.addValue(97);

        tft.printInOrder();
        tft.deleteValue(37);
        System.out.println("\nWithout 37:");
        tft.printInOrder();
        tft.deleteValue(73);
        System.out.println("\nWithout 73:");
        tft.printInOrder();
        tft.deleteValue(97);
        System.out.println("\nWithout 97:");
        tft.printInOrder();
    }
}
