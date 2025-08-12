package com.ovidius.persistence.model;

import java.util.List;

public record Guide(
   String title,
   String color,
   List<GuidePage> pages
) {}
