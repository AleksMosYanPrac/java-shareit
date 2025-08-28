package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ItemRequestInfo {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Set<ItemRecommendation> items;

    @Data
    @AllArgsConstructor
    public static class ItemRecommendation {
        private Long id;
        private String name;
    }
}