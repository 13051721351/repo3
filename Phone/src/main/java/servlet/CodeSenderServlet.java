package servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class VerifiCodeServlet
 */
@WebServlet("/CodeSenderServlet")
public class CodeSenderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeSenderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
     
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.先去页面拿数据
		 String phoneNo=  request.getParameter("phone_no");
		 //2.判断是否为空      判断手机号是否真实存在
		 if(phoneNo==null){
			 return ;
		 }
		 //3.生成验证码
		 String verifyCode=genCode(6);
		 
		 //4.存储验证码
		 Jedis jedis =new Jedis("192.168.70.110",6379);
		 jedis.setex(phoneNo,60, verifyCode);
		 jedis.close();

		 //5.存储次数    2020-03-21 00:00:00
		jedis.hincrBy("phonecount",phoneNo,1);

		//6.取值
		int phonecount = Integer.parseInt(jedis.hget("phonecount", phoneNo));

		if(phonecount>3){
			response.getWriter().print("limit");
		}else{
			response.getWriter().print(true);
		}


	} 
	
	
	private   String genCode(int len){
		 String code="";

		 for (int i = 0; i < len; i++) {
		     int rand=  new Random().nextInt(10);
		     code+=rand;
		 }
		 
		return code;
	}
	
	
 
}
