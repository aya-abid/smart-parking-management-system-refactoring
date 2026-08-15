
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

public class parkingbayA extends javax.swing.JFrame implements ActionListener {

    ArrayList<ParkingSlot> al = new ArrayList<>();

    public parkingbayA() {

        initComponents();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
    }
    String eid;

    public parkingbayA(String empid) {
        initComponents();
        eid = empid;

        setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        lbempid.setText("Welcome, " + empid);
        load();
    }

    void load() {
        Component[] components = jPanel2.getComponents();
        Component[] components2 = jPanel3.getComponents();
        for (Component cp : components) {
            JButton btn = (JButton) cp;
            btn.setBackground(Color.GREEN);
            btn.setOpaque(true);
            btn.addActionListener(this);
        }
        for (Component cp : components2) {
            JButton btn = (JButton) cp;
            btn.setBackground(Color.GREEN);
            btn.setOpaque(true);
            btn.addActionListener(this);
        }
        checkbooked();
    }

    public void checkbooked() {
        try {
            HttpResponse<String> res = Unirest.get("http://localhost:8888/getbookedslots")
                    .queryString("parkingbay", "Parking Bay A").asString();
            String response = res.getBody();
            if (response.isEmpty() == false) {
                StringTokenizer rows = new StringTokenizer(response, "~~~~");
                int n = rows.countTokens();
                Component[] components = jPanel2.getComponents();
                Component[] components2 = jPanel3.getComponents();
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

                    ParkingSlot obj = new ParkingSlot(vehicleno, loc, dateofentry, timeofentry, employeeid);
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
                jPanel2.repaint();
                jPanel3.repaint();
            }
        } catch (UnirestException ex) {
            Logger.getLogger(parkingbayA.class.getName()).log(Level.SEVERE, null, ex);
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
                String vehicletype = "Two Wheeler";
                String parkingbay = "Parking Bay A";
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

                    } else {
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

        jPanel2 = new javax.swing.JPanel();
        A46 = new javax.swing.JButton();
        A49 = new javax.swing.JButton();
        A41 = new javax.swing.JButton();
        A40 = new javax.swing.JButton();
        A33 = new javax.swing.JButton();
        A42 = new javax.swing.JButton();
        A35 = new javax.swing.JButton();
        A34 = new javax.swing.JButton();
        A27 = new javax.swing.JButton();
        A32 = new javax.swing.JButton();
        A29 = new javax.swing.JButton();
        A43 = new javax.swing.JButton();
        A50 = new javax.swing.JButton();
        A31 = new javax.swing.JButton();
        A45 = new javax.swing.JButton();
        A44 = new javax.swing.JButton();
        A37 = new javax.swing.JButton();
        A30 = new javax.swing.JButton();
        A36 = new javax.swing.JButton();
        A38 = new javax.swing.JButton();
        A48 = new javax.swing.JButton();
        A47 = new javax.swing.JButton();
        A26 = new javax.swing.JButton();
        A28 = new javax.swing.JButton();
        A39 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        A3 = new javax.swing.JButton();
        A4 = new javax.swing.JButton();
        A6 = new javax.swing.JButton();
        A7 = new javax.swing.JButton();
        A8 = new javax.swing.JButton();
        A1 = new javax.swing.JButton();
        A9 = new javax.swing.JButton();
        A2 = new javax.swing.JButton();
        A5 = new javax.swing.JButton();
        A10 = new javax.swing.JButton();
        A21 = new javax.swing.JButton();
        A22 = new javax.swing.JButton();
        A23 = new javax.swing.JButton();
        A24 = new javax.swing.JButton();
        A25 = new javax.swing.JButton();
        A18 = new javax.swing.JButton();
        A16 = new javax.swing.JButton();
        A17 = new javax.swing.JButton();
        A19 = new javax.swing.JButton();
        A20 = new javax.swing.JButton();
        A13 = new javax.swing.JButton();
        A11 = new javax.swing.JButton();
        A12 = new javax.swing.JButton();
        A14 = new javax.swing.JButton();
        A15 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lbempid = new javax.swing.JLabel();
        btexitvehicle = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setForeground(new java.awt.Color(51, 51, 255));
        jPanel2.setLayout(null);

        A46.setText("A46");
        A46.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A46);
        A46.setBounds(30, 340, 60, 60);

        A49.setText("A49");
        A49.setPreferredSize(new java.awt.Dimension(50, 50));
        A49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A49ActionPerformed(evt);
            }
        });
        jPanel2.add(A49);
        A49.setBounds(270, 340, 60, 60);

        A41.setText("A41");
        A41.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A41);
        A41.setBounds(30, 260, 60, 60);

        A40.setText("A40");
        jPanel2.add(A40);
        A40.setBounds(350, 170, 60, 60);

        A33.setText("A33");
        A33.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A33);
        A33.setBounds(190, 90, 60, 60);

        A42.setText("A42");
        A42.setPreferredSize(new java.awt.Dimension(50, 50));
        A42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A42ActionPerformed(evt);
            }
        });
        jPanel2.add(A42);
        A42.setBounds(110, 260, 60, 60);

        A35.setText("A35");
        jPanel2.add(A35);
        A35.setBounds(350, 90, 60, 60);

        A34.setText("A34");
        A34.setPreferredSize(new java.awt.Dimension(50, 50));
        A34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A34ActionPerformed(evt);
            }
        });
        jPanel2.add(A34);
        A34.setBounds(270, 90, 60, 60);

        A27.setText("A27");
        A27.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A27);
        A27.setBounds(110, 10, 60, 60);

        A32.setText("A32");
        A32.setPreferredSize(new java.awt.Dimension(50, 50));
        A32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A32ActionPerformed(evt);
            }
        });
        jPanel2.add(A32);
        A32.setBounds(110, 90, 60, 60);

        A29.setText("A29");
        A29.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A29);
        A29.setBounds(270, 10, 60, 60);

        A43.setText("A43");
        A43.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A43);
        A43.setBounds(190, 260, 60, 60);

        A50.setText("A50");
        jPanel2.add(A50);
        A50.setBounds(350, 340, 60, 60);

        A31.setText("A31");
        A31.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A31);
        A31.setBounds(30, 90, 60, 60);

        A45.setText("A45");
        jPanel2.add(A45);
        A45.setBounds(350, 260, 60, 60);

        A44.setText("A44");
        A44.setPreferredSize(new java.awt.Dimension(50, 50));
        A44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A44ActionPerformed(evt);
            }
        });
        jPanel2.add(A44);
        A44.setBounds(270, 260, 60, 60);

        A37.setText("A37");
        A37.setPreferredSize(new java.awt.Dimension(50, 50));
        A37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A37ActionPerformed(evt);
            }
        });
        jPanel2.add(A37);
        A37.setBounds(110, 170, 60, 60);

        A30.setText("A30");
        A30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A30ActionPerformed(evt);
            }
        });
        jPanel2.add(A30);
        A30.setBounds(350, 10, 60, 60);

        A36.setText("A36");
        A36.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A36);
        A36.setBounds(30, 170, 60, 60);

        A38.setText("A38");
        A38.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A38);
        A38.setBounds(190, 170, 60, 60);

        A48.setText("A48");
        A48.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A48);
        A48.setBounds(190, 340, 60, 60);

        A47.setText("A47");
        A47.setPreferredSize(new java.awt.Dimension(50, 50));
        A47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A47ActionPerformed(evt);
            }
        });
        jPanel2.add(A47);
        A47.setBounds(110, 340, 60, 60);

        A26.setText("A26");
        A26.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(A26);
        A26.setBounds(30, 10, 60, 60);

        A28.setText("A28");
        A28.setPreferredSize(new java.awt.Dimension(50, 50));
        A28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A28ActionPerformed(evt);
            }
        });
        jPanel2.add(A28);
        A28.setBounds(190, 10, 60, 60);

        A39.setText("A39");
        A39.setPreferredSize(new java.awt.Dimension(50, 50));
        A39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A39ActionPerformed(evt);
            }
        });
        jPanel2.add(A39);
        A39.setBounds(270, 170, 60, 60);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(650, 110, 460, 420);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(null);

        A3.setText("A3");
        A3.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A3);
        A3.setBounds(190, 10, 60, 60);

        A4.setText("A4");
        A4.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A4);
        A4.setBounds(270, 10, 60, 60);

        A6.setText("A6");
        A6.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A6);
        A6.setBounds(30, 90, 60, 60);

        A7.setText("A7");
        A7.setPreferredSize(new java.awt.Dimension(50, 50));
        A7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A7ActionPerformed(evt);
            }
        });
        jPanel3.add(A7);
        A7.setBounds(110, 90, 60, 60);

        A8.setText("A8");
        A8.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A8);
        A8.setBounds(190, 90, 60, 60);

        A1.setText("A1");
        A1.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A1);
        A1.setBounds(30, 10, 60, 60);

        A9.setText("A9");
        A9.setPreferredSize(new java.awt.Dimension(50, 50));
        A9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A9ActionPerformed(evt);
            }
        });
        jPanel3.add(A9);
        A9.setBounds(270, 90, 60, 60);

        A2.setText("A2");
        A2.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A2);
        A2.setBounds(110, 10, 60, 60);

        A5.setText("A5");
        A5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A5ActionPerformed(evt);
            }
        });
        jPanel3.add(A5);
        A5.setBounds(350, 10, 60, 60);

        A10.setText("A10");
        jPanel3.add(A10);
        A10.setBounds(350, 90, 60, 60);

        A21.setText("A21");
        A21.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A21);
        A21.setBounds(30, 340, 60, 60);

        A22.setText("A22");
        A22.setPreferredSize(new java.awt.Dimension(50, 50));
        A22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A22ActionPerformed(evt);
            }
        });
        jPanel3.add(A22);
        A22.setBounds(110, 340, 60, 60);

        A23.setText("A23");
        A23.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A23);
        A23.setBounds(190, 340, 60, 60);

        A24.setText("A24");
        A24.setPreferredSize(new java.awt.Dimension(50, 50));
        A24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A24ActionPerformed(evt);
            }
        });
        jPanel3.add(A24);
        A24.setBounds(270, 340, 60, 60);

        A25.setText("A25");
        jPanel3.add(A25);
        A25.setBounds(350, 340, 60, 60);

        A18.setText("A18");
        A18.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A18);
        A18.setBounds(190, 260, 60, 60);

        A16.setText("A16");
        A16.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A16);
        A16.setBounds(30, 260, 60, 60);

        A17.setText("A17");
        A17.setPreferredSize(new java.awt.Dimension(50, 50));
        A17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A17ActionPerformed(evt);
            }
        });
        jPanel3.add(A17);
        A17.setBounds(110, 260, 60, 60);

        A19.setText("A19");
        A19.setPreferredSize(new java.awt.Dimension(50, 50));
        A19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A19ActionPerformed(evt);
            }
        });
        jPanel3.add(A19);
        A19.setBounds(270, 260, 60, 60);

        A20.setText("A20");
        jPanel3.add(A20);
        A20.setBounds(350, 260, 60, 60);

        A13.setText("A13");
        A13.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A13);
        A13.setBounds(190, 170, 60, 60);

        A11.setText("A11");
        A11.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.add(A11);
        A11.setBounds(30, 170, 60, 60);

        A12.setText("A12");
        A12.setPreferredSize(new java.awt.Dimension(50, 50));
        A12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A12ActionPerformed(evt);
            }
        });
        jPanel3.add(A12);
        A12.setBounds(110, 170, 60, 60);

        A14.setText("A14");
        A14.setPreferredSize(new java.awt.Dimension(50, 50));
        A14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A14ActionPerformed(evt);
            }
        });
        jPanel3.add(A14);
        A14.setBounds(270, 170, 60, 60);

        A15.setText("A15");
        jPanel3.add(A15);
        A15.setBounds(350, 170, 60, 60);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(90, 110, 440, 420);

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel1.setText("                             PARKING BAY A  (TWO WHEELER)");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(280, 20, 690, 50);

        lbempid.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbempid.setText("EMPID");
        getContentPane().add(lbempid);
        lbempid.setBounds(100, 60, 140, 30);

        btexitvehicle.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btexitvehicle.setText("EXIT VEHICLE");
        btexitvehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btexitvehicleActionPerformed(evt);
            }
        });
        getContentPane().add(btexitvehicle);
        btexitvehicle.setBounds(1000, 20, 140, 70);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void A7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A7ActionPerformed

    private void A5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A5ActionPerformed

    private void A9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A9ActionPerformed

    private void A22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A22ActionPerformed

    private void A24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A24ActionPerformed

    private void A17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A17ActionPerformed

    private void A19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A19ActionPerformed

    private void A12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A12ActionPerformed

    private void A14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A14ActionPerformed

    private void A42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A42ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A42ActionPerformed

    private void A44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A44ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A44ActionPerformed

    private void A49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A49ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A49ActionPerformed

    private void A47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A47ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A47ActionPerformed

    private void A37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A37ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A37ActionPerformed

    private void A39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A39ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A39ActionPerformed

    private void A34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A34ActionPerformed

    private void A32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A32ActionPerformed

    private void A30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A30ActionPerformed

    private void A28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A28ActionPerformed

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
            java.util.logging.Logger.getLogger(parkingbayA.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(parkingbayA.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(parkingbayA.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(parkingbayA.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new parkingbayA().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A1;
    private javax.swing.JButton A10;
    private javax.swing.JButton A11;
    private javax.swing.JButton A12;
    private javax.swing.JButton A13;
    private javax.swing.JButton A14;
    private javax.swing.JButton A15;
    private javax.swing.JButton A16;
    private javax.swing.JButton A17;
    private javax.swing.JButton A18;
    private javax.swing.JButton A19;
    private javax.swing.JButton A2;
    private javax.swing.JButton A20;
    private javax.swing.JButton A21;
    private javax.swing.JButton A22;
    private javax.swing.JButton A23;
    private javax.swing.JButton A24;
    private javax.swing.JButton A25;
    private javax.swing.JButton A26;
    private javax.swing.JButton A27;
    private javax.swing.JButton A28;
    private javax.swing.JButton A29;
    private javax.swing.JButton A3;
    private javax.swing.JButton A30;
    private javax.swing.JButton A31;
    private javax.swing.JButton A32;
    private javax.swing.JButton A33;
    private javax.swing.JButton A34;
    private javax.swing.JButton A35;
    private javax.swing.JButton A36;
    private javax.swing.JButton A37;
    private javax.swing.JButton A38;
    private javax.swing.JButton A39;
    private javax.swing.JButton A4;
    private javax.swing.JButton A40;
    private javax.swing.JButton A41;
    private javax.swing.JButton A42;
    private javax.swing.JButton A43;
    private javax.swing.JButton A44;
    private javax.swing.JButton A45;
    private javax.swing.JButton A46;
    private javax.swing.JButton A47;
    private javax.swing.JButton A48;
    private javax.swing.JButton A49;
    private javax.swing.JButton A5;
    private javax.swing.JButton A50;
    private javax.swing.JButton A6;
    private javax.swing.JButton A7;
    private javax.swing.JButton A8;
    private javax.swing.JButton A9;
    private javax.swing.JButton btexitvehicle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
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
