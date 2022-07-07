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
@WebServlet("/display-posts")
public class DisplayPostsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Initialize Datastore 
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        // Store a fake post to Datastore to check display functionality
        Key fakePostKey = datastore.newKeyFactory().setKind("Post").newKey("fakePost");
        Key anotherFakePostKey = datastore.newKeyFactory().setKind("Post").newKey("2");
        Entity fakePost =
            Entity.newBuilder(fakePostKey)
                .set("content", "This is a test post by Mia Chen")
                .build();
        Entity anotherFakePost =
            Entity.newBuilder(anotherFakePostKey)
                .set("content", "This is another test post by Mia Chen")
                .build();
        datastore.put(fakePost);
        datastore.put(anotherFakePost);


        // Get the fake post
        Query<Entity> query = Query.newEntityQueryBuilder().setKind("Post").build();
        QueryResults<Entity> results = datastore.run(query);

        // Add the post content string into the 'posts' arraylist 
        List<String> posts = new ArrayList<>();
        while (results.hasNext()) {
            Entity entity = results.next();
            String postContent = entity.getString("content");
            posts.add(postContent);
        }

        // Convert the 'posts' arraylist into json and send as response
        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(posts));
    }
}
