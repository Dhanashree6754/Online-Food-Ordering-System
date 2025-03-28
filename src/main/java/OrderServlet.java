

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/food_ordering";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "dhanashree@123#";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        if (email == null) {
            response.sendRedirect("login1.html");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            double totalAmount = 0;
            PreparedStatement ps = conn.prepareStatement("INSERT INTO orders (user_email, item_name, total_price) VALUES (?, ?, ?)");

            for (String itemName : request.getParameterValues("menuItem")) {
                PreparedStatement itemStmt = conn.prepareStatement("SELECT price FROM menu WHERE name = ?");
                itemStmt.setString(1, itemName);
                ResultSet rs = itemStmt.executeQuery();

                if (rs.next()) {
                    double price = rs.getDouble("price");
                    totalAmount += price;

                    // Insert into orders table
                    ps.setString(1, email);
                    ps.setString(2, itemName);
                    ps.setDouble(3, price);
                    ps.executeUpdate();
                }
            }

            conn.close();
            
            // Order successfully placed, now redirect to success1.html
            response.sendRedirect("success1.html");

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
