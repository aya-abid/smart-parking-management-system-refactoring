


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JOptionPane;


public class adminlogin extends javax.swing.JFrame {

    
  
   public adminlogin()
    {
        initComponents(); 
        setSize(700, 600);
        setVisible(true);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((d.width) - this.getWidth())/2;
        int y = (int)((d.height) - this.getWidth())/2;
        setLocation(x,y);
        
    }
     
     

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb1 = new javax.swing.JLabel();
        btlogin = new javax.swing.JButton();
        tfusername = new javax.swing.JTextField();
        tfpassword = new javax.swing.JPasswordField();
        lbpassword = new javax.swing.JLabel();
        lbusername = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        lb1.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lb1.setText("     Admin Login");
        getContentPane().add(lb1);
        lb1.setBounds(140, 30, 190, 50);

        btlogin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btlogin.setText("login");
        btlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btloginActionPerformed(evt);
            }
        });
        getContentPane().add(btlogin);
        btlogin.setBounds(180, 280, 120, 23);
        getContentPane().add(tfusername);
        tfusername.setBounds(170, 120, 140, 30);
        getContentPane().add(tfpassword);
        tfpassword.setBounds(170, 200, 140, 30);

        lbpassword.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbpassword.setText("Password");
        getContentPane().add(lbpassword);
        lbpassword.setBounds(40, 200, 110, 30);

        lbusername.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbusername.setText("Username");
        getContentPane().add(lbusername);
        lbusername.setBounds(40, 120, 120, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btloginActionPerformed
        try {

            String u, p;
            u = tfusername.getText();
            p = tfpassword.getText();

            if (u.equals("") || p.equals("")) {
                JOptionPane.showMessageDialog(this, "empty username or password");
            } else {
                
                HttpResponse<String> res = Unirest.get("http://localhost:8888//adminlogin")
                        .queryString("username", u)
                        .queryString("password", p)
                        .asString();
                String response = res.getBody();
                if(response.equals("success"))
                {
                    JOptionPane.showMessageDialog(rootPane, "Login Success");
                    adminhome obj = new adminhome(u);
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Login Failed");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }//GEN-LAST:event_btloginActionPerformed

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
            java.util.logging.Logger.getLogger(adminlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(adminlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(adminlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(adminlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new adminlogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btlogin;
    private javax.swing.JLabel lb1;
    private javax.swing.JLabel lbpassword;
    private javax.swing.JLabel lbusername;
    private javax.swing.JPasswordField tfpassword;
    private javax.swing.JTextField tfusername;
    // End of variables declaration//GEN-END:variables

  
}
