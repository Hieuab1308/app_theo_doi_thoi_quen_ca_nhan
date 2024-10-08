package UI;

import java.util.List;
import java.util.ArrayList;
import Class.thoi_quen;
import Class.user;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import manageSQL.thoiquenadd;
import java.util.Date;
import java.util.HashMap;
import javax.swing.table.DefaultTableCellRenderer;
import manageSQL.ListThoiQuen;
import manageSQL.thoiquendelete;
import manageSQL.thoiquenedit;




public class main extends javax.swing.JFrame {

    /**
     * Creates new form main
     */
    public main(user userLogin) throws IOException, ClassNotFoundException {
        initComponents();
        loadThoiQuenData();
        setLocationRelativeTo(null);
        booleanCheck = new boolean[100];
        openFile();
        loadThoiQuenData1();
        lastDate = currentDate;
        currentDate = LocalDate.now();
        if (countDate1 == null) {
            countDate1 = new HashMap<>();
        }
        if (countDate2 == null) {
            countDate2 = new HashMap<>();
        }
        if(!currentDate.equals(lastDate)){
            for(int i = 0;i < thoiquen.size();i++){
               if(booleanCheck[i]==true){
                   if(countDate1.containsKey(thoiquen.get(i).getId())){
                       countDate1.put(thoiquen.get(i).getId(),countDate1.get(thoiquen.get(i).getId())+1);
                   }else{
                       countDate1.put(thoiquen.get(i).getId(),1);
                   }   
               }else{
                   if(countDate2.containsKey(thoiquen.get(i).getId())){
                       countDate2.put(thoiquen.get(i).getId(),countDate2.get(thoiquen.get(i).getId())+1);
                   }else{
                       countDate2.put(thoiquen.get(i).getId(),1);
                   }
               }
           }
           Arrays.fill(booleanCheck,false);
        }
       
//        Arrays.fill(booleanCheck,false);
        loadThoiQuenData1();
        ViewTable3();
        jP1.setVisible(true);
        jP2.setVisible(false);
        jP3.setVisible(false);
        jP4.setVisible(false);
        
        // set nhãn và textfield khồng được dùng khi mới khởi tạo
        label_add.setVisible(false);
        label_fix.setVisible(false);

        mtq_text.setEditable(false);
        ttq_text.setEditable(false);
        mota_text.setEditable(false);
        nbd_date.setEnabled(false);
        nkt_date.setEnabled(false);

        btn_cancel.setVisible(false);
        btn_save.setVisible(false);
        btn_savefix.setVisible(false);

        
    }
    private void openFile(){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        
        try {
            fis = new FileInputStream("data.dat");
            ois = new ObjectInputStream(fis);
            booleanCheck = (boolean[]) ois.readObject();
            currentDate = (LocalDate) ois.readObject();
            countDate1 = (HashMap) ois.readObject();
            countDate2 = (HashMap) ois.readObject();
        }catch(FileNotFoundException e){
        } 
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(ois != null && fis != null){
                    ois.close();
                    fis.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private void writeFile(){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream("data.dat");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(booleanCheck);
            oos.writeObject(currentDate);
            oos.writeObject(countDate1);
            oos.writeObject(countDate2);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                oos.close();
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private void clock() {
        new Thread() {
            public void run() {
                while (true) {
                    Calendar ca = new GregorianCalendar();
                    int hour = ca.get(Calendar.HOUR);
                    int minu = ca.get(Calendar.MINUTE);
                    int sec = ca.get(Calendar.SECOND);
                    int AM_PM = ca.get(Calendar.AM_PM);

                    String day_night;
                    if (AM_PM == 1) {
                        day_night = "PM";
                    } else {
                        day_night = "AM";
                    }
                    jPclock.setText(hour + ":" + minu + ":" + sec + "" + day_night);
                }
            }
        }.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this coddđe. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    List<thoi_quen> thoiquen = ListThoiQuen.getAllThoiQuen();
    HashMap<String,Integer> countDate1 = new HashMap<>();
    HashMap<String,Integer> countDate2 = new HashMap<>();
    public static int stt = -1;
    public static LocalDate lastDate;
    public static LocalDate currentDate;

    public static boolean[] booleanCheck;
    
    //Cập nhật lại độ dài của mảng boolean
    

//Table thói quen

    public void Viewtable1() {
//        
        DefaultTableModel model = (DefaultTableModel) this.table_thoiquen.getModel();
        model.setNumRows(0); // Xóa tất cả các hàng trong bảng
        int n = 1;
        for (thoi_quen tq : thoiquen) {
            model.addRow(new Object[]{n++, tq.getId(), tq.getName(), tq.getNgaybatdau(), tq.getNgayketthuc()});
        }
    }
    
    public void Viewtable2(){
        DefaultTableModel model2 =(DefaultTableModel) this.table_tracking.getModel();
        model2.setNumRows(0);
        int n =1;
        for(thoi_quen x : thoiquen){
            model2.addRow(new Object[]{ x.getId(), x.getName(), x.getNgaybatdau(), "Đang thực hiện"});
        }
}
    public void ViewTable3() {
        DefaultTableModel model23 = (DefaultTableModel) this.jTable3.getModel();
        model23.setNumRows(0); // Đặt lại số dòng trong bảng về 0

        for (thoi_quen x : thoiquen) {
            // Lấy giá trị từ countDate1 và countDate2 với phương thức getOrDefault
            Integer count1 = countDate1.getOrDefault(x.getId(), 0); // Nếu không có thì trả về 0
            Integer count2 = countDate2.getOrDefault(x.getId(), 0); // Nếu không có thì trả về 0

            // Thêm dòng mới vào bảng
            model23.addRow(new Object[]{
                x.getId(),
                x.getName(),
                count1, // Sử dụng giá trị từ countDate1
                count2, // Sử dụng giá trị từ countDate2
                count1 + count2 // Tổng giá trị
            });
        }
    }

     private void loadThoiQuenData1() {
        List<thoi_quen> thoiQuenList = ListThoiQuen.getAllThoiQuen(); // Gọi phương thức từ ListThoiQuen
        DefaultTableModel model = (DefaultTableModel) this.table_tracking.getModel(); // Giả sử bạn có một JTable tên là table_thoiquen
        model.setNumRows(0); // Xóa dữ liệu cũ
        for (int i = 0;i < thoiQuenList.size();i++) {
            if(booleanCheck[i] == false){
                model.addRow(new Object[]{thoiQuenList.get(i).getId(), thoiQuenList.get(i).getName(), thoiQuenList.get(i).getNgaybatdau(),"Chưa thực hiện"});
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                                   boolean hasFocus, int row, int column) {
                        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if("Chưa thực hiện".equals(value)){
                            cellComponent.setForeground(Color.RED); 
                        }
                        else{
                            cellComponent.setForeground(Color.GREEN);
                        }
                        return cellComponent;
                    }
                };
                table_tracking.getColumnModel().getColumn(3).setCellRenderer(renderer);
            }else{
                model.addRow(new Object[]{thoiQuenList.get(i).getId(), thoiQuenList.get(i).getName(), thoiQuenList.get(i).getNgaybatdau(),"Đã hoàn thành"});
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                                   boolean hasFocus, int row, int column) {
                        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if("Đã hoàn thành".equals(value)){
                            cellComponent.setForeground(Color.GREEN); 
                        }
                        else{
                            cellComponent.setForeground(Color.RED);
                        }  
                        return cellComponent;
                    }
                };
                table_tracking.getColumnModel().getColumn(3).setCellRenderer(renderer);
            
                
            }
            table_tracking.repaint();
        }
        
                
//        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
//        renderer.setForeground(Color.RED); 
//
//        table_tracking.getColumnModel().getColumn(3).setCellRenderer(renderer);
//        booleanCheck = new boolean[table_tracking.getRowCount()];
    }

