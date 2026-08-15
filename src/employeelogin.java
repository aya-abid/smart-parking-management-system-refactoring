
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JOptionPane;


public class employeelogin extends javax.swing.JFrame {

   
    public employeelogin() {
        initComponents();
         setSize(780, 600);
        setVisible(true);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((d.width) - this.getWidth())/2;
        int y = (int)((d.height) - this.getWidth())/2;
        setLocation(x,y);
        
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbemoployeelogin = new javax.swing.JLabel();
        tfemployee = new javax.swing.JTextField();
        pfpassword = new javax.swing.JPasswordField();
        lbemployee = new javax.swing.JLabel();
        lbpassword = new javax.swing.JLabel();
        btlogin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        lbemoployeelogin.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lbemoployeelogin.setText("               EMPLOYEE LOGIN");
        getContentPane().add(lbemoployeelogin);
        lbemoployeelogin.setBounds(280, 20, 370, 50);
        getContentPane().add(tfemployee);
        tfemployee.setBounds(370, 150, 160, 30);
        getContentPane().add(pfpassword);
        pfpassword.setBounds(370, 240, 160, 30);

        lbemployee.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbemployee.setText("employee id");
        getContentPane().add(lbemployee);
        lbemployee.setBounds(250, 150, 110, 40);

        lbpassword.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbpassword.setText("password");
        getContentPane().add(lbpassword);
        lbpassword.setBounds(260, 240, 90, 30);

        btlogin.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btlogin.setText("LOGIN");
        btlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btloginActionPerformed(evt);
            }
        });
        getContentPane().add(btlogin);
        btlogin.setBounds(370, 300, 100, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btloginActionPerformed
        try {

            String e, p;
            e = tfemployee.getText();
            p = pfpassword.getText();

            if (e.equals("") || p.equals("")) {
                JOptionPane.showMessageDialog(this, "empty username or password");
            } else {
                
                HttpResponse<String> res = Unirest.get("http://localhost:8888//employeelogin")
                        .queryString("empid", e)
                        .queryString("password",p)
                        .asString();
                String response = res.getBody();
                if(response.equals("success"))
                {
                    JOptionPane.showMessageDialog(rootPane, "Login Success");
                    employeehome obj = new employeehome(e);
                   
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
            java.util.logging.Logger.getLogger(employeelogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(employeelogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(employeelogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(employeelogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new employeelogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btlogin;
    private javax.swing.JLabel lbemoployeelogin;
    private javax.swing.JLabel lbemployee;
    private javax.swing.JLabel lbpassword;
    private javax.swing.JPasswordField pfpassword;
    private javax.swing.JTextField tfemployee;
    // End of variables declaration//GEN-END:variables
}
