package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfo;
import ru.practicum.shareit.request.interfaces.ItemRequestMapper;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ItemRequestMapperImpl implements ItemRequestMapper {

    @Override
    public ItemRequest toItemRequest(Long requesterId, ItemRequestDto dto) {
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setRequesterId(requesterId);
        return request;
    }

    @Override
    public ItemRequestDto toDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        return dto;
    }

    @Override
    public ItemRequestInfo toItemRequestInfo(ItemRequest request) {
        ItemRequestInfo requestInfo = new ItemRequestInfo();
        requestInfo.setId(request.getId());
        requestInfo.setDescription(request.getDescription());
        requestInfo.setCreated(request.getCreated());
        Set<ItemRequestInfo.ItemRecommendation> itemRecommendationSet = request.getItems()
                .stream()
                .map(i -> new ItemRequestInfo.ItemRecommendation(i.getId(), i.getName()))
                .collect(Collectors.toSet());
        requestInfo.setItems(itemRecommendationSet);
        return requestInfo;
    }
}