    // load lại dữ liệu vào bảng
    private void loadThoiQuenData() {
        List<thoi_quen> thoiQuenList = ListThoiQuen.getAllThoiQuen(); // Gọi phương thức từ ListThoiQuen
        DefaultTableModel model = (DefaultTableModel) this.table_thoiquen.getModel(); // Giả sử bạn có một JTable tên là table_thoiquen
        model.setNumRows(0); // Xóa dữ liệu cũ
        int n = 1; // Số thứ tự
        for (thoi_quen tq : thoiQuenList) {
            model.addRow(new Object[]{n++, tq.getId(), tq.getName(), tq.getNgaybatdau(), tq.getNgayketthuc()});
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tab1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tab2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        tab3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tab4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        tab5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jP1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        mtq_text = new javax.swing.JTextField();
        ttq_text = new javax.swing.JTextField();
        mota_text = new javax.swing.JTextField();
        nbd_date = new com.toedter.calendar.JDateChooser();
        nkt_date = new com.toedter.calendar.JDateChooser();
        btn_add = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_thoiquen = new javax.swing.JTable();
        label_add = new javax.swing.JLabel();
        label_fix = new javax.swing.JLabel();
        btn_savefix = new javax.swing.JButton();
        jP2 = new javax.swing.JPanel();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_tracking = new javax.swing.JTable();
        button_check = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPclock = new javax.swing.JLabel();
        jP3 = new javax.swing.JPanel();
        jP4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(51, 204, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(42, 58, 73));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo.png"))); // NOI18N
        jLabel1.setText("MANAGE HABITS");

        tab1.setBackground(new java.awt.Color(42, 58, 73));
        tab1.setForeground(new java.awt.Color(255, 255, 255));
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab1MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Thói quen");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout tab1Layout = new javax.swing.GroupLayout(tab1);
        tab1.setLayout(tab1Layout);
        tab1Layout.setHorizontalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );
        tab1Layout.setVerticalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        tab2.setBackground(new java.awt.Color(42, 58, 73));
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab2MouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Theo dõi thói quen");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout tab2Layout = new javax.swing.GroupLayout(tab2);
        tab2.setLayout(tab2Layout);
        tab2Layout.setHorizontalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab2Layout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        tab2Layout.setVerticalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        tab3.setBackground(new java.awt.Color(42, 58, 73));
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab3MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nhắc nhở");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout tab3Layout = new javax.swing.GroupLayout(tab3);
        tab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab3Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab3Layout.setVerticalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        tab4.setBackground(new java.awt.Color(42, 58, 73));
        tab4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab4MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Thống kê");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout tab4Layout = new javax.swing.GroupLayout(tab4);
        tab4.setLayout(tab4Layout);
        tab4Layout.setHorizontalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab4Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab4Layout.setVerticalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
        );

