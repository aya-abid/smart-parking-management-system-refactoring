 

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import escpos.EscPos;
import escpos.EscPosConst;
import escpos.Style;
import escpos.barcode.BarCode;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import output.PrinterOutputStream;


public class parkingbayB extends javax.swing.JFrame implements ActionListener {

     ArrayList<ParkingSlot> al = new ArrayList<>();
    
    public parkingbayB() {
        initComponents();
        
          Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        
    }
     String eid;
     public parkingbayB (String empid) {
        initComponents();
        eid = empid;
         setVisible(true);
          Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
         lbempid.setText("Welcome, " + empid);
         load();
     }

    void load()
    {
        Component[] components = jpanel4.getComponents();
        Component[] components2 = jpanel6.getComponents();
        for(Component cp : components)
        {
           JButton btn =  (JButton)cp;
           btn.setBackground(Color.GREEN);
           btn.setOpaque(true);
           btn.addActionListener( this);
        }
        for(Component cp : components2)
        {
           JButton btn =  (JButton)cp;
           btn.setBackground(Color.GREEN);
           btn.setOpaque(true);
             btn.addActionListener( this);
        }
        checkbooked();
    }
     void checkbooked() {
        try {
            HttpResponse<String> res = Unirest.get("http://localhost:8888/getbookedslots")
                    .queryString("parkingbay", "Parking Bay B").asString();
            String response = res.getBody();
            if (response.isEmpty() == false) {
                StringTokenizer rows = new StringTokenizer(response, "~~~~");
                int n = rows.countTokens();
                Component[] components = jpanel4.getComponents();
                Component[] components2 = jpanel6.getComponents();
                al.clear();
                for (int i = 1; i <= n; i++) {
                    String data = rows.nextToken();
                    StringTokenizer st = new StringTokenizer(data, "####");
                    String loc = st.nextToken();
                    String vehicleno = st.nextToken();
                    String dateofentry = st.nextToken();
                    String timeofentry = st.nextToken();
                    String employeeid = st.nextToken();
                    String parkinginfo = "<html><p width=\"200\">Vehicle No : " + vehicleno + "<br>"
                            + "Date of Entry : " + dateofentry + "<br>"
                            + "Time of Entry : " + timeofentry + "<br>"
                            + "Employee Id : " + employeeid + "</p></html>";

                   ParkingSlot obj = new parkingbayB.ParkingSlot(vehicleno, loc, dateofentry, timeofentry, employeeid);
                    al.add(obj);
                    for (int j = 0; j <= components.length - 1; j++) {
                        JButton btn = (JButton) (components[j]);
                        if (btn.getText().equals(loc)) {
                            btn.setBackground(Color.RED);
                            btn.setOpaque(true);
                            System.out.println("hello");
                            btn.removeActionListener(this);
                            btn.setToolTipText(parkinginfo);
                        }
                    }
                    for (int j = 0; j <= components2.length - 1; j++) {
                        JButton btn = (JButton) (components2[j]);
                        if (btn.getText().equals(loc)) {
                            btn.setBackground(Color.RED);
                            btn.setOpaque(true);
                            System.out.println("hello");
                            btn.removeActionListener(this);
                            btn.setToolTipText(parkinginfo);
                        }

                    }
                }
                repaint();
                jpanel4.repaint();
                jpanel6.repaint();
            }
        } catch (UnirestException ex) {
            Logger.getLogger(parkingbayB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        String result = JOptionPane.showInputDialog(rootPane, "Enter Vehilce No.");
        try {
            if (result.equals("") == false) {
                String vehicleno = result.toUpperCase();
                String parkinglocation = btn.getText();
                String vehicletype = "Four Wheeler";
                String parkingbay = "Parking Bay B";
                Date currentdate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String entrydate = sdf.format(new Date());
                sdf = new SimpleDateFormat("HH:mm");
                String entrytime = sdf.format(new Date());
                String empid = eid;

                if (vehicleno.equals("") || parkinglocation.equals("")) {
                    JOptionPane.showMessageDialog(this, "empty vehicle no and location are empty");
                } else {

                    HttpResponse<String> res = Unirest.get("http://localhost:8888/Parkingentry")
                            .queryString("empid", empid)
                            .queryString("vehicleno", vehicleno)
                            .queryString("location", parkinglocation)
                            .queryString("vehicletype", vehicletype)
                            .queryString("parkingbay", parkingbay)
                            .queryString("dateofentry", entrydate)
                            .queryString("timeofentry", entrytime)
                            .asString();
                    String response = res.getBody();
                    if (response.equals("failed")==false) {
                        String ticketid = response;
                        JOptionPane.showMessageDialog(rootPane, "Vehicle Entry Success");
                        load();
                        printslip(vehicleno, vehicletype, parkinglocation, parkingbay, entrydate, entrytime, empid,ticketid);

                     
                
                }  else {
                        JOptionPane.showMessageDialog(rootPane, "Vehicle Entery Failed!!!");
                    }
            }
                
                  } else {
                JOptionPane.showMessageDialog(rootPane, "Enter vehicle no.");
            }
            
         
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
     void printslip(String vehicleno, String vehicletype, String location, String bay, String entrydate, String entrytime, String empid,String ticketid) {
        try {

            PrintService printService = PrinterOutputStream.getPrintServiceByName("THERMAL Receipt Printer");
            PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
            EscPos escpos = new EscPos(printerOutputStream);
            Style header = new Style()
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setJustification(EscPosConst.Justification.Center)
                    .setBold(true)
                    .setColorMode(Style.ColorMode.WhiteOnBlack);

            Style address = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Center);
            escpos.writeLF(header, " Alpha One Parking ");
            escpos.writeLF(address, "SCO-108, Ranjeet Avenue");
            escpos.writeLF(address, "[REMOVED_PHONE]");
            escpos.writeLF(address, "Date Time : " + entrydate + " " + entrytime);
            escpos.writeLF(address, " ");
            Style lineStyle = new Style()
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Center);

            escpos.writeLF(lineStyle, "----------------------------------------");

            Style rightText = new Style()
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Right);

            Style leftText = new Style()
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Left_Default);

            escpos.write(leftText, "Parking Bay     : ");
            escpos.writeLF(rightText, bay);
            escpos.write(leftText, "Vehicle No      : ");
            escpos.writeLF(rightText, vehicleno);
            escpos.write(leftText, "Vehicle Type    : ");
            escpos.writeLF(rightText, vehicletype);
            escpos.write(leftText, "Location        : ");
            escpos.writeLF(rightText, location);
            escpos.write(leftText, "Employee Id     : ");
            escpos.writeLF(rightText, empid);
            escpos.write(leftText, "Ticket No.      : ");
                    
                    
            escpos.writeLF(rightText, ticketid);
            escpos.writeLF(address, " ");
            BarCode barcode = new BarCode();
            barcode.setJustification(EscPosConst.Justification.Center);
            barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode);
            escpos.write(barcode, ticketid);

            escpos.feed(5);
            escpos.cut(EscPos.CutMode.PART);
            escpos.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jpanel4 = new javax.swing.JPanel();
        B1 = new javax.swing.JButton();
        B2 = new javax.swing.JButton();
        B3 = new javax.swing.JButton();
        B4 = new javax.swing.JButton();
        B5 = new javax.swing.JButton();
        B6 = new javax.swing.JButton();
        B7 = new javax.swing.JButton();
        B8 = new javax.swing.JButton();
        B9 = new javax.swing.JButton();
        B10 = new javax.swing.JButton();
        B11 = new javax.swing.JButton();
        B12 = new javax.swing.JButton();
        B13 = new javax.swing.JButton();
        B14 = new javax.swing.JButton();
        B15 = new javax.swing.JButton();
        B16 = new javax.swing.JButton();
        B17 = new javax.swing.JButton();
        B18 = new javax.swing.JButton();
        B19 = new javax.swing.JButton();
        B20 = new javax.swing.JButton();
        jpanel6 = new javax.swing.JPanel();
        B35 = new javax.swing.JButton();
        B34 = new javax.swing.JButton();
        B36 = new javax.swing.JButton();
        B33 = new javax.swing.JButton();
        B29 = new javax.swing.JButton();
        B30 = new javax.swing.JButton();
        B31 = new javax.swing.JButton();
        B32 = new javax.swing.JButton();
        B25 = new javax.swing.JButton();
        B26 = new javax.swing.JButton();
        B27 = new javax.swing.JButton();
        B28 = new javax.swing.JButton();
        B21 = new javax.swing.JButton();
        B22 = new javax.swing.JButton();
        B23 = new javax.swing.JButton();
        B24 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lbempid = new javax.swing.JLabel();
        btexitvehicle = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 270, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jpanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jpanel4.setLayout(null);

        B1.setText("B1");
        B1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B1ActionPerformed(evt);
            }
        });
        jpanel4.add(B1);
        B1.setBounds(30, 10, 80, 70);

        B2.setText("B2");
        jpanel4.add(B2);
        B2.setBounds(130, 10, 80, 70);

        B3.setText("B3");
        jpanel4.add(B3);
        B3.setBounds(230, 10, 80, 70);

        B4.setText("B4");
        jpanel4.add(B4);
        B4.setBounds(330, 10, 80, 70);

        B5.setText("B5");
        B5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B5ActionPerformed(evt);
            }
        });
        jpanel4.add(B5);
        B5.setBounds(30, 100, 80, 70);

        B6.setText("B6");
        jpanel4.add(B6);
        B6.setBounds(130, 100, 80, 70);

        B7.setText("B7");
        jpanel4.add(B7);
        B7.setBounds(230, 100, 80, 70);

        B8.setText("B8");
        jpanel4.add(B8);
        B8.setBounds(330, 100, 80, 70);

        B9.setText("B9");
        B9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B9ActionPerformed(evt);
            }
        });
        jpanel4.add(B9);
        B9.setBounds(30, 200, 80, 70);

        B10.setText("B10");
        jpanel4.add(B10);
        B10.setBounds(130, 200, 80, 70);

        B11.setText("B11");
        jpanel4.add(B11);
        B11.setBounds(230, 200, 80, 70);

        B12.setText("B12");
        jpanel4.add(B12);
        B12.setBounds(330, 200, 80, 70);

        B13.setText("B13");
        B13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B13ActionPerformed(evt);
            }
        });
        jpanel4.add(B13);
        B13.setBounds(30, 300, 80, 70);

        B14.setText("B14");
        jpanel4.add(B14);
        B14.setBounds(130, 300, 80, 70);

        B15.setText("B15");
        jpanel4.add(B15);
        B15.setBounds(230, 300, 80, 70);

        B16.setText("B16");
        jpanel4.add(B16);
        B16.setBounds(330, 300, 80, 70);

        B17.setText("B17");
        B17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B17ActionPerformed(evt);
            }
        });
        jpanel4.add(B17);
        B17.setBounds(30, 390, 80, 70);

        B18.setText("B18");
        jpanel4.add(B18);
        B18.setBounds(130, 390, 80, 70);

        B19.setText("B19");
        B19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B19ActionPerformed(evt);
            }
        });
        jpanel4.add(B19);
        B19.setBounds(230, 390, 80, 70);

        B20.setText("B20");
        jpanel4.add(B20);
        B20.setBounds(330, 390, 80, 70);

        getContentPane().add(jpanel4);
        jpanel4.setBounds(80, 60, 440, 480);

        jpanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jpanel6.setLayout(null);

        B35.setText("B35");
        jpanel6.add(B35);
        B35.setBounds(260, 370, 90, 100);

        B34.setText("B34");
        jpanel6.add(B34);
        B34.setBounds(140, 370, 90, 100);

        B36.setText("B36");
        jpanel6.add(B36);
        B36.setBounds(370, 370, 90, 100);

        B33.setText("B33");
        B33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B33ActionPerformed(evt);
            }
        });
        jpanel6.add(B33);
        B33.setBounds(30, 370, 90, 100);

        B29.setText("B29");
        B29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B29ActionPerformed(evt);
            }
        });
        jpanel6.add(B29);
        B29.setBounds(30, 250, 90, 100);

        B30.setText("B30");
        jpanel6.add(B30);
        B30.setBounds(140, 250, 90, 100);

        B31.setText("B31");
        jpanel6.add(B31);
        B31.setBounds(260, 250, 90, 100);

        B32.setText("B32");
        jpanel6.add(B32);
        B32.setBounds(370, 250, 90, 100);

        B25.setText("B25");
        B25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B25ActionPerformed(evt);
            }
        });
        jpanel6.add(B25);
        B25.setBounds(30, 130, 90, 100);

        B26.setText("B26");
        jpanel6.add(B26);
        B26.setBounds(140, 130, 90, 100);

        B27.setText("B27");
        jpanel6.add(B27);
        B27.setBounds(260, 130, 90, 100);

        B28.setText("B28");
        jpanel6.add(B28);
        B28.setBounds(370, 130, 90, 100);

        B21.setText("B21");
        B21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B21ActionPerformed(evt);
            }
        });
        jpanel6.add(B21);
        B21.setBounds(30, 10, 90, 100);

        B22.setText("B22");
        jpanel6.add(B22);
        B22.setBounds(140, 10, 90, 100);

        B23.setText("B23");
        jpanel6.add(B23);
        B23.setBounds(260, 10, 90, 100);

        B24.setText("B24");
        jpanel6.add(B24);
        B24.setBounds(370, 10, 90, 100);

        getContentPane().add(jpanel6);
        jpanel6.setBounds(630, 60, 480, 480);

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel1.setText("                           PARKING BAY B ( FOUR WHEELER)");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(310, 0, 660, 50);

        lbempid.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbempid.setText("                  empid");
        getContentPane().add(lbempid);
        lbempid.setBounds(40, 30, 160, 30);

        btexitvehicle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btexitvehicle.setText("Exit Vehicle");
        btexitvehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btexitvehicleActionPerformed(evt);
            }
        });
        getContentPane().add(btexitvehicle);
        btexitvehicle.setBounds(980, 10, 180, 50);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void B1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B1ActionPerformed

    private void B5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B5ActionPerformed

    private void B9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B9ActionPerformed

    private void B13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B13ActionPerformed

    private void B17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B17ActionPerformed

    private void B19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B19ActionPerformed

    private void B33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B33ActionPerformed

    private void B29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B29ActionPerformed

    private void B25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B25ActionPerformed

    private void B21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B21ActionPerformed

    private void btexitvehicleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btexitvehicleActionPerformed
        exitvehicle obj = new exitvehicle(this);
        this.setVisible(false);
    }//GEN-LAST:event_btexitvehicleActionPerformed

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
            java.util.logging.Logger.getLogger(parkingbayB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(parkingbayB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(parkingbayB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(parkingbayB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new parkingbayB().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton B1;
    private javax.swing.JButton B10;
    private javax.swing.JButton B11;
    private javax.swing.JButton B12;
    private javax.swing.JButton B13;
    private javax.swing.JButton B14;
    private javax.swing.JButton B15;
    private javax.swing.JButton B16;
    private javax.swing.JButton B17;
    private javax.swing.JButton B18;
    private javax.swing.JButton B19;
    private javax.swing.JButton B2;
    private javax.swing.JButton B20;
    private javax.swing.JButton B21;
    private javax.swing.JButton B22;
    private javax.swing.JButton B23;
    private javax.swing.JButton B24;
    private javax.swing.JButton B25;
    private javax.swing.JButton B26;
    private javax.swing.JButton B27;
    private javax.swing.JButton B28;
    private javax.swing.JButton B29;
    private javax.swing.JButton B3;
    private javax.swing.JButton B30;
    private javax.swing.JButton B31;
    private javax.swing.JButton B32;
    private javax.swing.JButton B33;
    private javax.swing.JButton B34;
    private javax.swing.JButton B35;
    private javax.swing.JButton B36;
    private javax.swing.JButton B4;
    private javax.swing.JButton B5;
    private javax.swing.JButton B6;
    private javax.swing.JButton B7;
    private javax.swing.JButton B8;
    private javax.swing.JButton B9;
    private javax.swing.JButton btexitvehicle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jpanel4;
    private javax.swing.JPanel jpanel6;
    private javax.swing.JLabel lbempid;
    // End of variables declaration//GEN-END:variables

 class ParkingSlot {

        String vehicleno, location, dateofentry, timeofentry, employeeid;

        public ParkingSlot(String vehicleno, String location, String dateofentry, String timeofentry, String employeeid) {
            this.vehicleno = vehicleno;
            this.location = location;
            this.dateofentry = dateofentry;
            this.timeofentry = timeofentry;
            this.employeeid = employeeid;
        }

    }
}