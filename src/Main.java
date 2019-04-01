import java.util.HashMap;
import java.time.LocalDate;

public class Main {

    HashMap<Integer, Profession> employees;

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LocalDate d = LocalDate.of(2018, 11, 2);
        LocalDate d2 = LocalDate.of(2018, 11, 9);
        System.out.println(d.getDayOfYear());

        Schedule s = new Schedule(d, d2);
        s.createShifts();
        s.printShifts();
    }
}
