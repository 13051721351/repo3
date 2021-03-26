package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class CodeVerifyServlet
 */
@WebServlet("/CodeVerifyServlet")
public class CodeVerifyServlet extends HttpServlet {
	
 
    
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeVerifyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String phoneNo = request.getParameter("phone_no");
	     String verifyCode=request.getParameter("verify_code");

	     if(phoneNo==null||verifyCode==null){
	    	 return ;
	     }

		 Jedis jedis =new Jedis("192.168.70.110",6379);

		 String expectedCode = jedis.get(phoneNo);
		 jedis.close();
		 if(verifyCode.equals(expectedCode)){
			 response.getWriter().print(true);
			 return ;
          }
	     
		 
	}

}
