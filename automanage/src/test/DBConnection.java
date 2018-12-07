package test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.TimerTask;

import javax.swing.JOptionPane;



public class DBConnection extends TimerTask {
		int min; int tim; String day; String place; String noplace; int cut;
		int air[] = new int[4];
		int checkair[][] = new int [4][4];
		private Connection con;
		private PreparedStatement pstmt;
		private ResultSet rs;
		DBmanage dBmanage = new DBmanage();
		public DBConnection() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con=DriverManager.getConnection("jdbc:mysql://localhost:3307/hansung","���̵�","��й�ȣ");
				System.out.println("DB���� ����");
			}
			catch(Exception e) {
				System.out.println("DB �������"+e.getMessage());
			}
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar calendar = Calendar.getInstance( );
			if(calendar.get(Calendar.DAY_OF_WEEK)==1) {
				day = "��";
			}else if(calendar.get(Calendar.DAY_OF_WEEK)==2) {
				day = "��";
			}else if(calendar.get(Calendar.DAY_OF_WEEK)==3) {
				day = "ȭ";
			}else if(calendar.get(Calendar.DAY_OF_WEEK)==4) {
				day = "��";
			}else if(calendar.get(Calendar.DAY_OF_WEEK)==5) {
				day = "��";
			}else if(calendar.get(Calendar.DAY_OF_WEEK)==6) {
				day = "��";
			}else if(calendar.get(Calendar.DAY_OF_WEEK)==7) {
				day = "��";
			}
			if(calendar.get(Calendar.HOUR_OF_DAY)==8) {
				tim = 0;
			}else if(calendar.get(Calendar.HOUR_OF_DAY)==9) {
				tim = 1;
			}else if(calendar.get(Calendar.HOUR_OF_DAY)==10) {
				tim = 2;
			}else if(calendar.get(Calendar.HOUR_OF_DAY)==11) {
				tim = 3;
			}else if(calendar.get(Calendar.HOUR_OF_DAY)==12) {
				tim = 4;
			}else if(calendar.get(Calendar.HOUR_OF_DAY)==13) {
				tim = 5;
			}else if(calendar.get(Calendar.HOUR_OF_DAY)==14) {
				tim = 6;
			}else if(calendar.get(Calendar.HOUR_OF_DAY)==15) {
				tim = 7;
			}else if(calendar.get(Calendar.HOUR_OF_DAY)==16) {
				tim = 8;
			}else if(calendar.get(Calendar.HOUR_OF_DAY)==17) {
				tim = 9;
			}
			min = calendar.get(Calendar.MINUTE);
			try {
				String SQL = "SELECT * FROM schedule WHERE day = ?";
				pstmt = con.prepareStatement(SQL);
				pstmt.setString(1,day);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					place=""; noplace = "";
					String time = rs.getString("time");
					int change = Integer.parseInt(time.substring(1, 2));
					int change2 = Integer.parseInt(time.substring(time.length()-2, time.length()-1));
					if(min==30) {
						if((tim+1)==change||(tim+1)==change+1||(tim+1)==change2) {
							place = rs.getString("place");	
						}
						else {
							noplace = rs.getString("place");
							}
					}
					else {
						if((tim)==change||(tim)==change+1||(tim)==change2) {
							place = rs.getString("place");
							System.out.println(place);
						}
					}
						
					for(int i=0; i<4; i++) { //�޾ƿ��� ���� �ʱ�ȭ �� ���ְ�
						air[i]=0;
					}
					
					if(!place.equals("")) {
					if(place.equals("���а� B105")) { //�޾ƿͼ� ������ �ְ�
						for(int i=0; i<4; i++) {	
						air[i] = message(i+1);
						}
					}
					else if(place.equals("Ž���� B101")) {
						for(int i=0; i<4; i++) {	
							air[i] = message(i+5);
							}
					}
					
					if(screen.check==1) {
						checkair[0][0] = 900; //���� 1000~700���� �ʹ����Ƶ� �ȵ�
						checkair[0][1] = 80;//�̼����� 80�̻��̸� ����
						checkair[0][2] = 25; // �б� ���� �µ� ��� �����ֱ� 
						checkair[0][3] = 40;//40�� �����̻��̸� �۵�
					}
					else if(screen.check==2){
						checkair[0][0] = 1500; //���� 2000~800���� �ʹ����Ƶ� �ȵ�
						checkair[0][1] = 80;//�̼����� 80�̻��̸� ����
						checkair[0][2] = 23; // �б� ���� �µ� ��� �����ֱ� 
						checkair[0][3] = 50;//60�� �����̻��̸� �۵�
					}else if(screen.check==3){
						checkair[0][0] = 900; //���� 1000~700���� �ʹ����Ƶ� �ȵ�
						checkair[0][1] = 80;//�̼����� 80�̻��̸� ����
						checkair[0][2] = 25; // �б� ���� �µ� ��� �����ֱ� 
						checkair[0][3] = 40;//40�� �����̻��̸� �۵�
					}
					//���� �µ��� ���غ�
					if(700>air[0] || air[0]>checkair[0][0]) {
						dBmanage.ipsearch(place, air[0],checkair[0][0], 0);
					}
					if(air[1]>checkair[0][1]) {
						dBmanage.ipsearch(place, air[1],checkair[0][1] ,1);
					}
					if(air[2] != checkair[0][2]) {
						dBmanage.ipsearch(place, air[2],checkair[0][2],2);
					}
					if(air[3]>checkair[0][3]) {
						dBmanage.ipsearch(place, air[3],checkair[0][3],3);
					}
				}
					else if(!noplace.equals("")) {
						dBmanage.turnoff(noplace);
					}
				
				}	
			}catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, "����Ŀ�ؼǿ���"+e.getMessage(), "���� �޽���", JOptionPane.WARNING_MESSAGE);
			}{
				
			}
		}
		public int message(int place) throws Exception {
			URL url = null;
			String target = "https://api.thingspeak.com/channels/544668/fields/"+place+".json?results=1";
			HttpURLConnection con = (HttpURLConnection) new URL(target).openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while((temp = br.readLine()) != null) {
				temp = temp.substring(temp.indexOf("entry_id"));
				temp = temp.substring(temp.indexOf("field"));
				temp = temp.substring(temp.indexOf(":")+2,temp.length()-4);
				cut = Integer.parseInt(temp);
			}
			con.getInputStream();
			br.close();
			return cut;
		}
}
