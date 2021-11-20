import net.proteanit.sql.DbUtils;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JLabel Label;
    private JTextField Name_Text;
    private JTextField Salary_Text;
    private JTextField Mobile_Text;
    private JButton SAVEButton;
    private JTable table1;
    private JButton UPDATEButton;
    private JButton DELETEButton;
    private JButton SEARCHButton;
    private JTextField txtid;
    private JScrollPane table_1;

    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;

    public void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "");
            Statement statement = con.createStatement();

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    void table_load()
    {
        try
        {
            pst = con.prepareStatement("select * from employee_table");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void resetidCount() throws SQLException {
        pst = con.prepareStatement("SELECT COUNT(*) AS RowCnt FROM employee_table");
        ResultSet a = pst.executeQuery();

        if(a.next()) {
            if (a.getInt(1) == 0) {
                PreparedStatement resetCount = con.prepareStatement("ALTER TABLE employee_table AUTO_INCREMENT = 1;");
                resetCount.executeUpdate();
            }
        }
    }


    public Employee() throws SQLException {
        connect();
        table_load();
        resetidCount();


        SAVEButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String empname,salary,mobile;

                empname = Name_Text.getText();
                salary = Salary_Text.getText();
                mobile = Mobile_Text.getText();

                try{
                    pst = con.prepareStatement("Insert into employee_table(empname,salary,mobile)values(?,?,?)");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobile);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added!!");

                    Name_Text.setText("");
                    Salary_Text.setText("");
                    Mobile_Text.setText("");
                    Name_Text.requestFocus();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });
        SEARCHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    String empid = txtid.getText();

                    pst = con.prepareStatement("select empname,salary,mobile from employee_table where id = ?");
                    pst.setString(1, empid);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next())
                    {
                        String empname = rs.getString(1);
                        String emsalary = rs.getString(2);
                        String emmobile = rs.getString(3);

                        Name_Text.setText(empname);
                        Salary_Text.setText(emsalary);
                        Mobile_Text.setText(emmobile);

                    }
                    else
                    {
                        Name_Text.setText("");
                        Salary_Text.setText("");
                        Mobile_Text.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid Employee No");

                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empid,empname,salary,mobile;
                empname = Name_Text.getText();
                salary = Salary_Text.getText();
                mobile = Mobile_Text.getText();
                empid = txtid.getText();

                try {
                    pst = con.prepareStatement("update employee_table set empname = ?,salary = ?,mobile = ? where id = ?");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobile);
                    pst.setString(4, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Updated!!");
                    table_load();
                    Name_Text.setText("");
                    Salary_Text.setText("");
                    Mobile_Text.setText("");
                    Name_Text.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }

        });
        DELETEButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String empid;
                empid = txtid.getText();

                try {
                    pst = con.prepareStatement("delete from employee_table where id = ?");

                    pst.setString(1, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleted!!");
                    table_load();
                    Name_Text.setText("");
                    Salary_Text.setText("");
                    Mobile_Text.setText("");
                    Name_Text.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }

}
