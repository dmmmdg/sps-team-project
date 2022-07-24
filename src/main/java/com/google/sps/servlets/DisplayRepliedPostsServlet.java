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

/** Servlet responsible for displaying posts. */
@WebServlet("/display-posts-replied")
public class DisplayRepliedPostsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idOwner = request.getHeader("ownerId");
        System.out.println("RepliedOwner:"+idOwner);
        if (idOwner != null) {
            String base64CredentialId = idOwner.substring("Basic".length()).trim();
            byte[] credDecodedId = Base64.getDecoder().decode(base64CredentialId);
            idOwner = new String(credDecodedId, StandardCharsets.UTF_8);
        }

        // Initialize Datastore 
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        Query<Entity> query = Query.newEntityQueryBuilder()
            .setKind("RepliedPost")
            .setFilter(
                PropertyFilter.eq("ownerId", idOwner)
            )           
            .build();
        QueryResults<Entity> results = datastore.run(query);

        // Add the post content string into the 'posts' arraylist 
        List<List<String>> posts = new ArrayList<>();
        while (results.hasNext()) {
            List<String> onePost = new ArrayList<>(); 
            Entity entity = results.next();
            String postContent = entity.getString("originalcontent");
            String repliedContent = entity.getString("content");
            String postId = ""+entity.getKey().getId()+"";
            onePost.add(postContent);
            onePost.add(repliedContent);
            onePost.add(postId);
            posts.add(onePost);
        }

        long idOwnerLong = Long.valueOf(idOwner);
        Query<Entity> queryName = Query.newGqlQueryBuilder(Query.ResultType.ENTITY,             
                          "SELECT * FROM User WHERE __key__ = KEY(User, @idOwner")
                      .setBinding("idOwner", idOwnerLong)
                      .build();

        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(posts));
    }
}