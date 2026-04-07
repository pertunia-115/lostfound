package com.pertunia.lostfound.controller;

import com.pertunia.lostfound.model.FoundItem;
import com.pertunia.lostfound.model.LostItem;
import com.pertunia.lostfound.repository.FoundItemRepository;
import com.pertunia.lostfound.repository.LostItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/found-items")
public class FoundItemController {

    @Autowired
    private FoundItemRepository foundItemRepository;

    @Autowired
    private LostItemRepository lostItemRepository;

    @PostMapping
    public List<LostItem> addFoundItem(@RequestBody FoundItem item) {
        if (isBlank(item.getItemName()) || isBlank(item.getDescription()) || isBlank(item.getLocation())) {
            throw new ResponseStatusException(BAD_REQUEST, "itemName, description, and location are required");
        }

        item.setImageUrl(normalizeImageUrl(item.getImageUrl()));
        item.setStatus("FOUND");
        foundItemRepository.save(item);
        return lostItemRepository.findByItemNameContainingIgnoreCase(item.getItemName());
    }

    @GetMapping
    public Iterable<FoundItem> getAllFoundItems() {
        return foundItemRepository.findAll();
    }

    @PutMapping("/{id}/claim")
    public FoundItem claimItem(@PathVariable Long id) {
        FoundItem item = foundItemRepository.findById(id).orElse(null);

        if (item != null) {
            item.setStatus("CLAIMED");
            return foundItemRepository.save(item);
        }

        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String normalizeImageUrl(String imageUrl) {
        return isBlank(imageUrl) ? "" : imageUrl.trim();
    }

}
