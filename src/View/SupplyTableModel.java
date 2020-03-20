package View;

import Entity.Supply;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class SupplyTableModel<E> extends AbstractTableModel {
    Vector<Supply> data = new Vector<>();
    Vector<Supply> currentData = new Vector<>();
    String[] header;
    int[] index;

    public SupplyTableModel(String[] header, int[] index){
        this.header = new String[header.length];
        this.index = new int[index.length];
        System.arraycopy(header,0,this.header,0,header.length);
        System.arraycopy(index,0,this.index,0,index.length);
    }

    public Vector<Supply> getData() {
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
        Supply supply = currentData.get(i);
        switch (index[i1]){
            case 0: return supply.getSupCode();
            case 1: return supply.getSupName();
            case 2: return supply.getAddress();
            case 3 :{
                if (supply.isCollaborating()){
                    return "True";
                } else {
                    return "False";
                }
            }
        }
        return null;
    }

    public void setData( Vector<Supply> data ) {
        this.data = data;
    }

    public void paging(int left, int right){
        currentData = new Vector<>(data.subList(left,right));
    }
}
