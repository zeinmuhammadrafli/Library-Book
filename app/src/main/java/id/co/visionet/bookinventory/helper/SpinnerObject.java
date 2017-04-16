package id.co.visionet.bookinventory.helper;

public class SpinnerObject {
    private int id;
    private String name;

    public SpinnerObject(int paramId, String paramName) {
        this.id = paramId;
        this.name = paramName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return name;
    }
}
