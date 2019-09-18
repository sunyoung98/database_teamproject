package mail;

import java.util.ArrayList;

import bbs.Bbs;

public class EmailDTO {
	private String senderName;
	private String sendereMail;
	private String receiveMail;
	private String subject;
	private String message;
	
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSendereMail() {
		return sendereMail;
	}
	public void setSendereMail(String sendereMail) {
		this.sendereMail = sendereMail;
	}
	public String getReceiveMail() {
		return receiveMail;
	}
	public void setReceiveMail(String receiveMail) {
		this.receiveMail = receiveMail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String toString() {
		return "EmailDTo [senderName="+senderName+", senderMail="+",subject="+subject+",message="+message+"]";
	}

}
