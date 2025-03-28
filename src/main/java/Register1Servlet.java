

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Register1Servlet")
public class Register1Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String address = request.getParameter("address").trim();  // Trim to remove leading/trailing spaces
        String number = request.getParameter("number").trim();

        // Password validation
        if (password.length() < 6) {
            out.println("<script>alert('Password must be at least 6 characters long!'); window.location='register1.html';</script>");
            return;
        }

        // Address validation (Minimum 10 characters)
        if (address.length() < 10 || address.length() > 255) {
            out.println("<script>alert('Address must be between 10 to 255 characters!'); window.location='register1.html';</script>");
            return;
        }

        // Phone number validation (Must be 10 digits)
        if (!Pattern.matches("\\d{10}", number)) {
            out.println("<script>alert('Invalid phone number! Must be 10 digits.'); window.location='register1.html';</script>");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_ordering", "root", "dhanashree@123#");

            // Check if the email is already registered
            String checkQuery = "SELECT email FROM users WHERE email = ?";
            ps = con.prepareStatement(checkQuery);
            ps.setString(1, email);
            if (ps.executeQuery().next()) {
                out.println("<script>alert('Email already registered! Try logging in.'); window.location='login1.html';</script>");
                return;
            }

            // Insert user data including name, email, password, address, and number
            String insertQuery = "INSERT INTO users (name, email, password, address, number) VALUES (?, ?, ?, ?, ?)";
            ps = con.prepareStatement(insertQuery);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, address);
            ps.setString(5, number);

            int result = ps.executeUpdate();
            if (result > 0) {
                out.println("<script>alert('Registration Successful!'); window.location='login1.html';</script>");
            } else {
                out.println("<script>alert('Registration Failed! Please try again.'); window.location='register1.html';</script>");
            }
        } catch (Exception e) {
            out.println("<script>alert('Error: " + e.getMessage() + "'); window.location='register1.html';</script>");
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
