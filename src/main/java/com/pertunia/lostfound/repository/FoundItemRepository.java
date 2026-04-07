package com.pertunia.lostfound.repository;

import com.pertunia.lostfound.model.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    List<FoundItem> findByItemNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String itemName, String location);
}
