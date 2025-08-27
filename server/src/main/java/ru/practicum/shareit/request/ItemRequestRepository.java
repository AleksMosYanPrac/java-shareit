package ru.practicum.shareit.request;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends CrudRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequesterIdOrderByCreatedDesc(long requesterId);

    List<ItemRequest> findAllByOrderByCreatedDesc();

    Optional<ItemRequest> findById(long requestId);
}