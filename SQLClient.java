import java.sql.*;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
public class SQLClient(){
	private AmazonS3Client s3Client;
	private Connection con;
	private String hostname ="senior-projectdatabase.cauuuijr4uq1.us-east-2.rds.amazonaws.com"'
	private String port = "3306";
	private String adminUser = "adminSP";
	private String adminPassword = "2022SeniorP";
	private String dbName = "SeniorProj";
	
	public SQLClient(MY_ACCESS_KEY_ID, MY_SECRET_KEY){
		s3Client = new AmazonS3Client( new BasicAWSCredentials( MY_ACCESS_KEY_ID, MY_SECRET_KEY ) );
		boolean connected = connectDB()
		if(!connected){
			System.out.err("Couldn't connect to DB");
		}
	}
	private static void connectDB()
	{
		try{		
			Class.forName("com.mysql.jdbc.Driver"); 
			String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName; 
			con = DriverManager.getConnection(jdbcUrl,adminUser,adminPassword);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public static void uploadImage(String bucketName, String pictureName,String filePath,String userName){
		//String bucketName = "bucketName"; 
		//String pictureName = "images/{userName}/profile.jpg"; 
		//String filepath = "phone_localPath/profile.jpg"; 
		
		PutObjectRequest por = new PutObjectRequest( bucketName, pictureName,new java.io.File( filePath) ); 
		s3Client.putObject( por );
		
		//Update SQL Table
		//S3 url 
		String url = bucketName+"/"+pictureName;
		
		String statement = "UPDATE USER SET image=? where username=?"; 
		PreparedStatement ps = con.prepareStatement(statement); 
		ps.setString(1, url); 
		ps.setString(2, userName); 
		ps.executeUpdate();	
	}
	public static byte[] getImage(String userName){
		String query = "SELECT image from USER where username = ?";
		PreparedStatement ps = con.prepareStatement(query); 
		ps.setString(1, userName);
		String url = ps.executeQuery().getString("image");
		int index = url.indexOf("/");
		GetObjectRequest gor = new GetObjectRequest(url.substring(0,index),url.substring(index+1))
		InputStream obj = s3Client.getObject(gor).getObjectContent();
		byte[] byteArray = IOUtils.toByteArray(obj);
		return byteArray
	}
	
}