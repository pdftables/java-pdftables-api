import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PDFTablesExample {

    public static void main(String[] args) throws Exception {
        if (args.length != 1)  {
            System.out.println("File path of PDF not given");
            System.exit(1);
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost("{{ .FullURL }}" +
                    "/api?key={{ .DisplayAPIKey }}&format=xml");

            FileBody bin = new FileBody(new File(args[0]));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("f", bin)
                    .build();
            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println(EntityUtils.toString(resEntity));
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
