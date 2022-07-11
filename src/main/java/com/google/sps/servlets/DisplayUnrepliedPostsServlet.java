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

/** Servlet responsible for displaying posts. */
@WebServlet("/display-posts-unreplied")
public class DisplayUnrepliedPostsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Initialize Datastore 
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        Query<Entity> query = Query.newEntityQueryBuilder().setKind("Post").build();
        QueryResults<Entity> results = datastore.run(query);

        // Add the post content string into the 'posts' arraylist 
        List<List<String>> posts = new ArrayList<>();
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

        // Convert the 'posts' arraylist into json and send as response
        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(posts));
    }
}