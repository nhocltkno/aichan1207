package Service;

import Entity.Item;
import Entity.Supply;

import javax.swing.*;
import java.sql.*;
import java.util.Vector;

public class Service {
    String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String url = "jdbc:sqlserver://AICHAN\\SQLEXPRESS:1433; databaseName=ItemDB; "
            + " user=sa; password=12071999";

    public Connection openConnection() throws Exception{
        Class.forName(driver);
        return DriverManager.getConnection(url);
    }

    public Vector<Item> getAllItems() throws Exception{
        Vector<Item> list = new Vector<>();
        String query = "Select * From Items";

        try(Connection c = openConnection(); Statement st =c.createStatement()){
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                String code = rs.getString("itemCode");
                String name = rs.getString("itemName");
                String supCode = rs.getString("supCode");
                String unit = rs.getString("unit");
                int price = rs.getInt("price");
                String supply = rs.getString("supplying");
                boolean isSupply = supply.equals("1");
                Item item = new Item(code,name,supCode,unit,price,isSupply);
                list.add(item);
            }
        }
        return list;
    }

    public Vector<Supply> getAllSupply() throws Exception{
        Vector<Supply> list = new Vector<>();
        String query = "Select * From Suppliers";
        try(Connection c = openConnection(); Statement st = c.createStatement()){
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                String code = rs.getString("SupCode");
                String name =rs.getString("SupName");
                String address = rs.getString("Address");
                boolean collaborate = rs.getString("colloborating").equals("1");
                Supply supply = new Supply(code,name,address,collaborate);
                list.add(supply);
            }
        }
        return list;
    }

    public int checkItemByCode(String code) throws Exception {
        String query ="Select * From Items Where itemCode = ?";
        try (Connection c = openConnection(); PreparedStatement st = c.prepareStatement(query)){
            st.setString(1,code);
            ResultSet rs =st.executeQuery();
            if (rs.next()){
                return 1;
            }
        }
        return 0;
    }

    public int checkSupplyByCode(String code) throws Exception{
        String query = "Select * From Suppliers Where SupCode = ?";
        try (Connection c = openConnection(); PreparedStatement preparedStatement = c.prepareStatement(query)){
            preparedStatement.setString(1,code);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return 1;
            }
        }
        return 0;
    }

    public void insertItem(Item item) throws Exception{
        String query ="Insert into Items Values(?,?,?,?,?,?)";
        try (Connection c =openConnection();PreparedStatement pr = c.prepareStatement(query)){
            pr.setString(1,item.getItemCode());
            pr.setString(2,item.getItemName());
            pr.setString(3,item.getSupCode());
            pr.setString(4,item.getUnit());
            pr.setInt(5,item.getPrice());
            pr.setBoolean(6,item.isSupplying());
            pr.executeUpdate();
        }
    }

    public void insertSupply(Supply supply) throws Exception{
        String query ="Insert into Suppliers Values(?,?,?,?)";
        try (Connection c = openConnection(); PreparedStatement ps = c.prepareStatement(query)){
            ps.setString(1,supply.getSupCode());
            ps.setString(2,supply.getSupName());
            ps.setString(3,supply.getAddress());
            ps.setBoolean(4,supply.isCollaborating());
            ps.executeUpdate();
        }
    }

    public void updateItem(Item item) throws Exception{
        String query = "Update Items Set itemName = ?, supCode = ?, unit = ?, price = ?, supplying = ? Where itemCode = ?";
        try (Connection c = openConnection(); PreparedStatement pr = c.prepareStatement(query)){
            pr.setString(1,item.getItemName());
            pr.setString(2,item.getSupCode());
            pr.setString(3,item.getUnit());
            pr.setInt(4,item.getPrice());
            pr.setBoolean(5,item.isSupplying());
            pr.setString(6,item.getItemCode());
            pr.executeUpdate();
        }
    }

    public void updateSupply(Supply supply) throws Exception{
        String query = "Update Suppliers Set SupName = ?, Address = ?, colloborating = ? Where SupCode = ?";
        try (Connection c = openConnection(); PreparedStatement ps =c.prepareStatement(query)){
            ps.setString(1,supply.getSupName());
            ps.setString(2,supply.getAddress());
            ps.setBoolean(3,supply.isCollaborating());
            ps.setString(4,supply.getSupCode());
            ps.executeUpdate();
        }
    }

    public void deleteItem(String code) throws Exception{
        String query = "Delete From Items Where itemCode = ?";
        try (Connection c =openConnection(); PreparedStatement ps = c.prepareStatement(query)){
            ps.setString(1,code);
            ps.executeUpdate();
        }
    }

    public int deleteSupply(String code) throws Exception {
        Vector<Item> items = getAllItems();
        for (Item item:items){
            if (item.getSupCode().equals(code)){
                JOptionPane.showMessageDialog(null,"You must delete all the item using this code!");
                return 1;
            }
        }
        String query = "Delete From Suppliers Where SupCode = ?";
        try (Connection c = openConnection(); PreparedStatement ps  = c.prepareStatement(query)){
            ps.setString(1,code);
            ps.executeUpdate();
        }
        return 0;
    }
}
