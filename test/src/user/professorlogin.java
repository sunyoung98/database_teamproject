package user;



import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;





public class professorlogin {

	private Connection conn; //占쏙옙占쏙옙占싶븝옙占싱쏙옙占쏙옙 占쏙옙占쏙옙占싹깍옙 占쏙옙占쏙옙 占쏙옙체

	private PreparedStatement pstmt;

	private ResultSet rs; //占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙 占쌍댐옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙娩占�.

	

//mysql 처占쏙옙占싸븝옙

	public professorlogin() {

		// 占쏙옙占쏙옙占쌘몌옙 占쏙옙占쏙옙占쏙옙娩占�.

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



//占싸깍옙占쏙옙 처占쏙옙占싸븝옙

	public int login(String userID, String userPassword) {

		String SQL = "SELECT Password FROM professor WHERE ID = ?";

		try {

			pstmt = conn.prepareStatement(SQL);

			pstmt.setString(1, userID);

			rs = pstmt.executeQuery();

			if(rs.next()) {

				if(rs.getString(1).equals(userPassword)) {

					return 1; //占싸깍옙占쏙옙 占쏙옙占쏙옙					

				}

			}else { 

				return -1; //占쏙옙橘占싫� 占쏙옙占쏙옙치

			}

			return 0; //占쏙옙占싱듸옙占쏙옙占�

		}catch(Exception e) {

			e.printStackTrace();

		}

		return -2; //占쏙옙占쏙옙占싶븝옙占싱쏙옙 占쏙옙占쏙옙

	}

 

}