package bbs;



import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.util.ArrayList;



public class BbsDAO {

		private Connection conn; // connection:db占쏙옙占쏙옙占쏙옙占싹곤옙 占쏙옙占쌍댐옙 占쏙옙체

		//private PreparedStatement pstmt;

		private ResultSet rs;



		public BbsDAO() {


			try {

				Class.forName("com.mysql.cj.jdbc.Driver");	
				String url="jdbc:mysql://localhost:3306/jdbc_test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
				
				String id="root";
				String pw="aa8020";
				
				conn=DriverManager.getConnection(url,id,pw);

			} catch (Exception e) {

				e.printStackTrace();


			}

		}

		

		//占쏙옙占쏙옙占쏙옙 占시곤옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쌉쇽옙

		public String getDate() { 

			String SQL = "SELECT NOW()";

			try {

				PreparedStatement pstmt = conn.prepareStatement(SQL);

				rs = pstmt.executeQuery();

				if(rs.next()) {

					return rs.getString(1);

				}

			} catch (Exception e) {

				e.printStackTrace();

			}

			return ""; //占쏙옙占쏙옙占싶븝옙占싱쏙옙 占쏙옙占쏙옙

		}

		public int getNext() { 

			String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";

			try {

				PreparedStatement pstmt = conn.prepareStatement(SQL);

				rs = pstmt.executeQuery();

				if(rs.next()) {

					return rs.getInt(1) + 1;

				}

				return 1;//첫 占쏙옙째 占쌉시뱄옙占쏙옙 占쏙옙占�

			} catch (Exception e) {

				e.printStackTrace();

			}

			return -1; //占쏙옙占쏙옙占싶븝옙占싱쏙옙 占쏙옙占쏙옙

		}

			


			public int write(String bbsTitle, String userID, String bbsContent,int CNum,String fileName, String fileRealName) { 

				String SQL = "INSERT INTO BBS VALUES(?, ?, ?, ?, ?, ?,?,?,?)";

				try {

				
					PreparedStatement pstmt = conn.prepareStatement(SQL);


					pstmt.setInt(1, getNext());
					pstmt.setString(2, bbsTitle);

					pstmt.setString(3, userID);
					pstmt.setString(4, getDate());

					pstmt.setString(5, bbsContent);

					pstmt.setInt(6,1);
					pstmt.setInt(7, CNum);
					pstmt.setString(8,fileName);
					pstmt.setString(9,fileRealName);
					
					return pstmt.executeUpdate();

				} catch (Exception e) {

					e.printStackTrace();

				}

				return -1; //占쏙옙占쏙옙占싶븝옙占싱쏙옙 占쏙옙占쏙옙

			}
			
