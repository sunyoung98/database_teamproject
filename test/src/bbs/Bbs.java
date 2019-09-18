package bbs;



public class Bbs {

	

	private int bbsID;
	private String bbsTitle;
	private String userID;
	private String bbsDate;
	private String bbsContent;
	private int bbsAvailable;
	
	private String CName;
	private String CTime;
	private int CNum;
	
	private String fileName;
	private String fileRealName;
	
	private String Pname;
	private String CRoom;
	
	private String sId;
	private String major;
	private String grade;
	private String gender;
	private String foreign;
	private String Email;
	private String StuId;
	private String Phone;
	private String[] receiveMaillist;
	public String getCName() {
		return CName;
	}
	public void setCName(String cname) {
		CName=cname;
	}
	public String getCTime() {
		return CTime;
	}
	public void setCTime(String ctime) {
		CTime=ctime;
	}
	public int getCNum() {
		return CNum;
	}
	public void setCNum(int cNum) {
		CNum = cNum;
	}
	

	
	public int getBbsID() {
		return bbsID;
	}
	public void setBbsID(int bbsID) {
		this.bbsID = bbsID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getBbsTitle() {
		return bbsTitle;
	}
	public void setBbsTitle(String bbsTitle) {
		this.bbsTitle = bbsTitle;
	}
	public String getBbsDate() {
		return bbsDate;
	}
	public void setBbsDate(String bbsDate) {
		this.bbsDate = bbsDate;
	}
	public String getBbsContent() {
		return bbsContent;
	}
	public void setBbsContent(String bbsContent) {
		this.bbsContent = bbsContent;
	}
	public int getBbsAvailable() {
		return bbsAvailable;
	}
	public void setBbsAvailable(int bbsAvailable) {
		this.bbsAvailable = bbsAvailable;
	}
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileRealName() {
		return fileRealName;
	}
	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}

	
	public String getPname() {
		return Pname;
	}
	public void setPname(String pname) {
		Pname=pname;
	}
	public String getCRoom() {
		return CRoom;
	}
	public void setCRoom(String cRoom) {
		CRoom = cRoom;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getForeign() {
		return foreign;
	}
	public void setForeign(String foreign) {
		this.foreign = foreign;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getsId() {
		return sId;
	}
	public void setsId(String sId) {
		this.sId = sId;
	}
	public String getStuId() {
		return StuId;
	}
	public void setStuId(String stuId) {
		StuId = stuId;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String[] getReceiveMaillist() {
		return receiveMaillist;
	}
	public void setReceiveMaillist(String[] receiveMaillist) {
		this.receiveMaillist = receiveMaillist;
	}
	
}
