
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class adminchangepassword extends javax.swing.JFrame {

    /**
     * Creates new form adminlogin2
     */
    public adminchangepassword() {
        initComponents();
        setSize(700, 600);
         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((d.width) - this.getWidth())/2;
        int y = (int)((d.height) - this.getWidth())/2;
        setLocation(x,y);
    }
    String uname;

    public adminchangepassword(String username) {
        initComponents();
        uname = username;
        
        setVisible(true);
        tfusername.setText(uname);
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfusername = new javax.swing.JTextField();
        pfoldpass = new javax.swing.JPasswordField();
        pfnewpass = new javax.swing.JPasswordField();
        pfconfpass = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        lb4 = new javax.swing.JLabel();
        lb5 = new javax.swing.JLabel();
        lb6 = new javax.swing.JLabel();
        lb7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel1.setText("        Admin Change Password");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(51, 25, 280, 22);

        tfusername.setEnabled(false);
        tfusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfusernameActionPerformed(evt);
            }
        });
        getContentPane().add(tfusername);
        tfusername.setBounds(139, 100, 120, 20);
        getContentPane().add(pfoldpass);
        pfoldpass.setBounds(140, 140, 120, 20);
        getContentPane().add(pfnewpass);
        pfnewpass.setBounds(140, 180, 120, 20);
        getContentPane().add(pfconfpass);
        pfconfpass.setBounds(140, 220, 120, 20);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("change password");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(150, 260, 140, 30);

        lb4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb4.setText("      username");
        getContentPane().add(lb4);
        lb4.setBounds(14, 94, 120, 20);

        lb5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb5.setText("   old password");
        getContentPane().add(lb5);
        lb5.setBounds(20, 140, 100, 15);

        lb6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb6.setText("new password");
        getContentPane().add(lb6);
        lb6.setBounds(30, 180, 90, 20);

        lb7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb7.setText("confirm password");
        getContentPane().add(lb7);
        lb7.setBounds(14, 214, 110, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfusernameActionPerformed

    }//GEN-LAST:event_tfusernameActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String oldpass = pfoldpass.getText();
        String newpass = pfnewpass.getText();
        String confpass = pfconfpass.getText();
        if (oldpass.isEmpty() || confpass.isEmpty() || newpass.isEmpty()) {

            JOptionPane.showMessageDialog(this, "all fields are mandatory");

    }//GEN-LAST:event_jButton1ActionPerformed
       else if (newpass.equals(confpass) == false) {
            JOptionPane.showMessageDialog(this, "new password and confirm password not matched");

        } else {
            try {
                HttpResponse<String> res = Unirest.get("http://localhost:8888/adminchangepassword")
                        .queryString("username", uname)
                        .queryString("oldpass", oldpass)
                        .queryString("newpass", newpass).asString();
                String response = res.getBody();
                if (response.equals("success")) {
                    JOptionPane.showMessageDialog(this, "password changed successfully");

                } else {
                    JOptionPane.showMessageDialog(this, "invalid old password");
                }
            } catch (UnirestException ex) {
                Logger.getLogger(adminchangepassword.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
    }

   
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
            java.util.logging.Logger.getLogger(adminchangepassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(adminchangepassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(adminchangepassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(adminchangepassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new adminchangepassword().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lb4;
    private javax.swing.JLabel lb5;
    private javax.swing.JLabel lb6;
    private javax.swing.JLabel lb7;
    private javax.swing.JPasswordField pfconfpass;
    private javax.swing.JPasswordField pfnewpass;
    private javax.swing.JPasswordField pfoldpass;
    private javax.swing.JTextField tfusername;
    // End of variables declaration//GEN-END:variables
}
