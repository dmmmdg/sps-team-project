package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Key;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.WebRowSet;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/signup")
public class SignUpWithGoogleServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String nameValue = Jsoup.clean(request.getParameter("name"), Whitelist.none());
    String emailValue = Jsoup.clean(request.getParameter("email"), Whitelist.none());
    String idValue = Jsoup.clean(request.getParameter("ID"),Whitelist.none());
    System.out.println("hello!"+nameValue);

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("User");
    FullEntity newUser =
        Entity.newBuilder(keyFactory.newKey())
            .set("name", nameValue)
            .set("email", emailValue)
            .set("ID",idValue)
            .build();
        datastore.put(newUser);
        response.sendRedirect("https://summer22-sps-39.wl.r.appspot.com/mainPage.html?id="+idValue);


    }
}