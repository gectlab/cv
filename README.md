# CV Display Application (Java Swing + MySQL + JDBC)

This project demonstrates a **simple CV Display System** using:

- **MySQL** (Database)
- **Java JDBC** (Database connectivity)
- **Java Swing** (GUI display)

The program retrieves CV information from a MySQL database and displays it in a Swing window.

This README provides a **complete step-by-step guide**, including installation, troubleshooting, version fixes, and common mistakes encountered during development.

---

# 1. Project Structure

```
cv-app/
│
├── lib/
│ └── mysql.jar (MySQL JDBC driver)
│
├── DBConnection.java (Database connection handler)
├── CVDisplay.java (Swing UI + queries)
├── database.sql (Database creation + sample data)
│
└── README.md
```

---

# 2. System Requirements

Recommended environment:

| Component | Version |
|--------|--------|
| Ubuntu | 20+ |
| MySQL | 8.x |
| Java JDK | 17+ |
| JDBC Driver | mysql-connector-j 8.x |

---

# 3. Step 1 — Start MySQL

Before anything else, start the MySQL server.

```bash
sudo systemctl start mysql
```

Check if MySQL is running:

```bash
sudo systemctl status mysql
```

If MySQL is not running, the program cannot connect to the database.

---

# 4. Step 2 — Access MySQL

Ubuntu lab machines usually allow login using:

```bash
sudo mysql
```

If password authentication is enabled:

```bash
mysql -u root -p
```

---

# 5. Step 3 — Change MySQL Root Password (Optional)

Inside MySQL:

```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'password123';
FLUSH PRIVILEGES;
```

Exit:

```bash
exit
```

Now login using:

```bash
mysql -u root -p
```

---

# 6. Step 4 — Create Database Using SQL Script

Create a file called **database.sql**.

```bash
nano database.sql
```

Paste:

```sql
CREATE DATABASE IF NOT EXISTS cvdb;
USE cvdb;

CREATE TABLE user(
userid INT PRIMARY KEY,
firstname VARCHAR(50),
lastname VARCHAR(50),
email VARCHAR(100),
phoneno VARCHAR(15),
address VARCHAR(200)
);

CREATE TABLE qualification(
qid INT PRIMARY KEY,
userid INT,
highestqualification VARCHAR(100),
university VARCHAR(100),
yearofgraduation INT,
cgpa DECIMAL(3,2),
FOREIGN KEY(userid) REFERENCES user(userid)
);

CREATE TABLE experience(
eid INT PRIMARY KEY,
userid INT,
nameoforganization VARCHAR(100),
noofyears INT,
designation VARCHAR(100),
startdate DATE,
enddate DATE,
FOREIGN KEY(userid) REFERENCES user(userid)
);

CREATE TABLE skills(
sid INT PRIMARY KEY,
userid INT,
skills VARCHAR(100),
proficiency VARCHAR(50),
FOREIGN KEY(userid) REFERENCES user(userid)
);

CREATE TABLE projects(
pid INT PRIMARY KEY,
userid INT,
noofprojects INT,
titles VARCHAR(200),
FOREIGN KEY(userid) REFERENCES user(userid)
);

INSERT INTO user VALUES
(1,'John','Doe','john@gmail.com','9999999999','New York');

INSERT INTO qualification VALUES
(1,1,'B.Tech','MIT',2022,8.7);

INSERT INTO experience VALUES
(1,1,'Google',2,'Software Engineer','2022-01-01','2024-01-01');

INSERT INTO skills VALUES
(1,1,'Java','Advanced'),
(2,1,'SQL','Intermediate');

INSERT INTO projects VALUES
(1,1,3,'Student System, Chat App, Portfolio');
```

Execute:

```bash
sudo mysql < database.sql
```

Verify:

```bash
sudo mysql
USE cvdb;
SELECT * FROM user;
```

---

# 7. Step 5 — Install Java

If javac is missing:

`javac: command not found`

Install JDK:

```bash
sudo apt install openjdk-17-jdk
```

Verify installation:

```bash
java -version
javac -version
```

Both should match.

Example:

```
openjdk version "17"
javac 17
```

---

# 8. Step 6 — Choose Correct Java Version

Check installed Java versions:

```bash
sudo update-alternatives --config java
```

Example:

```
1  java-21-openjdk
2  java-17-openjdk
```

Choose a stable version like 17 or 21.

Set compiler version:

```bash
sudo update-alternatives --config javac
```

Make sure java and javac use the same version.

---

# 9. Step 7 — Download MySQL JDBC Driver

Create library folder:

```bash
mkdir lib
cd lib
```

Download driver:

```bash
wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar
```

Rename for easier usage:

```bash
mv mysql-connector-j-8.3.0.jar mysql.jar
```

Return:

```bash
cd ..
```

---

# 10. Step 8 — DBConnection.java

```java
import java.sql.*;

public class DBConnection {

    public static Connection getConnection() {

        Connection conn = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cvdb",
                    "root",
                    "password123");

        } catch(Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
```

---

# 11. Step 9 — CVDisplay.java

```java
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
```

---

# 12. Step 10 — Compile Program

Always include the JDBC driver:

```bash
javac -cp ".:lib/mysql.jar" *.java
```

If successful, .class files appear.

---

# 13. Step 11 — Run Program

```bash
java -cp ".:lib/mysql.jar" CVDisplay
```

A Swing window will open displaying the CV.

---

# 14. Common Errors and Fixes

### MySQL not running
`Can't connect to MySQL`

Fix:
```bash
sudo systemctl start mysql
```

### Database already exists
`ERROR 1007 database exists`

Fix:
```bash
DROP DATABASE cvdb;
```
or use
`CREATE DATABASE IF NOT EXISTS cvdb`

### javac not found
Install JDK:
```bash
sudo apt install openjdk-17-jdk
```

### Java version mismatch
Check:
```bash
java -version
javac -version
```
Set correct version:
```bash
sudo update-alternatives --config java
sudo update-alternatives --config javac
```

### JDBC Driver missing
`ClassNotFoundException: com.mysql.cj.jdbc.Driver`

Fix:
Download MySQL connector and compile with classpath.

### Empty Swing window
Cause: database has no records.

Fix:
```sql
SELECT * FROM user;
```
Insert records if empty.

---

# 15. JDBC Architecture

```
MySQL Database
      ↓
JDBC Driver
      ↓
Java Application
      ↓
Swing GUI
```

---

# 16. Steps of JDBC

1. Load Driver
2. Establish Connection
3. Create Statement
4. Execute Query
5. Process ResultSet
6. Close Connection

---

# 17. Quick Lab Workflow

```bash
sudo systemctl start mysql
sudo mysql < database.sql

javac -cp ".:lib/mysql.jar" *.java
java -cp ".:lib/mysql.jar" CVDisplay
```
