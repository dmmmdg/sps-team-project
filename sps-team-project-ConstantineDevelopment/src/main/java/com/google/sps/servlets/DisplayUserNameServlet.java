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
@WebServlet("/get-name")
public class DisplayUserNameServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ownerId = request.getParameter("ownerId");
        System.out.println("RepliedOwner:"+ownerId);
        // Initialize Datastore 
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();


        long idOwnerLong = Long.valueOf(ownerId);

        KeyFactory keyFactory = datastore.newKeyFactory().setKind("User");
        Query<Entity> queryName = Query.newEntityQueryBuilder()
            .setKind("User")
            .setFilter(PropertyFilter.gt("__key__", keyFactory.newKey(((Number) idOwnerLong).longValue())))
            .build();
        
        QueryResults<Entity> ownerName = datastore.run(queryName);
        String name = "";
        while (ownerName.hasNext()) {
            Entity entity = ownerName.next();
            String postContent = entity.getString("name");
            name = postContent;
        }
        System.out.println("name: "+name);




        // Convert the 'posts' arraylist into json and send as response
        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(name));
    }
}