package com.pyvaops.visionflow.dto;

public record BoundingBoxDto(
        int xmin,
        int ymin,
        int xmax,
        int ymax
) {}