package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

/**
 * @author: 张鹏飞
 * @company： 西安尚观科技IT教育学院
 * @Official： www.uplookedu.com
 */
@WebServlet("/SecKillServlet")
public class SecKillServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userid = new Random().nextInt(50000) +"" ;

        String prodid =request.getParameter("prodid");

        boolean if_success=SecKill_redis.doSecKill(userid,prodid);

        response.getWriter().print(if_success);
    }
}
