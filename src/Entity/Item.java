package Entity;

public class Item {
    private String itemCode;
    private String itemName;
    private String supCode;
    private String unit;
    private int price;
    private boolean supplying;

    public Item( String itemCode, String itemName, String supCode, String unit, int price, boolean supplying ) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.supCode = supCode;
        this.unit = unit;
        this.price = price;
        this.supplying = supplying;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName( String itemName ) {
        this.itemName = itemName;
    }

    public String getSupCode() {
        return supCode;
    }

    public void setSupCode( String supCode ) {
        this.supCode = supCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit( String unit ) {
        this.unit = unit;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice( int price ) {
        this.price = price;
    }

    public boolean isSupplying() {
        return supplying;
    }

    public void setSupplying( boolean supplying ) {
        this.supplying = supplying;
    }

}
