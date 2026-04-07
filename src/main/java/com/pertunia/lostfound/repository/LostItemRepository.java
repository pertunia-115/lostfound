package com.pertunia.lostfound.repository;

import com.pertunia.lostfound.model.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    List<LostItem> findByItemNameContainingIgnoreCase(String itemName);
}
