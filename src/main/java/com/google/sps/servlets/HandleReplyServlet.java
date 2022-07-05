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
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet responsible for displaying posts. */
@WebServlet("/reply-post")
public class HandleReplyServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ownerId = Jsoup.clean(request.getParameter("ownerId"), Whitelist.none());
        String postId = Jsoup.clean(request.getParameter("postId"), Whitelist.none());
        String content = Jsoup.clean(request.getParameter("content"), Whitelist.none());
        String originalContent = Jsoup.clean(request.getParameter("originalContent"), Whitelist.none());
    
    long timestamp = System.currentTimeMillis();

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("repliedPost");
     FullEntity repliedEntity =
        Entity.newBuilder(keyFactory.newKey())
            .set("ownerId", ownerId)
            .set("content", content)
            .set("originalcontent",originalContent)
            .set("timestamp",timestamp )
            .build();
    datastore.put(repliedEntity);
    }
}