			public ArrayList<Bbs> getSubjects(String ID){
				String SQL = "SELECT * from section s, course c,enrollment e \r\n" + 
						"where s.CNum=c.CNum and e.CNum=c.CNum and e.studentID=?";

				ArrayList<Bbs> list = new ArrayList<Bbs>();

				try {

					PreparedStatement pstmt = conn .prepareStatement(SQL);
					pstmt.setInt(1, Integer.parseInt(ID));

					rs = pstmt.executeQuery();

					while (rs.next()) {

						Bbs bs=new Bbs();
						bs.setCNum(rs.getInt(3));
						bs.setCName(rs.getString(6));
						bs.setCTime(rs.getString(2));
						list.add(bs);

					}

				} catch (Exception e) {

					e.printStackTrace();

				}

				return list; 

			}
			
			
			public ArrayList<Bbs> getProSubjects(String userID){
				String SQL = "Select time, cname, s.cnum from section s, course c where s.CNum=c.CNum AND C.PID=?";
				
						

				ArrayList<Bbs> list = new ArrayList<Bbs>();

				try {

					PreparedStatement pstmt = conn.prepareStatement(SQL);
					pstmt.setString(1, userID);

					rs = pstmt.executeQuery();

					while (rs.next()) {

						Bbs bs=new Bbs();
						bs.setCTime(rs.getString(1));
						bs.setCName(rs.getString(2));
						bs.setCNum(rs.getInt(3));
						list.add(bs);

					}

				} catch (Exception e) {

					e.printStackTrace();

				}

				return list; 

			}
			public ArrayList<Bbs> getsubjectName(int Cnum){
				String SQL = "SELECT CName, Time from course c, section s where c.CNum=? And s.CNum=?";
				ArrayList<Bbs> list = new ArrayList<Bbs>();
				try {
					PreparedStatement pstmt = conn.prepareStatement(SQL);
					pstmt.setInt(1, Cnum);
					pstmt.setInt(2, Cnum);
					rs = pstmt.executeQuery();
					while (rs.next()) {
					Bbs bs=new Bbs();
					bs.setCName(rs.getString(1));
					bs.setCTime(rs.getString(2));
					list.add(bs);
					}
					Bbs bs=new Bbs();
					bs.setCName("과목 선택");
					bs.setCTime(" ");
					list.add(bs);
				} catch (Exception e) {
					e.printStackTrace();
					Bbs bs=new Bbs();
					bs.setCName("과목 선택");
					bs.setCTime(" ");
					list.add(bs);

				}

				
				return list;

			}
			public ArrayList<Bbs> getstudentArray(String userID, int Cnum){
				String SQL="select distinct s.Id, s.major, s.Grade, s.Gender, s.Foreign, s.Email\r\n" + 
		                  "from enrollment e, course c, student s, professor p\r\n" + 
		                  "where p.id=c.pid and c.cnum=e.cnum and e.StudentID =s.Id and p.id=? and c.cnum=?;";
				ArrayList<Bbs> list = new ArrayList<Bbs>();
				try {
					PreparedStatement pstmt = conn.prepareStatement(SQL);
					pstmt.setString(1, userID);
					pstmt.setInt(2, Cnum);
					rs=pstmt.executeQuery();
					while(rs.next()){
						Bbs bs=new Bbs();
						bs.setsId(rs.getString(1));
						bs.setMajor(rs.getString(2));
						bs.setGrade(rs.getString(3));
						bs.setGender(rs.getString(4));
						bs.setForeign(rs.getString(5));
						bs.setEmail(rs.getString(6));
						list.add(bs);
					}
					}catch (Exception e) {
						Bbs bs=new Bbs();
						bs.setsId(" ");
						bs.setMajor(" ");
						bs.setGrade(" ");
						bs.setGender(" ");
						bs.setForeign(" ");
						bs.setEmail(" ");
						list.add(bs);
				}

				
				return list;

			}
			public ArrayList<Bbs> getList(int pageNumber,int CNum){ 

				String SQL = "select distinct * from bbs b, section s, course c where s.CNum=c.CNum  AND b.bbsCNum=s.Cnum AND bbsCNum=? having bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";

				ArrayList<Bbs> list = new ArrayList<Bbs>();

				try {

					PreparedStatement pstmt = conn.prepareStatement(SQL);
					//int pid=Integer.parseInt(PID);
					//pstmt.setInt(1, pid);
					pstmt.setInt(1, CNum);

					rs = pstmt.executeQuery();

					while (rs.next()) {

						Bbs bbs = new Bbs();

						bbs.setBbsID(rs.getInt(1));

						bbs.setBbsTitle(rs.getString(2));

						bbs.setUserID(rs.getString(3));

						bbs.setBbsDate(rs.getString(4));

						bbs.setBbsContent(rs.getString(5));

						bbs.setBbsAvailable(rs.getInt(6));
						bbs.setCNum(rs.getInt(7));
						
						bbs.setCTime(rs.getString(9));
						bbs.setCName(rs.getString(13));

						list.add(bbs);

					}

				} catch (Exception e) {

					e.printStackTrace();

				}

				return list; 

			}
			public ArrayList<Bbs> enrollStudent(String userId, int CNum) {
	            String SQL="select distinct s.phone, s.Id\r\n" + 
	                  "from enrollment e, course c, student s, professor p\r\n" + 
	                  "where p.id=c.pid and c.cnum=e.cnum and e.StudentID =s.Id and p.id=? and c.cnum=?;";
	            ArrayList<Bbs> list = new ArrayList<Bbs>();

	            try {

	               PreparedStatement pstmt = conn.prepareStatement(SQL);
	               pstmt.setString(1, userId);
	               pstmt.setInt(2, CNum);
	               rs = pstmt.executeQuery();

	               while (rs.next()) {

	                  Bbs bbs = new Bbs();
	                  bbs.setPhone(rs.getString(1));
	                  bbs.setStuId(rs.getString(2));
	            
	                  list.add(bbs);

	               }

	            } catch (Exception e) {

	               e.printStackTrace();

	            }

	            return list;
	         }
			public Bbs getBbs(int bbsID) {

				String SQL = "SELECT * FROM BBS WHERE bbsID = ?";

				try {

					PreparedStatement pstmt = conn.prepareStatement(SQL);

					pstmt.setInt(1, bbsID);

					rs = pstmt.executeQuery();

					if (rs.next()) {

						Bbs bbs = new Bbs();

						bbs.setBbsID(rs.getInt(1));

						bbs.setBbsTitle(rs.getString(2));

						bbs.setUserID(rs.getString(3));

						bbs.setBbsDate(rs.getString(4));

						bbs.setBbsContent(rs.getString(5));

						bbs.setBbsAvailable(rs.getInt(6));
						bbs.setFileRealName(rs.getString(8));
						bbs.setFileName(rs.getString(9));


						return bbs;

					}

				} catch (Exception e) {

					e.printStackTrace();

				}

				return null;



			}
			//占쏙옙占쏙옙 占쌉쇽옙
			public void enrollSubject(String ID, int CNum) {
				String sql="INSERT enrollment VALUES(?,?)";
				try {
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(2,ID);
					pstmt.setInt(1, CNum);
					pstmt.executeUpdate();
				}
				catch (Exception e) {

					e.printStackTrace();

				}
				
			}
			
