package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Key;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.WebRowSet;


import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/signup")
public class SignUpPageServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.sendRedirect("/signUp.html");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    String username = Jsoup.clean(request.getParameter("username"), Whitelist.none());
    String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
    String password = Jsoup.clean(request.getParameter("password"),Whitelist.none());

    if(username!="" && email!="" && password!=""){
        System.out.println(request);
        System.out.println("New User: " + username);
        System.out.println("New email: " + email);
        System.out.println("New password: " + password);
    
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("User");
        Key id = datastore.allocateId(keyFactory.newKey());
        System.out.println("New id: " + id.getId());
         FullEntity newUser =
            Entity.newBuilder(id)
                .set("username", username)
                .set("email", email)
                .set("password",password)
                .build();
        datastore.put(newUser);
        Cookie user_name = new Cookie("user_name", username);
        Cookie passwordCookie = new Cookie("password", password);
        Cookie user_id = new Cookie("user_id",id.getId()+"");
        response.addCookie(user_name);
        response.addCookie(passwordCookie);
        response.addCookie(user_id);
    
        response.sendRedirect("/mainPage.html?id="+java.net.URLEncoder.encode(""+id.getId()+"", "UTF-8"));

        
        
    }
  }

}