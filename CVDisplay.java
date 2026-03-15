import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CVDisplay extends JFrame {

    JTextArea area;

    public CVDisplay() {

        setTitle("CV Viewer");
        setSize(700,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        area = new JTextArea();

        // Better formatting
        area.setFont(new Font("Monospaced", Font.BOLD, 18));
        area.setMargin(new Insets(20,20,20,20));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane pane = new JScrollPane(area);

        add(pane, BorderLayout.CENTER);

        loadCV(1);

        setVisible(true);
    }

    public void loadCV(int userid) {

        try {

            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            StringBuilder cv = new StringBuilder();

            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM user WHERE userid="+userid);

            while(rs.next()) {

                cv.append("===== PERSONAL DETAILS =====\n\n");

                cv.append("Name: ")
                  .append(rs.getString("firstname"))
                  .append(" ")
                  .append(rs.getString("lastname"))
                  .append("\n");

                cv.append("Email: ")
                  .append(rs.getString("email"))
                  .append("\n");

                cv.append("Phone: ")
                  .append(rs.getString("phoneno"))
                  .append("\n");

                cv.append("Address: ")
                  .append(rs.getString("address"))
                  .append("\n\n");
            }

            rs = stmt.executeQuery(
                    "SELECT * FROM qualification WHERE userid="+userid);

            while(rs.next()) {

                cv.append("===== EDUCATION =====\n\n");

                cv.append("Qualification: ")
                  .append(rs.getString("highestqualification"))
                  .append("\nUniversity: ")
                  .append(rs.getString("university"))
                  .append("\nYear: ")
                  .append(rs.getInt("yearofgraduation"))
                  .append("\nCGPA: ")
                  .append(rs.getDouble("cgpa"))
                  .append("\n\n");
            }

            rs = stmt.executeQuery(
                    "SELECT * FROM experience WHERE userid="+userid);

            while(rs.next()) {

                cv.append("===== EXPERIENCE =====\n\n");

                cv.append("Organization: ")
                  .append(rs.getString("nameoforganization"))
                  .append("\nDesignation: ")
                  .append(rs.getString("designation"))
                  .append("\nYears: ")
                  .append(rs.getInt("noofyears"))
                  .append("\n\n");
            }

            rs = stmt.executeQuery(
                    "SELECT * FROM skills WHERE userid="+userid);

            cv.append("===== SKILLS =====\n\n");

            while(rs.next()) {

                cv.append("- ")
                  .append(rs.getString("skills"))
                  .append(" (")
                  .append(rs.getString("proficiency"))
                  .append(")\n");
            }

            cv.append("\n");

            rs = stmt.executeQuery(
                    "SELECT * FROM projects WHERE userid="+userid);

            while(rs.next()) {

                cv.append("\n===== PROJECTS =====\n\n");

                cv.append("Number of Projects: ")
                  .append(rs.getInt("noofprojects"))
                  .append("\nTitles: ")
                  .append(rs.getString("titles"))
                  .append("\n");
            }

            area.setText(cv.toString());

            conn.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new CVDisplay();
    }
}
