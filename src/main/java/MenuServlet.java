

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MenuServlet")

public class MenuServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/food_ordering";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "dhanashree@123#";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Food Menu</title>");
        out.println("<style>");
        out.println("body { background-color: #d3d3d3; text-align: center; font-family: Arial, sans-serif; }"); 
        out.println(".container { width: 80%; margin: auto; background-color: white; padding: 20px; border-radius: 10px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); min-height: 600px; }");
        out.println("h1 { font-size: 28px; font-weight: bold; }");
        out.println("table { width: 100%; border-collapse: collapse; background-color: white; }");
        out.println("th, td { border: 2px solid #bbb; padding: 15px; text-align: center; font-size: 18px; font-weight: bold; }"); // Light gray border
        out.println("th { background-color: #ffcc00; color: black; }");
        out.println("input[type='checkbox'] { transform: scale(1.5); }");
        out.println("button { background-color: #28a745; color: white; padding: 10px 15px; font-size: 16px; border: none; cursor: pointer; border-radius: 5px; margin-top: 20px; }");
        out.println("button:hover { background-color: #218838; }");
        out.println("</style>");

        // ✅ `orderItems()` JavaScript Function
        out.println("<script>");
        out.println("function orderItems() {");
        out.println("   let selectedItems = [];");
        out.println("   let selectedPrices = [];"); 

        out.println("   document.querySelectorAll('input[name=\"menuItem\"]:checked').forEach(item => {");
        out.println("       selectedItems.push(item.value);");
        out.println("       selectedPrices.push(item.getAttribute('data-price'));"); // ✅ Price extract 
        out.println("   });");

        out.println("   if (selectedItems.length > 0) {");
        out.println("       window.location.href = 'order.html?items=' + encodeURIComponent(selectedItems.join(',')) + '&prices=' + encodeURIComponent(selectedPrices.join(','));"); // ✅ Prices send karo
        out.println("   } else {");
        out.println("       alert('Please select at least one item!');");
        out.println("   }");
        out.println("}");
        out.println("</script>");

        
        
        
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Food Menu</h1>");
        out.println("<table><tr><th>Select</th><th>Name</th><th>Price</th><th>Category</th><th>Image</th></tr>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM menu");

            while (rs.next()) {
                String itemName = rs.getString("name");
                double itemPrice = rs.getDouble("price");

                out.println("<tr>");
            
                // ✅ `data-price` attribute add 
               
                out.println("<td><input type='checkbox' name='menuItem' value='" + itemName + "' data-price='" + itemPrice + "'></td>");
                out.println("<td>" + itemName + "</td>");
                out.println("<td>" + itemPrice + "</td>");
                out.println("<td>" + rs.getString("category") + "</td>");
                out.println("<td><img src='" + rs.getString("image_url") + "' width='100' height='100' style='border-radius: 10px;'></td>");
                out.println("</tr>");
            }
            conn.close();
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }

        out.println("</table>");
        out.println("<br><button onclick='orderItems()'>Order Selected Items</button>");
        out.println("</div>");
        out.println("</body></html>");
    }
}
