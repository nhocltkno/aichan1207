package View;

import Entity.Item;
import Entity.Supply;
import Service.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class MainClass extends JFrame {
    JPanel panelMain = new JPanel();
    JTable tableItem = new JTable();
    JTextField txtItemCode = new JTextField();
    JTextField txtItemName = new JTextField();
    JTextField txtItemUnit = new JTextField();
    JTextField txtItemPrice = new JTextField();
    JCheckBox checkSupply = new JCheckBox();
    JComboBox<Supply> supplier = new JComboBox<>();
    JButton addItemBtn = new JButton("Add");
    JButton saveItemBtn = new JButton("Save");
    JButton removeItemBtn = new JButton("Remove");

    JTable tableSupply = new JTable();
    JTextField txtSupplyCode = new JTextField();
    JTextField txtSupplyName = new JTextField();
    JTextField txtSupplyAddress = new JTextField();
    JCheckBox checkCollaborate = new JCheckBox();
    JButton addSupplyBtn = new JButton("Add");
    JButton saveSupplyBtn = new JButton("Save");
    JButton removeSupplyBtn = new JButton("Remove");
    JLabel labelItem = new JLabel("0/0");
    JLabel labelSupply = new JLabel("0/0");
    JButton preItem = new JButton("Previous");
    JButton nextItem = new JButton("Next");

    JButton preSupply = new JButton("Previous");
    JButton nextSupply = new JButton("Next");

    ItemTableModel<Item> itemModel;
    SupplyTableModel<Supply> supplyModel;

    Vector<Item> itemData;
    int currentPageItem =0;

    Vector<Supply> supplyData;
    int currentPageSupply =0;

    Service service = new Service();

    boolean addNewItem =true;
    boolean addNewSupply = true;

    public static void main( String[] args ) {
        MainClass m = new MainClass();
        m.setSize(800,600);
        m.setDefaultCloseOperation(EXIT_ON_CLOSE);
        m.setVisible(true);
        m.setResizable(false);
    }

    public MainClass(){
        setup();
        String[] header = {"Code", "Name", "Address", "Collaborate"};
        int[] index ={0, 1, 2, 3};
        supplyModel = new SupplyTableModel<>(header,index);
        this.tableSupply.setModel(supplyModel);

        String[] header1 ={"Code","Name","Supplier","Unit","Price","Supplying"};
        int[] index1 = {0, 1, 2, 3, 4, 5};
        itemModel = new ItemTableModel<>(header1,index1);
        this.tableItem.setModel(itemModel);
        loadData();

        supplyModel.setData(supplyData);
        supplier.setModel(new DefaultComboBoxModel<>(supplyData));
        pageTableSupply(currentPageSupply);
        itemModel.setData(itemData);
        pageTableItem(currentPageItem);

        this.tableSupply.updateUI();
        this.tableItem.updateUI();

        tableSupply.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent e ) {
                int choose = tableSupply.getSelectedRow();
                Supply supply = supplyData.elementAt(choose);
                txtSupplyCode.setText(supply.getSupCode());
                txtSupplyName.setText(supply.getSupName());
                txtSupplyAddress.setText(supply.getAddress());
                checkCollaborate.setSelected(supply.isCollaborating());
                txtSupplyCode.setEditable(false);
            }
        });

        tableItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent e ) {
                int choose = tableItem.getSelectedRow();
                Item item = itemData.elementAt(choose);
                txtItemCode.setEditable(false);
                txtItemCode.setText(item.getItemCode());
                txtItemName.setText(item.getItemName());
                txtItemPrice.setText(item.getPrice()+"");
                txtItemUnit.setText(item.getUnit());
                for (int i =0;i<supplier.getItemCount();i++){
                    if (supplier.getItemAt(i).getSupCode().equals(item.getSupCode())){
                        supplier.setSelectedIndex(i);
                        break;
                    }
                }
                checkSupply.setSelected(item.isSupplying());
                addNewItem = false;
            }
        });

        tableSupply.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent e ) {
                int choose = tableSupply.getSelectedRow();
                Supply supply = supplyData.elementAt(choose);
                txtSupplyCode.setEditable(false);
                txtSupplyCode.setText(supply.getSupCode());
                txtSupplyName.setText(supply.getSupName());
                txtSupplyAddress.setText(supply.getAddress());
                checkCollaborate.setSelected(supply.isCollaborating());
                addNewSupply =false;
            }
        });
        nextItem.addActionListener(actionEvent -> pageTableItem(currentPageItem+1));
        preItem.addActionListener(actionEvent -> pageTableItem(currentPageItem-1));
        nextSupply.addActionListener(actionEvent -> pageTableSupply(currentPageSupply+1));
        preSupply.addActionListener(actionEvent -> pageTableSupply(currentPageSupply-1));

        addItemBtn.addActionListener(actionEvent -> {
            txtItemCode.setEditable(true);
            txtItemCode.setText("");
            txtItemCode.requestFocus();
            txtItemName.setText("");
            txtItemPrice.setText("");
            txtItemUnit.setText("");
            supplier.setEnabled(true);
            checkSupply.setSelected(false);
            addNewItem = true;
        });

        addSupplyBtn.addActionListener(actionEvent -> {
            txtSupplyCode.setEditable(true);
            txtSupplyName.setText("");
            txtSupplyCode.setText("");
            txtSupplyAddress.setText("");
            checkCollaborate.setSelected(false);
            txtSupplyCode.requestFocus();
            addNewSupply = true;
        });

        saveSupplyBtn.addActionListener(actionEvent -> {
            if (addNewSupply) {
                String code = txtSupplyCode.getText();
                if ( !code.matches("[a-zA-Z]{1,5}|") ) {
                    JOptionPane.showMessageDialog(this, "Code just allow characters only min:1, max: 5");
                    return;
                }
                try {
                    if ( service.checkSupplyByCode(code) == 1 ) {
                        JOptionPane.showMessageDialog(this, "Duplicated Code!");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String name = txtSupplyName.getText();
                if ( !name.matches("[a-zA-Z\\s]{3,50}") ) {
                    JOptionPane.showMessageDialog(this, "Name just allow characters only min:3, max:50");
                    return;
                }
                String address = txtSupplyAddress.getText();
                boolean isCheck = checkCollaborate.isSelected();
                Supply supply = new Supply(code,name,address,isCheck);
                supplyData.add(supply);
                try {
                    service.insertSupply(supply);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pageTableSupply(currentPageSupply);
            } else {
                int choose = tableSupply.getSelectedRow();
                Supply supply = supplyData.elementAt(choose);
                String name = txtSupplyName.getText();
                if ( !name.matches("\\d\\s{3,50}") ) {
                    JOptionPane.showMessageDialog(this, "Name just allow characters only min:3, max:50");
                    return;
                }
                String address = txtSupplyAddress.getText();
                boolean isCheck = checkCollaborate.isSelected();
                supply.setSupName(name);
                supply.setAddress(address);
                supply.setCollaborating(isCheck);
                try {
                    service.updateSupply(supply);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pageTableSupply(currentPageSupply);
                tableSupply.clearSelection();
                addNewSupply = false;
            }
        });

        removeSupplyBtn.addActionListener(actionEvent -> {
            int choose = tableSupply.getSelectedRow();
            if (choose<0){
                JOptionPane.showMessageDialog(this,"You must choose a row to delete!");
            } else {
                if (JOptionPane.showConfirmDialog(this,"Do you want to delete that row?","DELETE?",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                    try {
                        if (service.deleteSupply(supplyData.get(choose).getSupCode())==0) {
                            supplyData.remove(choose);
                            pageTableSupply(currentPageSupply);
                        } else {
                            tableSupply.clearSelection();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        saveItemBtn.addActionListener(actionEvent -> {
            if (addNewItem){
                String code = txtItemCode.getText();
                if (!code.matches("(E)\\d{4}")){
                    JOptionPane.showMessageDialog(this,"Code must follow format [Exxxx]","CODE WRONG",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                try {
                    if (service.checkItemByCode(code)==1){
                        JOptionPane.showMessageDialog(this,"Code is duplicated!","Duplicated Code",JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String name =txtItemName.getText();
                if (!name.matches("[0-9a-zA-Z\\s]{3,30}")){
                    JOptionPane.showMessageDialog(this, "Name just allow digits and characters min:3, max:30", "NAME WRONG!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                int price ;
                try {
                    price = Integer.parseInt(txtItemPrice.getText());
                } catch (Exception e){
                    JOptionPane.showMessageDialog(this,"Price must be a positive number","PRICE WRONG",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String unit = txtItemUnit.getText();
                Supply supply = (Supply) supplier.getSelectedItem();
                boolean checkSupply1 =checkSupply.isSelected();
                Item item = new Item(code,name,supply.getSupCode(),unit,price,checkSupply1);
                try {
                    service.insertItem(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                itemData.add(item);
                pageTableItem(currentPageItem);
            } else {
                int choose = tableItem.getSelectedRow();
                String code = itemData.get(choose).getItemCode();
                if (!code.matches("(E)\\d{4}")){
                    JOptionPane.showMessageDialog(this,"Code must follow format [Exxxx]","CODE WRONG",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String name =txtItemName.getText();
                if (!name.matches("[0-9a-zA-Z\\s]{3,30}")){
                    JOptionPane.showMessageDialog(this, "Name just allow digits and characters min:3, max:30", "NAME WRONG!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                int price = 0;
                try {
                    price = Integer.parseInt(txtItemPrice.getText());
                } catch (Exception e){
                    JOptionPane.showMessageDialog(this,"Price must be a positive number","PRICE WRONG",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String unit = txtItemUnit.getText();
                Supply supply = (Supply) supplier.getSelectedItem();
                boolean checkSupply1 =checkSupply.isSelected();
                Item item = itemData.get(choose);
                item.setItemName(name);
                item.setPrice(price);
                item.setSupCode(supply.getSupCode());
                item.setSupplying(checkSupply1);
                item.setUnit(unit);
                pageTableItem(currentPageItem);
                try {
                    service.updateItem(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tableItem.clearSelection();
                addNewItem =false;
            }
        });

        removeItemBtn.addActionListener(actionEvent -> {
            int choose = tableItem.getSelectedRow();
            if (choose<0){
                JOptionPane.showMessageDialog(this,"You must choose a row to delete");
            }
            else {
                if (JOptionPane.showConfirmDialog(this,"Do you want to delete that items?","Answer?",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    try {
                        service.deleteItem(itemData.get(choose).getItemCode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    itemData.remove(choose);
                    pageTableItem(currentPageItem);
                }
            }
        });
    }

    public void pageTableItem( int page ) {
        int dataSize = itemModel.getData().size();
        int maxPage = Math.max((int) Math.ceil(dataSize / 5.0), 1) - 1;
        if ( page < 0 ) page = maxPage;
        if ( page > maxPage ) page = 0;
        currentPageItem = page;
        int left = ( currentPageItem ) * 5;
        int right = Math.min(( currentPageItem + 1 ) * 5, dataSize);
        itemModel.paging(left, right);
        labelItem.setText(String.format("%s/%s", ( page + 1 ), ( maxPage + 1 )));
        tableItem.updateUI();
    }

    public void pageTableSupply( int page ) {
        int dataSize = supplyModel.getData().size();
        int maxPage = Math.max((int) Math.ceil(dataSize / 5.0), 1) - 1;
        if ( page < 0 ) page = maxPage;
        if ( page > maxPage ) page = 0;
        currentPageSupply = page;
        int left = ( currentPageSupply ) * 5;
        int right = Math.min(( currentPageSupply + 1 ) * 5, dataSize);
        supplyModel.paging(left, right);
        labelSupply.setText(String.format("%s/%s", ( page + 1 ), ( maxPage + 1 )));
        tableSupply.updateUI();
    }


    public void loadData(){
        try {
            supplyData = service.getAllSupply();
            itemData = service.getAllItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setup(){
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(800,599));
        panelMain.add(tabbedPane);

        {
            JPanel panelSupply = new JPanel();
            tabbedPane.add(panelSupply, "Manager Supplier");
            panelSupply.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setPreferredSize(new Dimension(450, 500));
            panelSupply.add(panel);
            JScrollPane scrollPane  = new JScrollPane();
            panel.add(scrollPane,BorderLayout.CENTER);
            scrollPane.add(tableSupply);
            scrollPane.setViewportView(tableSupply);

            JPanel panel11 = new JPanel();
            panel11.setLayout(new FlowLayout(FlowLayout.CENTER,10,0));
            panel.add(panel11,BorderLayout.SOUTH);

            panel11.add(preSupply);
            panel11.add(labelSupply);
            panel11.add(nextSupply);

            JPanel paneDetail = new JPanel();
            paneDetail.setPreferredSize(new Dimension(300, 500));
            panelSupply.add(paneDetail);
            paneDetail.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
            panel11 = new JPanel();
            panel11.setBorder(BorderFactory.createTitledBorder("Supply Details"));
            paneDetail.add(panel11);
            panel11.setPreferredSize(new Dimension(290, 200));

            JLabel label = new JLabel("Supply Code: ");
            panel11.setLayout(new FlowLayout(FlowLayout.LEFT));
            label.setPreferredSize(new Dimension(80, 40));
            panel11.add(label);

            txtSupplyCode.setPreferredSize(new Dimension(150, 20));
            panel11.add(txtSupplyCode);

            label = new JLabel("Supply Name: ");
            label.setPreferredSize(new Dimension(80, 40));
            panel11.add(label);

            txtSupplyName.setPreferredSize(new Dimension(150, 20));
            panel11.add(txtSupplyName);

            label = new JLabel("Address: ");
            label.setPreferredSize(new Dimension(80, 40));
            panel11.add(label);

            txtSupplyAddress.setPreferredSize(new Dimension(150, 20));
            panel11.add(txtSupplyAddress);

            label = new JLabel("Collaborate: ");
            label.setPreferredSize(new Dimension(80, 40));
            panel11.add(label);
            panel11.add(checkCollaborate);

            JPanel buttons = new JPanel();
            buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 30));
            paneDetail.add(buttons);
            buttons.setPreferredSize(new Dimension(290, 100));
            buttons.setBorder(BorderFactory.createTitledBorder(""));

            addItemBtn.setPreferredSize(new Dimension(80, 30));
            buttons.add(addSupplyBtn);

            saveItemBtn.setPreferredSize(new Dimension(80, 30));
            buttons.add(saveSupplyBtn);

            removeItemBtn.setPreferredSize(new Dimension(80, 30));
            buttons.add(removeSupplyBtn);
        }

        {
            JPanel panelItem = new JPanel();
            tabbedPane.add(panelItem, "Manager Items");
            panelItem.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setPreferredSize(new Dimension(450, 500));
            panelItem.add(panel);
            JScrollPane scrollPane = new JScrollPane();
            panel.add(scrollPane,BorderLayout.CENTER);
            scrollPane.add(tableItem);
            scrollPane.setViewportView(tableItem);

            JPanel panel11 = new JPanel();
            panel.add(panel11,BorderLayout.SOUTH);
            panel11.setLayout(new FlowLayout(FlowLayout.CENTER,10,0));

            panel11.add(preItem);
            panel11.add(labelItem);
            panel11.add(nextItem);

            JPanel panelDetail = new JPanel();
            panelDetail.setPreferredSize(new Dimension(300, 500));
            panelItem.add(panelDetail);

            panelDetail.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
            panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder("Item Details"));
            panelDetail.add(panel);
            panel.setPreferredSize(new Dimension(290, 300));

            JLabel label = new JLabel("Item Code: ");
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            label.setPreferredSize(new Dimension(80, 40));
            panel.add(label);

            txtItemCode.setPreferredSize(new Dimension(150, 20));
            panel.add(txtItemCode);

            label = new JLabel("Item Name: ");
            label.setPreferredSize(new Dimension(80, 40));
            panel.add(label);

            txtItemName.setPreferredSize(new Dimension(150, 20));
            panel.add(txtItemName);

            label = new JLabel("Supplier: ");
            label.setPreferredSize(new Dimension(80, 40));
            panel.add(label);

            supplier.setPreferredSize(new Dimension(150, 20));
            panel.add(supplier);

            label = new JLabel("Unit: ");
            label.setPreferredSize(new Dimension(80, 40));
            panel.add(label);

            txtItemUnit.setPreferredSize(new Dimension(150, 20));
            panel.add(txtItemUnit);

            label = new JLabel("Price: ");
            label.setPreferredSize(new Dimension(80, 40));
            panel.add(label);

            txtItemPrice.setPreferredSize(new Dimension(150, 20));
            panel.add(txtItemPrice);

            label = new JLabel("Supplying: ");
            label.setPreferredSize(new Dimension(80, 40));
            panel.add(label);
            panel.add(checkSupply);

            JPanel buttons = new JPanel();
            buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 30));
            panelDetail.add(buttons);
            buttons.setPreferredSize(new Dimension(290, 100));
            buttons.setBorder(BorderFactory.createTitledBorder(""));

            addItemBtn.setPreferredSize(new Dimension(80, 30));
            buttons.add(addItemBtn);

            saveItemBtn.setPreferredSize(new Dimension(80, 30));
            buttons.add(saveItemBtn);

            removeItemBtn.setPreferredSize(new Dimension(80, 30));
            buttons.add(removeItemBtn);
        }


        Container c = getContentPane();
        c.add(panelMain);
    }
}
