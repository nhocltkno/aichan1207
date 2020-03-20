package Entity;

public class Supply {
    private String supCode;
    private String supName;
    private String address;
    private boolean collaborating;

    public Supply( String supCode, String supName, String address, boolean collaborating ) {
        this.supCode = supCode;
        this.supName = supName;
        this.address = address;
        this.collaborating = collaborating;
    }

    public Supply(Supply supply){
        this.supCode = supply.getSupCode();
        this.supName = supply.getSupName();
        this.address = supply.getAddress();
        this.collaborating = supply.isCollaborating();
    }

    public String getSupCode() {
        return supCode;
    }

    public void setSupCode( String supCode ) {
        this.supCode = supCode;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName( String supName ) {
        this.supName = supName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public boolean isCollaborating() {
        return collaborating;
    }

    public void setCollaborating( boolean collaborating ) {
        this.collaborating = collaborating;
    }

    @Override
    public String toString() {
        return supName;
    }
}
