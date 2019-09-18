package mail;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/email_servlet/*")
public class EmailController extends HttpServlet{
	private static final long serialVersionUID=1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String senderName="±³¼ö";
		String senderMail="tjsdud9151@gmail.com";
		String[] receiveMaillist=request.getParameterValues("email");
		String subject=request.getParameter("subject");
		String message=request.getParameter("message");
		try {
			for(int i=0; i<receiveMaillist.length; i++) {
		EmailDTO dto=new EmailDTO();
		dto.setSenderName(senderName);
		dto.setSendereMail(senderMail);
		dto.setReceiveMail(receiveMaillist[i]);
		dto.setSubject(subject);
		dto.setMessage(message);
		EmailService service=new EmailService();
		service.mailSender(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath()+"/mail.jsp");
	}
		
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	public EmailController() {
		super();
	}
}
