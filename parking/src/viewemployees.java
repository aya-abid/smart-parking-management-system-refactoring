
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class viewemployees extends javax.swing.JFrame {

    ArrayList<employee> al;
    mytablemodel tm;

    public viewemployees() {
        initComponents();
        
        al = new ArrayList<>();
        tm = new mytablemodel();
        jt.setModel(tm);
        setSize(780,600);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((d.width) - this.getWidth())/2;
        int y = (int)((d.height) - this.getWidth())/2;
        setLocation(x,y);
        
        setVisible(true);
        loaddata();
    }

    void loaddata() {
        try {

            HttpResponse<String> res = Unirest.get("http://localhost:8888/getallemployees").asString();
            String response = res.getBody();
            StringTokenizer st = new StringTokenizer(response, "~!@");
            int n = st.countTokens();
            for(int i=1;i<=n;i++)
            {
                String row = st.nextToken();
                StringTokenizer st2 =  new StringTokenizer(row,"#$%");
                String empid = st2.nextToken();
                String ename = st2.nextToken();
                String fname = st2.nextToken();
                String mobile = st2.nextToken();
                String address = st2.nextToken();
                String photo = st2.nextToken();
                employee obj = new employee(empid, ename, fname, mobile, address, photo);
                al.add(obj);
            }
            
        } catch (UnirestException ex) {
            Logger.getLogger(viewemployees.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    ///inner class///
    class mytablemodel extends AbstractTableModel {

        String col[] = {"empid", "name", "father name", "moblieno", "address", "photo"};

        public int getRowCount() {
            return al.size();
        }

        @Override
        public String getColumnName(int j) {
            return col[j];
        }

        @Override
        public int getColumnCount() {
               return col.length;
        }

        @Override
        public Object getValueAt(int i, int j) {
         employee et = al.get(i);
            switch (j) {
                case 0:
                    return et.empid;
                case 1:
                    return et.name;
                case 2:
                    return et.fname;
                case 3:
                    return et.mobileno;
                case 4:
                    return et.address;
                case 5:
                    return et.photo;
                default:
                    return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jt = new javax.swing.JTable();
        lbviewemployees = new javax.swing.JLabel();
        btdelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        jScrollPane1.setViewportView(jt);
        if (jt.getColumnModel().getColumnCount() > 0) {
            jt.getColumnModel().getColumn(4).setResizable(false);
        }

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(80, 60, 530, 320);

        lbviewemployees.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lbviewemployees.setText("                   VIEW EMPLOYEES");
        getContentPane().add(lbviewemployees);
        lbviewemployees.setBounds(170, 10, 320, 40);

        btdelete.setText("DELETE");
        btdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btdelete);
        btdelete.setBounds(270, 390, 160, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btdeleteActionPerformed

    class employee {

        String empid, name, fname, mobileno, address, photo;

        public employee(String empid, String name, String fname, String mobileno, String address, String photo) {
            this.empid = empid;
            this.name = name;
            this.fname = fname;
            this.mobileno = mobileno;
            this.address = address;
            this.photo = photo;
        }

    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new viewemployees().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btdelete;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jt;
    private javax.swing.JLabel lbviewemployees;
    // End of variables declaration//GEN-END:variables
}