        tab5.setBackground(new java.awt.Color(42, 58, 73));
        tab5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab5MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Đăng xuất");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout tab5Layout = new javax.swing.GroupLayout(tab5);
        tab5.setLayout(tab5Layout);
        tab5Layout.setHorizontalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab5Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tab5Layout.setVerticalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(tab5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tab4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tab3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tab2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(tab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(tab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 51, 51));
        jPanel2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jPanel2AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jPanel2.setLayout(new javax.swing.OverlayLayout(jPanel2));

        jP1.setBackground(new java.awt.Color(255, 255, 255));
        jP1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jP1.setPreferredSize(new java.awt.Dimension(1875, 534));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Mã thói quen:");

        jLabel8.setFont(new java.awt.Font("Segoe UI Symbol", 0, 14)); // NOI18N
        jLabel8.setText("Tên thói quen:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Mô tả:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Ngày bắt đầu:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Ngày kết thúc:");

        mtq_text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mtq_textActionPerformed(evt);
            }
        });

        ttq_text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ttq_textActionPerformed(evt);
            }
        });

        nbd_date.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                nbd_dateAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        nkt_date.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                nkt_dateAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        btn_add.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_add.setText("Thêm");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });

        btn_save.setText("Lưu");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_cancel.setText("Huỷ");
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });

        table_thoiquen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã thói quen", "Tên thói quen", "Ngày bắt đầu", "Ngày kết thúc"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_thoiquen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_thoiquenMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_thoiquen);
        if (table_thoiquen.getColumnModel().getColumnCount() > 0) {
            table_thoiquen.getColumnModel().getColumn(0).setMinWidth(40);
            table_thoiquen.getColumnModel().getColumn(0).setMaxWidth(40);
            table_thoiquen.getColumnModel().getColumn(1).setMinWidth(100);
            table_thoiquen.getColumnModel().getColumn(1).setMaxWidth(100);
            table_thoiquen.getColumnModel().getColumn(3).setMinWidth(120);
            table_thoiquen.getColumnModel().getColumn(3).setMaxWidth(120);
            table_thoiquen.getColumnModel().getColumn(4).setMinWidth(120);
            table_thoiquen.getColumnModel().getColumn(4).setMaxWidth(120);
        }

        label_add.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        label_add.setText("Thêm thói quen:");

        label_fix.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        label_fix.setText("Sửa thói quen:");

        btn_savefix.setText("Lưu");
        btn_savefix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_savefixActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jP1Layout = new javax.swing.GroupLayout(jP1);
        jP1.setLayout(jP1Layout);
        jP1Layout.setHorizontalGroup(
            jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(label_fix, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(label_add, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(mtq_text, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(ttq_text, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(mota_text, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(nbd_date, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(nkt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_savefix, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addComponent(btn_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(btn_add, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jP1Layout.setVerticalGroup(
            jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(label_fix))
                    .addComponent(label_add, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mtq_text, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ttq_text, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(mota_text, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nbd_date, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jP1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(nkt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25)
                .addGroup(jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_savefix, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(btn_add, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(93, Short.MAX_VALUE))
            .addGroup(jP1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jPanel2.add(jP1);

        jP2.setBackground(new java.awt.Color(255, 255, 255));
        jP2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jP2AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jP2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jP2MouseClicked(evt);
            }
        });

        table_tracking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã thói quen", "Tên thói quen", "Ngày bắt đầu", "Hàng ngày"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_tracking.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                table_trackingAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        table_tracking.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_trackingMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_tracking);
        if (table_tracking.getColumnModel().getColumnCount() > 0) {
            table_tracking.getColumnModel().getColumn(0).setMinWidth(110);
            table_tracking.getColumnModel().getColumn(0).setMaxWidth(130);
            table_tracking.getColumnModel().getColumn(1).setMinWidth(150);
            table_tracking.getColumnModel().getColumn(1).setMaxWidth(150);
            table_tracking.getColumnModel().getColumn(2).setMinWidth(100);
            table_tracking.getColumnModel().getColumn(2).setMaxWidth(100);
        }

        button_check.setText("Check");
        button_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_checkActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(42, 58, 73));

        jPclock.setBackground(new java.awt.Color(51, 102, 255));
        jPclock.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jPclock.setForeground(new java.awt.Color(255, 255, 255));
        jPclock.setText("00:00:00 AM");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPclock)
                .addGap(164, 164, 164))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jPclock, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jP2Layout = new javax.swing.GroupLayout(jP2);
        jP2.setLayout(jP2Layout);
        jP2Layout.setHorizontalGroup(
            jP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP2Layout.createSequentialGroup()
                .addGroup(jP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jP2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jCalendar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(button_check, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(117, Short.MAX_VALUE))
        );
        jP2Layout.setVerticalGroup(
            jP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_check, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
        );

        jPanel2.add(jP2);

        jP3.setBackground(new java.awt.Color(255, 255, 255));
        jP3.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jP3AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                jP3AncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout jP3Layout = new javax.swing.GroupLayout(jP3);
        jP3.setLayout(jP3Layout);
        jP3Layout.setHorizontalGroup(
            jP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1875, Short.MAX_VALUE)
        );
        jP3Layout.setVerticalGroup(
            jP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 625, Short.MAX_VALUE)
        );

        jPanel2.add(jP3);

        jP4.setBackground(new java.awt.Color(255, 255, 255));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã thói quen", "Tên thói quen", "Số ngày hoạt động", "Số ngày bỏ qua", "Tổng ngày"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 51));
        jLabel12.setText("Thống kê");

        javax.swing.GroupLayout jP4Layout = new javax.swing.GroupLayout(jP4);
        jP4.setLayout(jP4Layout);
        jP4Layout.setHorizontalGroup(
            jP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jP4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 949, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 926, Short.MAX_VALUE))
            .addGroup(jP4Layout.createSequentialGroup()
                .addGap(383, 383, 383)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jP4Layout.setVerticalGroup(
            jP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jP4Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(jP4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        jP1.setVisible(true);
        jP2.setVisible(false);
        jP3.setVisible(false);
        jP4.setVisible(false);
        Viewtable1();
//        tab5.setVisible(false);


    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        jP1.setVisible(false);
        jP2.setVisible(true);
        jP3.setVisible(false);
        jP4.setVisible(false);
        clock();
//        loadThoiQuenData1();
        

//        tab5.setVisible(false);

    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        jP1.setVisible(false);
        jP2.setVisible(false);
        jP3.setVisible(true);
        jP4.setVisible(false);

// Tạo JDialog
        JDialog dialog_eg = new JDialog();
        dialog_eg.setSize(250, 110);
        dialog_eg.setTitle("Tính năng đang cập nhật");
        dialog_eg.setLayout(new BorderLayout());

// Thêm thông điệp vào JDialog
        JLabel messageLabel = new JLabel("Tính năng đang cập nhật!", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dialog_eg.add(messageLabel, BorderLayout.NORTH);

// Tạo panel chứa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

// Tạo hai nút "OK" và "Canny"
        JButton okButton = new JButton("OK");
        JButton cannyButton = new JButton("Canny");

// Thêm các nút vào buttonPanel
        buttonPanel.add(okButton);
        buttonPanel.add(cannyButton);

// Thêm buttonPanel vào JDialog ở phần dưới
        dialog_eg.add(buttonPanel, BorderLayout.SOUTH);

// Định nghĩa hành động cho các nút
        ActionListener closeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Đóng JDialog
                dialog_eg.dispose();
            }
        };

// Gán hành động cho cả hai nút
        okButton.addActionListener(closeAction);
        cannyButton.addActionListener(closeAction);

// Hiển thị JDialog
        dialog_eg.setLocationRelativeTo(null); // Hiển thị ở giữa màn hình
        dialog_eg.setVisible(true);
//        tab5.setVisible(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
        jP1.setVisible(false);
        jP2.setVisible(false);
        jP3.setVisible(false);
        jP4.setVisible(true);
//        tab5.setVisible(false);

    }//GEN-LAST:event_jLabel5MouseClicked

    private void tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MouseClicked
        // TODO add your handling code here:

        jP1.setVisible(true);
        jP2.setVisible(false);
        jP3.setVisible(false);
        jP4.setVisible(false);
//        tab5.setVisible(false);

    }//GEN-LAST:event_tab1MouseClicked

    private void tab2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab2MouseClicked
        // TODO add your handling code here:

        jP1.setVisible(false);
        jP2.setVisible(true);
        jP3.setVisible(false);
        jP4.setVisible(false);
        Viewtable2();
        loadThoiQuenData1();
//      tab5.setVisible(false);
    }//GEN-LAST:event_tab2MouseClicked

    private void tab3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab3MouseClicked
        // TODO add your handling code here:
        jP1.setVisible(false);
        jP2.setVisible(false);
        jP3.setVisible(true);
        jP4.setVisible(false);
//        tab5.setVisible(false);
    }//GEN-LAST:event_tab3MouseClicked

    private void tab4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab4MouseClicked
        // TODO add your handling code here:
        jP1.setVisible(false);
        jP2.setVisible(false);
        jP3.setVisible(false);
        jP4.setVisible(true);
//        tab5.setVisible(false);
    }//GEN-LAST:event_tab4MouseClicked

    private void tab5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab5MouseClicked
        // TODO add your handling code here:
        this.dispose();
        user userLogin = null;
        new Dangnhap(userLogin).setVisible(true);
    }//GEN-LAST:event_tab5MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        this.dispose();
        user userLogin = null;
        new Dangnhap(userLogin).setVisible(true);
    }//GEN-LAST:event_jLabel4MouseClicked

    private void button_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_checkActionPerformed
            // TODO add your handling code here:
        
    }//GEN-LAST:event_button_checkActionPerformed

    private void ttq_textActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ttq_textActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ttq_textActionPerformed

    private void nbd_dateAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_nbd_dateAncestorAdded
        // TODO add your handling code here:
        nbd_date.setDateFormatString("dd/MM/yyyy");


    }//GEN-LAST:event_nbd_dateAncestorAdded

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        // TODO add your handling code here:
        label_add.setVisible(true);
        mtq_text.setEditable(true);
        ttq_text.setEditable(true);
        mota_text.setEditable(false);
        nbd_date.setEnabled(true);
        nkt_date.setEnabled(true);

        btn_save.setVisible(true);
        btn_cancel.setVisible(true);
        btn_add.setVisible(false);
        Viewtable1();
    }//GEN-LAST:event_btn_addActionPerformed
    public boolean[] addFalseToEnd(boolean[] array) {
    // Tạo mảng mới với kích thước lớn hơn mảng ban đầu
        boolean[] newArray = new boolean[array.length + 1];

        // Sao chép tất cả phần tử từ mảng ban đầu sang mảng mới
        System.arraycopy(array, 0, newArray, 0, array.length);

        // Gán giá trị false cho phần tử cuối cùng
        newArray[array.length] = false;

        return newArray;
    }

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        // TODO add your handling code here:
        // Lấy thông tin từ textjield
        String id = mtq_text.getText();
        String name = ttq_text.getText();
        Date ngaybatdau = nbd_date.getDate();
        Date ngayketthuc = nkt_date.getDate();
        boolean checkid = true;
        // add thói quen
        // check xem đã tồn tại mã thói quen chưa
        for (thoi_quen x : thoiquen) {
            if (x.getId().equals(id)) {
                checkid = false;
                break;
            }
        }
        if (name.length() == 0 || id.length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Không được để trống thông tin");
        } else if (id.length() != 4) {
            JOptionPane.showMessageDialog(rootPane, "Mã thói quen phải có 4 kí tự");
        } else if (!checkid) {
            JOptionPane.showMessageDialog(rootPane, "Mã thói quen đã tồn tại");
        } else if (ngayketthuc.before(ngaybatdau)) {
            // Kiểm tra ngày kết thúc phải lớn hơn ngày bắt đầu
            JOptionPane.showMessageDialog(rootPane, "Ngày kết thúc phải lớn hơn ngày bắt đầu");

        } else {
            thoiquenadd add = new thoiquenadd();
            add.addThoiQuen(id, name, new java.sql.Date(ngaybatdau.getTime()), new java.sql.Date(ngayketthuc.getTime()));
            java.sql.Date sqlNgayBatDau = new java.sql.Date(ngaybatdau.getTime());
            thoiquen = ListThoiQuen.getAllThoiQuen();

            // Gọi phương thức addThoiQuen chính thức mà bạn đã triển khai
            Viewtable1();
            addFalseToEnd(booleanCheck);
            writeFile();
            loadThoiQuenData1();
            label_add.setVisible(false);
            btn_save.setVisible(false);
            btn_cancel.setVisible(false);
            btn_add.setVisible(true);

            mtq_text.setText("");
            ttq_text.setText("");
            nbd_date.setDate(null);
            nkt_date.setDate(null);
            mtq_text.setEditable(false);
            ttq_text.setEditable(false);
            nbd_date.setEnabled(false);
            nkt_date.setEnabled(false);
        }

        // Sửa thói quen
        // Hiển thị thông báo thêm thành công
