package com.google.sps.servlets;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.WebRowSet;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.servlet.http.Cookie;
import java.lang.String;
import java.nio.charset.*;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
@WebServlet("/signup")
public class SignUpWithGoogleServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    String nameValue = Jsoup.clean(request.getParameter("name"), Whitelist.none());
    String emailValue = Jsoup.clean(request.getParameter("email"), Whitelist.none());
    String idValue = Jsoup.clean(request.getParameter("ID"),Whitelist.none());
    System.out.println("hello!"+nameValue);

    Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("User")
        .setFilter(
                PropertyFilter.eq("ID", idValue)
            )
        .build();
    
    QueryResults<Entity> results = datastore.run(query);
    System.out.println("result:"+results);
    if(results.hasNext()){
        //if the user exits, stop here and set a cookie
        String originToken = ""+results.next().getKey().getId()+"";
        final byte[] authBytesFirst = originToken.getBytes(StandardCharsets.UTF_8);
        String encodedFirst = Base64.getEncoder().encodeToString(authBytesFirst);
        final byte[] authBytesSecond = encodedFirst.getBytes(StandardCharsets.UTF_8);
        String encodedSecond = Base64.getEncoder().encodeToString(authBytesSecond);
        Cookie token = new Cookie("token", encodedSecond);
        response.addCookie(token);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(200);
        response.sendRedirect("mainPage.html?id="+idValue);
        return;
    }


    KeyFactory keyFactory = datastore.newKeyFactory().setKind("User");
    Key id = datastore.allocateId(keyFactory.newKey());
    System.out.println("New id: " + id.getId());
    FullEntity newUser =
        Entity.newBuilder(id)
            .set("name", nameValue)
            .set("email", emailValue)
            .set("ID",idValue)
            .build();
        datastore.put(newUser);
    String originToken = ""+id.getId()+"";
    final byte[] authBytesFirst = originToken.getBytes(StandardCharsets.UTF_8);
    String encodedFirst = Base64.getEncoder().encodeToString(authBytesFirst);
    final byte[] authBytesSecond = encodedFirst.getBytes(StandardCharsets.UTF_8);
    String encodedSecond = Base64.getEncoder().encodeToString(authBytesSecond);
    Cookie token = new Cookie("token", encodedSecond);

    String OriginGoogleToken = ""+idValue+"";
    final byte[] GoogleAuthBytesFirst = OriginGoogleToken.getBytes(StandardCharsets.UTF_8);
    String GoogleEncodedFirst = Base64.getEncoder().encodeToString(GoogleAuthBytesFirst);
    final byte[] GoogleAuthBytesSecond = GoogleEncodedFirst.getBytes(StandardCharsets.UTF_8);
    String GoogleEncodedSecond = Base64.getEncoder().encodeToString(GoogleAuthBytesSecond);
    Cookie GoogleToken = new Cookie("Google-token", GoogleEncodedSecond);
    response.addCookie(token);
    response.addCookie(GoogleToken);
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setStatus(200);
    response.sendRedirect("/mainPage.html?id="+idValue);
    }
}