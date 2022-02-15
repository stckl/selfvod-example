import java.lang.Exception;
import java.io.*;
import java.nio.file.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

public class UploadObjectExample {

	public static void main(String[] args) {
        try {
            URL url = new URL("...signed URL...");
            File file = new File("...Videofile to upload...");

            uploadFile(url, file);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
	}

    public static void uploadFile(URL url, File file) {
        try {
            Path fpath = file.toPath();
            String contentType = Files.probeContentType(fpath);
            byte[] fbyte = Files.readAllBytes(fpath);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",contentType); /* contenttype of file to upload */
            connection.setRequestMethod("PUT");

            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            request.write(fbyte);
            request.flush();

            InputStream stream = connection.getInputStream();
    
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(stream));
        
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
        
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();
        
            String response = stringBuilder.toString();
            connection.disconnect();

            System.out.print(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}