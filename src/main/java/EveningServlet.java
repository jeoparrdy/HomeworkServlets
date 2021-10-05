import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/evening")
public class EveningServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        HttpSession httpSession = req.getSession();

        var sessionName =(String) httpSession.getAttribute("name");
        var reqName = req.getParameter("name");
        String name ="";
        if (sessionName == null){
            if (reqName != null) {
                httpSession.setAttribute("name", reqName);
                name = reqName;
            }else {
                name = "my friend";
            }

        } else {
            name = (String) httpSession.getAttribute("name");
        }


        writer.println("Good evening, " + name);
        writer.flush();
    }
}
