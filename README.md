# Storing a Profile Photo in S3; Storing link in RDS


## Step 1: Upload image to S3 on Android Studio
Create Amazon S3 client
`AmazonS3Client s3Client =   new AmazonS3Client( new BasicAWSCredentials( MY_ACCESS_KEY_ID, MY_SECRET_KEY ) );`

Put the image object into the Amazon S3bucket.
`String bucketName = "bucketName";`
`String pictureName = "images/{userName}/profile.jpg";`
`String filepath = "phone_localPath/profile.jpg";`
`PutObjectRequest por = new PutObjectRequest( bucketName,                          pictureName,new java.io.File( filePath) );`
`s3Client.putObject( por );`
## Update S3 URL to the User's image column

### User table structure


| Field    | Type        | Null | Key | Default | Extra          |
|----------|-------------|------|-----|---------|----------------|
| user_id  | int         | NO   | PRI | NULL    | auto_increment |
| username | varchar(45) | NO   |     | NULL    |                |
| password | varchar(45) | NO   |     | NULL    |                |
| image    | text        | YES  |     | NULL    |                |
### Generate pre-signed URL
Create an override content type
`ResponseHeaderOverrides override = new ResponseHeaderOverrides();`
`override.setContentType( "image/jpeg" );`
Generate URL
`GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( bucketName,                                     pictureName);`

Allow the URL to be valid for an hour
`urlRequest.setExpiration(new Date( System.currentTimeMillis() + 3600000 ) );`
`urlRequest.setResponseHeaders( override );`

Get the pre-signed URL
`String url = s3Client.generatePresignedUrl( urlRequest ).toURI().toString();`

### Connect to RDS DB
`Class.forName("com.mysql.jdbc.Driver");`
 `String dbName = System.getProperty("RDS_DB_NAME");` 
 `String userName = System.getProperty("RDS_USERNAME");` 
 `String password = System.getProperty("RDS_PASSWORD");` 
 `String hostname = System.getProperty("RDS_HOSTNAME");` 
 `String port = System.getProperty("RDS_PORT");` 
 
 `String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;` 
 `Connection con = DriverManager.getConnection(jdbcUrl);`
### Update image URL column for the user in User table
`String statement = "UPDATE USER SET image=? where username=?";`
`PreparedStatement ps = conn.prepareStatement(statement);`
`ps.setString(1,  url);` 
`ps.setString(2,  userName);`
`ps.executeUpdate();`