//        JOptionPane.showMessageDialog(rootPane, "Thêm thói quen thành công");
        

    }//GEN-LAST:event_btn_saveActionPerformed

    private void mtq_textActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mtq_textActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mtq_textActionPerformed
    public boolean[] removeElementAt(boolean[] array, int index) {
    // Kiểm tra chỉ số hợp lệ
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        // Tạo mảng mới với kích thước nhỏ hơn mảng ban đầu
        boolean[] newArray = new boolean[array.length - 1];

        // Sao chép các phần tử trước vị trí cần xóa
        for(int i = 0;i < index;i++){
            newArray[i] = array[i];
        }
        for(int i = index+1;i < array.length;i++){
            newArray[i-1] = array[i];
        }

        return newArray;
    }
    private void table_thoiquenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_thoiquenMouseClicked
        // TODO add your handling code here:
        JDialog dialog_ego = new JDialog();

        dialog_ego.setSize(170, 100);
        dialog_ego.setTitle("Mời lựa chọn");

        dialog_ego.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton button1 = new JButton("Sửa");
        JButton button2 = new JButton("Xoá");

        int pos = this.table_thoiquen.getSelectedRow();
        //button Sửa
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table_thoiquen.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để chỉnh sửa.");
                    return;
                }
                stt = selectedRow;
                mtq_text.setText(table_thoiquen.getValueAt(pos, 1).toString());
                ttq_text.setText(table_thoiquen.getValueAt(pos, 2).toString());
                nbd_date.setDate((Date) table_thoiquen.getValueAt(pos, 3));
                nkt_date.setDate((Date) table_thoiquen.getValueAt(pos, 4));
                dialog_ego.setVisible(false);
                label_fix.setVisible(true);
                label_add.setVisible(false);

                btn_savefix.setVisible(true);
                btn_cancel.setVisible(true);
                btn_add.setVisible(false);
                btn_save.setVisible(false);

                mtq_text.setEditable(true);
                ttq_text.setEditable(true);
                nbd_date.setEnabled(true);
                nkt_date.setEnabled(true);
            }
        });

        //button Xoá
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int response = JOptionPane.showConfirmDialog(dialog_ego,
                        "Bạn thật sự muốn xóa?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                //Xoá trong list và view lại bảng
                if (response == JOptionPane.YES_OPTION) {
//                    JOptionPane.showMessageDialog(dialog_ego, "Đã xóa!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                    String mathoiquen = table_thoiquen.getValueAt(pos, 1).toString();
                    // Xoá trong CSDL
                    thoiquendelete deleteHabit = new thoiquendelete();  // Sử dụng class thoiquendelete
                    deleteHabit.deleteThoiQuen(mathoiquen);
                    for (thoi_quen x : thoiquen) {
                        if (x.getId().equals(mathoiquen)) {
                            thoiquen.remove(x);
                            break;
                        }
                    }
//                    JOptionPane.showMessageDialog(dialog_ego, "Đã xóa!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println(pos);
                    booleanCheck = removeElementAt(booleanCheck, pos);
                    for(boolean x : booleanCheck){
                        System.out.println(x);
                    }
                    
                    countDate1.remove(mathoiquen);
                    countDate2.remove(mathoiquen);
                    Viewtable1();
                    writeFile();
                    loadThoiQuenData1();
                    dialog_ego.setVisible(false);
                    ttq_text.setText("");
                    nbd_date.setDate(null);
                    nkt_date.setDate(null);
                    mtq_text.setEditable(false);
                    ttq_text.setEditable(false);
                    nbd_date.setEnabled(false);
                    nkt_date.setEnabled(false);
                    
                    
                } else {
                    dialog_ego.setVisible(false);
                }
            }
        });

        dialog_ego.add(button1);
        dialog_ego.add(button2);
        dialog_ego.setLocationRelativeTo(null);
        dialog_ego.setVisible(true);
    }//GEN-LAST:event_table_thoiquenMouseClicked

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        // TODO add your handling code here:
        btn_add.setVisible(true);
        btn_cancel.setVisible(false);
        btn_save.setVisible(false);
        label_add.setVisible(false);
        label_fix.setVisible(false);
        btn_savefix.setVisible(false);

        ttq_text.setText("");
        nbd_date.setDate(null);
        nkt_date.setDate(null);
        mtq_text.setEditable(false);
        ttq_text.setEditable(false);
        nbd_date.setEnabled(false);
        nkt_date.setEnabled(false);
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void btn_savefixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_savefixActionPerformed
        // TODO add your handling code here:
        String id = mtq_text.getText();
        String name = ttq_text.getText();
        Date ngaybatdau = nbd_date.getDate();
        Date ngayketthuc = nkt_date.getDate();
        boolean check_habit = true;
        for (int i = 0; i < thoiquen.size(); i++) {
            if (i == stt) {
                continue;
            }
            if (thoiquen.get(i).getId().equals(id)) {
                JOptionPane.showMessageDialog(rootPane, "Mã thói quen đã tồn tại");
                check_habit = false;
                break;
            }
        }
        if (name.length() == 0 || id.length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Không được để trống");
        } else if (id.length() != 4) {
            JOptionPane.showMessageDialog(rootPane, "Mã thói quen phải có 4 kí tự");
        } else if (ngaybatdau == null || ngayketthuc == null) {
            JOptionPane.showMessageDialog(rootPane, "Ngày bắt đầu và ngày kết thúc không được để trống");
        } else if (ngaybatdau.after(ngayketthuc)) {
            JOptionPane.showMessageDialog(rootPane, "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu");

        } else if (check_habit) {
            thoiquen.get(stt).setId(id);
            thoiquen.get(stt).setName(name);
            thoiquen.get(stt).setNgaybatdau(ngaybatdau);
            thoiquen.get(stt).setNgayketthuc(ngayketthuc);
            Viewtable1();

            btn_add.setVisible(true);
            btn_save.setVisible(false);
            btn_savefix.setVisible(false);
            btn_cancel.setVisible(false);
            label_fix.setVisible(false);

            ttq_text.setText("");
            mtq_text.setText("");
            nbd_date.setDate(null);
            nkt_date.setDate(null);
            mtq_text.setEditable(false);
            ttq_text.setEditable(false);
            nbd_date.setEnabled(false);
            nkt_date.setEnabled(false);

            thoiquenedit thoiQuenEdit = new thoiquenedit();
            thoiQuenEdit.updateThoiQuen(
                    id,
                    name,
                    new java.sql.Date(ngaybatdau.getTime()),
                    new java.sql.Date(ngayketthuc.getTime())
            );
//            JOptionPane.showMessageDialog(rootPane, "Cập nhật thói quen thành công!");
        }


    }//GEN-LAST:event_btn_savefixActionPerformed

    private void nkt_dateAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_nkt_dateAncestorAdded
        // TODO add your handling code here:
        nkt_date.setDateFormatString("dd/MM/yyyy");
        JTextField textField = (JTextField) nkt_date.getDateEditor().getUiComponent();
        String date = textField.getText();
    }//GEN-LAST:event_nkt_dateAncestorAdded

    private void jPanel2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jPanel2AncestorAdded
        // TODO add your handling code here:
        Viewtable1();
    }//GEN-LAST:event_jPanel2AncestorAdded

    private void jP2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jP2AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jP2AncestorAdded

    private void jP2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jP2MouseClicked
        // TODO add your handling code here:
        Viewtable2();
        loadThoiQuenData1();
    }//GEN-LAST:event_jP2MouseClicked

    private void table_trackingAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_table_trackingAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_table_trackingAncestorAdded
  
    private void table_trackingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_trackingMouseClicked
        // TODO add your handling code here:
        
        int selectedRow = table_tracking.rowAtPoint(evt.getPoint());

        button_check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow != -1 && booleanCheck[selectedRow] == false) { 
                    booleanCheck[selectedRow] = true;
                }
                writeFile();
                loadThoiQuenData1();
            }
        });
        

    }//GEN-LAST:event_table_trackingMouseClicked

    private void jP3AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jP3AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jP3AncestorAdded

    private void jP3AncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jP3AncestorMoved
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jP3AncestorMoved

     
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
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    user userLogin = null;
                    try {
                        new main(userLogin).setVisible(true);
                    } catch (IOException ex) {
                        Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_savefix;
    private javax.swing.JButton button_check;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jP1;
    private javax.swing.JPanel jP2;
    private javax.swing.JPanel jP3;
    private javax.swing.JPanel jP4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel jPclock;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel label_add;
    private javax.swing.JLabel label_fix;
    private javax.swing.JTextField mota_text;
    private javax.swing.JTextField mtq_text;
    private com.toedter.calendar.JDateChooser nbd_date;
    private com.toedter.calendar.JDateChooser nkt_date;
    private javax.swing.JPanel tab1;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JPanel tab4;
    private javax.swing.JPanel tab5;
    private javax.swing.JTable table_thoiquen;
    private javax.swing.JTable table_tracking;
    private javax.swing.JTextField ttq_text;
    // End of variables declaration//GEN-END:variables
}
