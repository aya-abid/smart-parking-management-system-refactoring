
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public class manageparkingrent extends javax.swing.JFrame {

    ArrayList<ParkingRent> al = new ArrayList<>();
    ParkingTableModel ptm;

    public manageparkingrent() {
        initComponents();
        setSize(780, 600);
        setVisible(true);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((d.width) - this.getWidth())/2;
        int y = (int)((d.height) - this.getWidth())/2;
        setLocation(x,y);
        cbvehicle.removeAllItems();
        cbvehicle.addItem("Two Wheeler");
        cbvehicle.addItem("Four Wheeler");
        ptm = new ParkingTableModel();
        jtable.setModel(ptm);
        loaddata();
    }

    void loaddata() {
        try {
            HttpResponse<String> res = Unirest.get("http://localhost:8888/GetParkingrent").asString();
            String response = res.getBody();
            StringTokenizer st = new StringTokenizer(response, "~!@");
            al.clear();
            System.out.println(st.countTokens());
            int n = st.countTokens();
            for (int i = 1; i <= n; i++) {
                String[] row = st.nextToken().split(",");
                String vtype = row[0];
                int rent = Integer.parseInt(row[1]);
                al.add(new ParkingRent(vtype, rent));
            }
            ptm.fireTableDataChanged();
        } catch (UnirestException ex) {
            Logger.getLogger(manageparkingrent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbparking = new javax.swing.JLabel();
        btaddrent = new javax.swing.JButton();
        cbvehicle = new javax.swing.JComboBox<>();
        lbvehicle = new javax.swing.JLabel();
        lbrent = new javax.swing.JLabel();
        tfrent = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtable = new javax.swing.JTable();
        btdelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        lbparking.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lbparking.setText("     Parking rent");
        getContentPane().add(lbparking);
        lbparking.setBounds(300, 20, 180, 40);

        btaddrent.setText("Add rent");
        btaddrent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btaddrentActionPerformed(evt);
            }
        });
        getContentPane().add(btaddrent);
        btaddrent.setBounds(150, 300, 130, 30);

        cbvehicle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbvehicle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Two Wheeler", "Four Wheeler" }));
        cbvehicle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbvehicleItemStateChanged(evt);
            }
        });
        cbvehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbvehicleActionPerformed(evt);
            }
        });
        getContentPane().add(cbvehicle);
        cbvehicle.setBounds(170, 90, 110, 30);

        lbvehicle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbvehicle.setText("         Vehicle type");
        getContentPane().add(lbvehicle);
        lbvehicle.setBounds(30, 90, 110, 20);

        lbrent.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbrent.setText("Rent per hour");
        getContentPane().add(lbrent);
        lbrent.setBounds(50, 180, 110, 30);
        getContentPane().add(tfrent);
        tfrent.setBounds(160, 180, 120, 30);

        jtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "vehicle type", "rent per hour"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtable);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(400, 60, 330, 200);

        btdelete.setText("Delete");
        btdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btdelete);
        btdelete.setBounds(510, 270, 80, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbvehicleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbvehicleActionPerformed


    }//GEN-LAST:event_cbvehicleActionPerformed

    private void cbvehicleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbvehicleItemStateChanged

    }//GEN-LAST:event_cbvehicleItemStateChanged

    private void btaddrentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btaddrentActionPerformed
        String item = cbvehicle.getSelectedItem().toString();
        int rent = Integer.parseInt(tfrent.getText());
        if (item == "" || tfrent.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "all fields are mandatory");
        } else {
            try {
                HttpResponse<String> res = Unirest.get("http://localhost:8888//Parkingrent")
                        .queryString("vehicletype", item)
                        .queryString("rentperhour", rent)
                        .asString();
                String response = res.getBody();
                if (response.equals("success")) {
                    JOptionPane.showMessageDialog(rootPane, "Parking Rent Added");
                    loaddata();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Duplicate Vehicle Type");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btaddrentActionPerformed

    private void btdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeleteActionPerformed
        try {
            String vehicletype = "";
            int index = jtable.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(rootPane, "Please select a record");
            } else {
                vehicletype = al.get(index).vehicletype;
                HttpResponse<String> res = Unirest.get("http://localhost:8888/deleteparkingrent")
                        .queryString("vehicletype", vehicletype)
                        .asString();
                String response = res.getBody();
                if (response.equals("success")) {
                    JOptionPane.showMessageDialog(rootPane, "Parking Rent Deleted");
                    loaddata();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error Delete Parking Rent");
                }
            }
            }catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_btdeleteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(manageparkingrent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(manageparkingrent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(manageparkingrent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(manageparkingrent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new manageparkingrent().setVisible(true);
            }
        });
    }

    class ParkingRent {

        public ParkingRent() {
        }

        public ParkingRent(String vehicletype, int rentperhour) {
            this.vehicletype = vehicletype;
            this.rentperhour = rentperhour;
        }

        String vehicletype;
        int rentperhour;
    }

    class ParkingTableModel extends AbstractTableModel {

        String[] cnames = {"Vehicle Type", "Rent Per Hour"};

        @Override
        public int getRowCount() {
            return al.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return al.get(rowIndex).vehicletype;
                case 1:
                    return al.get(rowIndex).rentperhour;
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(int column) {
            return cnames[column];
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btaddrent;
    private javax.swing.JButton btdelete;
    private javax.swing.JComboBox<String> cbvehicle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtable;
    private javax.swing.JLabel lbparking;
    private javax.swing.JLabel lbrent;
    private javax.swing.JLabel lbvehicle;
    private javax.swing.JTextField tfrent;
    // End of variables declaration//GEN-END:variables
}
