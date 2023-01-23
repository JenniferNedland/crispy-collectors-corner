package org.launchcode.crispy.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.C;
import org.launchcode.crispy.model.Collection;
import org.launchcode.crispy.model.Movie;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import jakarta.servlet.http.HttpSession;


@RestController
public class ListController {
  private static final String CLIENT_ID = "144302576001-o81ltuu0kfts285u4jr004jejbvgv4fm.apps.googleusercontent.com";
  private HttpClient client;
  private GoogleIdTokenVerifier verifier;

  private record CollectionWithOwner(String owner, Collection collection) {
  }

  private final List<CollectionWithOwner> collectionList = new ArrayList<>(List.of(
    new CollectionWithOwner("system",
    new Collection(
      "Kickass Laser Movies",
      "movies",
      List.of("89384", "701071", "377315", "35632")
    ))
  ));
  

  public ListController() {
    client = HttpClient.newBuilder()
    .followRedirects(HttpClient.Redirect.NORMAL)
    .build();

    verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
  // Specify the CLIENT_ID of the app that accesses the backend:
    .setAudience(List.of(CLIENT_ID))
    // Or, if multiple clients access the backend:
    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
    .build();
  }

  @GetMapping("/public/lists")
  public List<Collection> listPublicCollection() {
    return collectionList.stream().map(c -> c.collection()).toList();
  }
  @GetMapping("/movies/{id}")
  public Movie getMovie(@PathVariable("id") String id) throws JsonMappingException, JsonProcessingException {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.themoviedb.org/3/movie/%s?api_key=068120d03377a9539115269490d93a54".formatted(id)))
      .build();
    HttpResponse<String> response = client.sendAsync(request, BodyHandlers.ofString())
      .join();

    if (response.statusCode() == 200) {
      ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      Movie movie = objectMapper.readValue(response.body(), Movie.class);
      return movie;
    }
    return null;
  }

  @PostMapping("/user/lists")
  public String createCollection(@RequestBody Collection newCollection, HttpSession httpSession) {
    String userId = (String) httpSession.getAttribute("userId");
    if (userId != null) {
      collectionList.add(new CollectionWithOwner(userId, newCollection));
    }
    
    return "new collection id goes here";
  }

  @PostMapping("/login")
  public String loginOrSomething(@RequestParam String idtoken, HttpSession httpSession) throws GeneralSecurityException, IOException {
    GoogleIdToken idToken = verifier.verify(idtoken);
if (idToken != null) {
  Payload payload = idToken.getPayload();

  // Print user identifier
  String userId = payload.getSubject();
  httpSession.setAttribute("userId", userId);

  // Get profile information from payload
  String email = payload.getEmail();
  boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
  String name = (String) payload.get("name");
  String pictureUrl = (String) payload.get("picture");
  String locale = (String) payload.get("locale");
  String familyName = (String) payload.get("family_name");
  String givenName = (String) payload.get("given_name");

  // Use or store profile information
  // ...

} else {
  System.out.println("Invalid ID token.");
}
    return "session or something";
  }
}