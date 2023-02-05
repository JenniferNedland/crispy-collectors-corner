package org.launchcode.crispy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.launchcode.crispy.model.Collection;
import org.launchcode.crispy.model.Movie;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@CrossOrigin
public class ListController {
  // This is used to make HTTP calls from the backend. Currently we call TMDB to get movie information.
  private final HttpClient client;

  // Combine a collection with an owner
  private record CollectionWithOwner(String owner, Collection collection) {
  }

  // Our in-memory database. Will be replaced by an actual database
  private final List<CollectionWithOwner> collectionList = new ArrayList<>(List.of(
    new CollectionWithOwner(
      "system",
      new Collection(
        "Kickass Laser Movies",
        List.of("89384", "701071", "377315", "35632"),
        false
      )
    )
  ));  // TODO: remove hardcoded objects when we have a persistent database
  

  public ListController() {
    // Initialize client to follow redirects, since TMDB may respond with a redirect.
    client = HttpClient.newBuilder()
    .followRedirects(HttpClient.Redirect.NORMAL)
    .build();
  }

  // Return a list of public collections to the frontend.
  @GetMapping("/public/lists")
  public List<Collection> listPublicCollection() {
    return collectionList.stream()
    .map(c -> c.collection())
    .filter(c -> !c.isPrivate())
    .toList();
  }

  // Return a list of the currently logged in user's collections to the frontend.
  @GetMapping("/user/lists")
  public List<Collection> listUserCollection(
    @AuthenticationPrincipal String userId  // Get the user id from the authentication system
    ) {
    return collectionList.stream() // Look at each of the collections in the in-memory database
      .filter(c -> c.owner.equals(userId))  // Only keep collections that belong to the current user
      .map(c -> c.collection())  // Return the remaining collection objects
      .toList();
  }

  // Look up movie information for a single movie in TMDB and return it to the frontend.
  @GetMapping("/movies/{id}")
  public Movie getMovie(@PathVariable("id") String id) throws JsonMappingException, JsonProcessingException {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.themoviedb.org/3/movie/%s?api_key=068120d03377a9539115269490d93a54".formatted(id)))
      .build();
    HttpResponse<String> response = client.sendAsync(request, BodyHandlers.ofString())
      .join();

    if (response.statusCode() == 200) {
      ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  // Ignore unknown data fields
      Movie movie = objectMapper.readValue(response.body(), Movie.class);  // Parse the response into a Movie object
      return movie;
    }
    return null;  // Fetching the movie failed. TODO: add better error handling.
  }

  // Create a new list for the currently logged in user.
  @PostMapping("/user/lists")
  public String createCollection(@RequestBody Collection newCollection,
                                 @AuthenticationPrincipal String userId) {
    collectionList.add(new CollectionWithOwner(userId, newCollection));

    // TODO: Return the actual id, if we start requiring it in the frontend.
    return "new collection id goes here";
  }
}