package mail;
import java.net.PasswordAuthentication;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import mail.EmailDTO;

public class EmailService {
	public void mailSender(EmailDTO dto) throws Exception{
		 String host="smtp.gmail.com";
		 String username="tjsdud9151";
		 String password="aa80237320!";
		 int port=587;
		 String sender=dto.getSendereMail();
		 String recipient=dto.getReceiveMail();
		 String subject=dto.getSubject();
		 String body=dto.getMessage();
		 String senderName=dto.getSenderName();
		 Properties props=System.getProperties();
		 props.put("mail.smtp.host", host);
		 props.put("mail.smtp.port", port);
		 props.put("mail.transport.protocol", "smtp");
		 props.put("mail.smtp.auth", "true");
		 props.put("mail.smtp.starttls.enable", "true");
		 Session session=Session.getDefaultInstance(props,new javax.mail.Authenticator(){
			 String un=username;
			 String pw=password;
			 public javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(un,pw); 
			 }
		 });
		 session.setDebug(true);
		 Message mimeMessage=new MimeMessage(session);
		 mimeMessage.addFrom(new InternetAddress[] {new InternetAddress(sender,senderName)});
				 mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
				 mimeMessage.setRecipient(Message.RecipientType.CC, new InternetAddress(recipient));
				 mimeMessage.setSubject(subject);
				 mimeMessage.setText(body);
				 Transport.send(mimeMessage);
				
	 }

	   

}
