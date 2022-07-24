//TODO: unify the display unreplied post endpoint

package com.google.sps.servlets;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet responsible for displaying posts. */
@WebServlet("/display-posts-unreplied")
public class DisplayUnrepliedPostsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    //get id from url and cookie
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    String idCookie = "null";
    String idUrl = "null";
    List<List<String>> posts = new ArrayList<>();

    
    idUrl = request.getHeader("Url-Id");
    System.out.println("newId:"+idUrl);
    if (idUrl != null) {
        String base64CredentialId = idUrl.substring("Basic".length()).trim();
        byte[] credDecodedId = Base64.getDecoder().decode(base64CredentialId);
        idUrl = new String(credDecodedId, StandardCharsets.UTF_8);
    }
    System.out.println("newIdUrl:"+idUrl);
    
    final String authorizationCookie = request.getHeader("Authorization");
    if (authorizationCookie != null && authorizationCookie.toLowerCase().startsWith("basic")) {
        String base64CredentialCookie = authorizationCookie.substring("Basic".length()).trim();
        byte[] credDecodedCookie = Base64.getDecoder().decode(base64CredentialCookie);
        idCookie = new String(credDecodedCookie, StandardCharsets.UTF_8);
    }

    System.out.println("finding:"+idCookie);
    
    if(idUrl.equals(idCookie)){
        Query<Entity> query = Query.newEntityQueryBuilder().setKind("Post")
            .setFilter(
                PropertyFilter.eq("user_id", idCookie)
            )
            .build();
        QueryResults<Entity> results = datastore.run(query);
        System.out.println("result:"+results);

        // Add the post content string into the 'posts' arraylist 
        while (results.hasNext()) {
            List<String> onePost = new ArrayList<>(); 
            Entity entity = results.next();
            String postContent = entity.getString("content");
            String ownerId = entity.getString("user_id");
            String postId = ""+entity.getKey().getId()+"";
            onePost.add(postContent);
            onePost.add(ownerId);
            onePost.add(postId);
            posts.add(onePost);
            
        }
    }

    

    // Convert the 'posts' arraylist into json and send as response
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(posts));
    }
}