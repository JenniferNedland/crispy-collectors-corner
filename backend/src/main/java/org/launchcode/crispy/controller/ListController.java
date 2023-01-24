package org.launchcode.crispy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.launchcode.crispy.model.Collection;
import org.launchcode.crispy.model.Movie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin
public class ListController {
  private final HttpClient client;

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
  public String createCollection(@RequestBody Collection newCollection,
                                 @AuthenticationPrincipal String userId) {
    collectionList.add(new CollectionWithOwner(userId, newCollection));

    return "new collection id goes here";
  }
}