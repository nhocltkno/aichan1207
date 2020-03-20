package View;

import Entity.Item;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class ItemTableModel<E> extends AbstractTableModel {
    Vector<Item> data = new Vector<>();
    Vector<Item> currentData = new Vector<>();
    String[] header;
    int[] index;

    public ItemTableModel(String[] header, int[] index){
        this.header = new String[header.length];
        this.index = new int[index.length];
        System.arraycopy(header, 0, this.header, 0, header.length);
        System.arraycopy(index, 0, this.index, 0, index.length);
    }

    public Vector<Item> getData() {
        return data;
    }

    public String getColumnName( int column){
        return (column>=0 && column<header.length)? header[column] :"";
    }

    @Override
    public int getRowCount() {
        return currentData.size();
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public Object getValueAt( int i, int i1 ) {
        if (i<0 || i>=data.size() || i1<0 || i1>=header.length) return null;
        Item item = currentData.get(i);
        switch (index[i1]){
            case 0: return item.getItemCode();
            case 1: return item.getItemName();
            case 2: return item.getSupCode();
            case 3: return item.getUnit();
            case 4: return item.getPrice();
            case 5: {
                if (item.isSupplying()){
                    return "True";
                }
                return "False";
            }
        }
        return null;
    }

    public void setData(Vector<Item> data) {
        this.data = data;
    }

    public void paging(int left, int right){
        this.currentData = new Vector<>(data.subList(left,right));
    }
}
