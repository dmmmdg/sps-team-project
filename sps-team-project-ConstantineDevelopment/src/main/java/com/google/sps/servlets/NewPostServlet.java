package com.google.sps.servlets;
import java.util.Base64;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/new-post")
public class NewPostServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the value entered in the form.
    String userId = Jsoup.clean(request.getParameter("userId"), Whitelist.none());
    String content = Jsoup.clean(request.getParameter("content"), Whitelist.none());
    byte[] decodedFirst = Base64.getDecoder().decode(userId);
    String decodedStringFirst = new String(decodedFirst);
    byte[] decodedSecond = Base64.getDecoder().decode(decodedStringFirst);
    userId = new String(decodedSecond);


    // Print the value so you can see it in the server logs.
    System.out.println("New Id: " + userId);
    System.out.println("New content: " + content);
    
    long timestamp = System.currentTimeMillis();

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("Post");
     FullEntity taskEntity =
        Entity.newBuilder(keyFactory.newKey())
            .set("user_id", userId)
            .set("content", content)
            .set("timestamp",timestamp )
            .build();
    datastore.put(taskEntity);
  }
}