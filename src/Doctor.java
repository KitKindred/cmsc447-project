public class Doctor extends Profession {

    private boolean hadAttendingWeek;

    public Doctor(int id, int type, String name){
        super(id, type, name);
        hadAttendingWeek = false;
    }

    public boolean getAttending() {
        return hadAttendingWeek;
    }

}
