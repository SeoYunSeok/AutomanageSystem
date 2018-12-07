package test;

import java.awt.TextArea;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class DBmanage {
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public DBmanage() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/hansung", "root", "dkdlvkt12");
			System.out.println("DB���� ����");
		} catch (Exception e) {
			System.out.println("DB �������" + e.getMessage());
		}
	}

	public void search(TextArea textArea) {
		// TODO Auto-generated method stub
		try {
			String SQL = "SELECT * FROM manage";
			pstmt = con.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				textArea.append(rs.getString("place") + "\n");
				textArea.append(rs.getString("ip") + "\n");
			}
			textArea.append("--------------------------\n");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("����" + e.getMessage());
		}
	}
	
	public int insert(String place, String ip) {
		// TODO Auto-generated method stub
		try {
			String SQL = "INSERT INTO manage VALUES (?, ?)";
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, place);
			pstmt.setString(2, ip);
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("����" + e.getMessage());
			return 0;
		}
	}
	public void ipsearch(String place, int air, int chkair, int i) {
		String temp = null;
		URL url = null;
		HttpURLConnection urlConnection = null;
		String sUrl = null;
		// TODO Auto-generated method stub
		try {
			String SQL = "SELECT * FROM manage WHERE place = ?";
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1,place);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				temp=rs.getString("ip");
			}
			if (i==0) {
				if(chkair==700) {
					sUrl = "http://"+temp+"/lighton";
				}
				if(chkair==900) {
					sUrl = "http://"+temp+"/qulight";
				}
				if(chkair==1500) {
					sUrl = "http://"+temp+"/perlight";
				}
				System.out.println("����Ʈ��!111");
			}else if(i==1) {
				sUrl = "http://"+temp+"/duston";
				System.out.println("��������");
			}else if(i==2) {
				if(air==100 && chkair==100) {
					sUrl = "http://"+temp+"/airhigh";
					System.out.println("����ǳ��");
				}
				if(air < chkair) {
					sUrl = "http://"+temp+"/airmid";
					System.out.println("�µ��� �ʹ� ���ƿ�");
				}
				else if(air>chkair) {
					if(checkpeople(place)<=20) {
						sUrl = "http://"+temp+"/airmid";
						System.out.println("�߰�ǳ��");
					}
					else if(checkpeople(place)>20) {
						sUrl = "http://"+temp+"/airhigh";
						System.out.println("����ǳ��");
					}
				}
			}else if(i==3) {
				sUrl = "http://"+temp+"/cleanon";
				System.out.println("����");
			}
			url = new URL(sUrl);
			urlConnection = (HttpURLConnection)url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("����ipsearch�κ�" + e.getMessage());			
		}
	}
	
	public void turnoff(String place) {
		String temp = null;
		URL url = null;
		HttpURLConnection urlConnection = null;
		String sUrl = null;
		// TODO Auto-generated method stub
		try {
			String SQL = "SELECT * FROM manage WHERE place = ?";
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1,place);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				temp=rs.getString("ip");
			}
				sUrl = "http://"+temp+"/turnoff";
			System.out.println("turn off ����");
			url = new URL(sUrl);
			urlConnection = (HttpURLConnection)url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "turnoff����"+e.getMessage(), "���� �޽���", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public int checkpeople(String place) {
		int temp = 0;
		// TODO Auto-generated method stub
		try {
			String SQL = "SELECT * FROM schedule WHERE place = ?";
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1,place);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				temp=rs.getInt("people");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("����check�κ�" + e.getMessage());
		}
		return temp;
	}
}
