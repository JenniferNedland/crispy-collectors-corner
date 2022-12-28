package org.launchcode.crispy.controller;

import java.util.List;

import org.launchcode.crispy.model.Collection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListController {
  @GetMapping("/public/lists")
  public List<Collection> listPublicCollection() {
    return List.of(
      new Collection(
        "Kickass Laser Movies",
        "movies",
        List.of("laser-fart", "laser-moon", "laser-wolf")
      ),
      new Collection(
        "World's Greatest Shark Movies or Something. Yeah.",
        "movies",
        List.of("jaws", "5-headed-shark-attack", "sharknado", "sharkboy-and-lavagirl", "sharktopus", "mega-shark-vs-giant-octopus",)
      )
    );
  }
}