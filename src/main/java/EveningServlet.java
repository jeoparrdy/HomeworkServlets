import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/evening")
public class EveningServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        HttpSession httpSession = req.getSession();
        String name = "";
        httpSession.setAttribute("name",req.getParameter("name"));
        if(req.getParameter("name") != null){
            name = (String) httpSession.getAttribute("name");
        } else {
            name = "my friend";
        }
        writer.println("Good evening, " + name);
        writer.flush();
    }
}
