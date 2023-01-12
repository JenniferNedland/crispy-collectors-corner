package org.launchcode.crispy.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

import org.launchcode.crispy.model.Collection;
import org.launchcode.crispy.model.Movie;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ListController {
  private HttpClient client;

  public ListController() {
    client = HttpClient.newBuilder()
    .followRedirects(HttpClient.Redirect.NORMAL)
    .build();
  }

  @GetMapping("/public/lists")
  @CrossOrigin(origins = "http://localhost:3000")
  public List<Collection> listPublicCollection() {
    return List.of(
      new Collection(
        "Kickass Laser Movies",
        "movies",
        List.of("89384", "701071", "377315", "35632")
      ),
      new Collection(
        "World's Greatest Shark Movies or Something. Yeah.",
        "movies",
        List.of("jaws", "5-headed-shark-attack", "sharknado", "sharkboy-and-lavagirl", "sharktopus", "mega-shark-vs-giant-octopus")
      )
    );
  }
  @GetMapping("/movies/{id}")
  @CrossOrigin(origins = "http://localhost:3000")
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
}