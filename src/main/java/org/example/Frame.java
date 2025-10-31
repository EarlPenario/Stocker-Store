package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Frame extends JFrame {
    JTable productTable;
    Table table;
    JButton add,remove,update,checkExpiration;
    Container container;
    GridBagLayout layout;
    GridBagConstraints constraints;
    public Frame(){
        table=new Table();
        productTable=new JTable(table);

        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                Color rowColor = ((Table) table.getModel()).getRowColor(row);
                if (rowColor != null && !isSelected) {
                    c.setBackground(rowColor);
                } else if (!isSelected) {
                    c.setBackground(table.getBackground());
                }
                return c;
            }
        });

        add=new JButton("Add");
        remove=new JButton("Remove");
        update=new JButton("Update");
        checkExpiration=new JButton("Check Expiration");

        container=this.getContentPane();
        layout=new GridBagLayout();
        container.setLayout(layout);
        constraints=new GridBagConstraints();
        constraints.insets=new Insets(10,10,10,10);

        constraints.gridx=0;
        constraints.gridy=0;
        constraints.gridheight=1;
        constraints.gridwidth=1;
        constraints.weightx=1;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        container.add(add,constraints);

        constraints.gridx=1;
        constraints.gridy=0;
        constraints.gridheight=1;
        constraints.gridwidth=1;
        constraints.weightx=1;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        container.add(remove,constraints);

        constraints.gridx=2;
        constraints.gridy=0;
        constraints.gridheight=1;
        constraints.gridwidth=1;
        constraints.weightx=1;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        container.add(update,constraints);

        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        container.add(checkExpiration, constraints);

        constraints.gridx=0;
        constraints.gridy=1;
        constraints.gridheight=1;
        constraints.gridwidth=4;
        constraints.weightx=1;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        container.add(new JScrollPane(productTable),constraints);

        this.setTitle("Stocker Store");
        this.setVisible(true);
        this.setSize(800,600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
}
