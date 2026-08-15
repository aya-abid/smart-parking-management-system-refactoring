
import java.awt.Dimension;
import java.awt.Toolkit;


public class jparking extends javax.swing.JFrame {

    webparking obj;
    public jparking() {
        initComponents();
        
       
        setSize(500,400);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((d.width) - this.getWidth())/2;
        int y = (int)((d.height) - this.getWidth())/2;
        setLocation(x,y);
                
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bt1 = new javax.swing.JButton();
        bt2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        bt1.setText("start server");
        bt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt1ActionPerformed(evt);
            }
        });
        getContentPane().add(bt1);
        bt1.setBounds(148, 84, 110, 23);

        bt2.setText("stop server");
        bt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt2ActionPerformed(evt);
            }
        });
        getContentPane().add(bt2);
        bt2.setBounds(150, 190, 110, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt1ActionPerformed
        try {
           obj = new webparking(8888);
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        bt1.setEnabled(false);
        bt2.setEnabled(true);
        System.out.println("server start");
    }//GEN-LAST:event_bt1ActionPerformed

    private void bt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt2ActionPerformed
     obj.shutdown();
        
        bt1.setEnabled(true);
        bt2.setEnabled(false);
        
        System.out.println("server shutdown");
    }//GEN-LAST:event_bt2ActionPerformed

   
    public static void main(String args[]) {
      
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jparking().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt1;
    private javax.swing.JButton bt2;
    // End of variables declaration//GEN-END:variables
}
