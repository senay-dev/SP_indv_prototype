# Storing a Profile Photo in S3; Storing photo's S3 URL in RDS
## Instantiate Class
`SQLClient sql = new SQLClient(aws_id,aws_pwd);`

Database will connect to AWS and to the RDS via java's JDBC

## Upload image to S3 on Android Studio
`sql.uploadImage(bucketName,pictureName,localFilePath, currUserName);`
#### This will upload currUserName's photo to S3,
#### Generate pre-signed URL,
#### and update their image column in the User table in RDS with the S3 URL.

## Get a user's profile image
sql.getImage(currUserName);
#### Returns the image as byte[]

### User table structure


| Field    | Type        | Null | Key | Default | Extra          |
|----------|-------------|------|-----|---------|----------------|
| user_id  | int         | NO   | PRI | NULL    | auto_increment |
| username | varchar(45) | NO   |     | NULL    |                |
| password | varchar(45) | NO   |     | NULL    |                |
| image    | text        | YES  |     | NULL    |                |