			public void delectSubject(String ID, int CNum) {
				String sql="DELETE FROM enrollment\r\n" + 
						"WHERE CNUM=? and STUDENTID=?\r\n";
						
				try {
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(2,ID);
					pstmt.setInt(1, CNum);
					pstmt.executeUpdate();
				}
				catch (Exception e) {

					e.printStackTrace();

				}
				
			}
			

			public int delete(int bbsID) {

				String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";

				try {

					PreparedStatement pstmt = conn.prepareStatement(SQL);   

					pstmt.setInt(1, bbsID);

					return pstmt.executeUpdate();



				} catch (Exception e) {

					e.printStackTrace();

				}

				return -1; // 占쏙옙占쏙옙占싶븝옙占싱쏙옙 占쏙옙占쏙옙

			}
			public ArrayList<Bbs> enrollSubject() {
				String SQL="SELECT name, CName,Sem,Time,Room,c.CNum\r\n" + 
						"From Course as c, professor as p, section as s\r\n" + 
						"Where c. PId=p.id and c.CNum=s.CNum";
				ArrayList<Bbs> list = new ArrayList<Bbs>();

				try {

					PreparedStatement pstmt = conn.prepareStatement(SQL);
					
					rs = pstmt.executeQuery();

					while (rs.next()) {

						Bbs bbs = new Bbs();

						bbs.setPname(rs.getString(1));
						bbs.setCName(rs.getString(2));
						bbs.setCTime(rs.getString(4));
						bbs.setCRoom(rs.getString(5));
						bbs.setCNum(rs.getInt(6));
						list.add(bbs);

					}

				} catch (Exception e) {

					e.printStackTrace();

				}

				return list;
			}

		}
