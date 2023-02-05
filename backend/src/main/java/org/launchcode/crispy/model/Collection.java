package org.launchcode.crispy.model;

import java.util.List;

public record Collection(String collectionTitle, List<String> movieIds, boolean isPrivate) {}