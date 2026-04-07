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
@RequestMapping({"/lost-items", "/lost-item"})
public class LostItemController {

    @Autowired
    private LostItemRepository lostItemRepository;

    @Autowired
    private FoundItemRepository foundItemRepository;

    @PostMapping
    public List<FoundItem> addLostItem(@RequestBody LostItem item) {
        if (isBlank(item.getItemName()) || isBlank(item.getDescription()) || isBlank(item.getLocation())) {
            throw new ResponseStatusException(BAD_REQUEST, "itemName, description, and location are required");
        }

        item.setImageUrl(normalizeImageUrl(item.getImageUrl()));
        item.setStatus("LOST");
        lostItemRepository.save(item);
        return foundItemRepository.findByItemNameContainingIgnoreCaseAndLocationContainingIgnoreCase(
                item.getItemName(),
                item.getLocation()
        );
    }

    @GetMapping
    public Iterable<LostItem> getAllLostItems() {
        return lostItemRepository.findAll();
    }

    @PutMapping("/{id}/claim")
    public LostItem claimItem(@PathVariable Long id) {
        LostItem item = lostItemRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(BAD_REQUEST, "Lost item not found")
        );

        item.setStatus("CLAIMED");
        return lostItemRepository.save(item);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String normalizeImageUrl(String imageUrl) {
        return isBlank(imageUrl) ? "" : imageUrl.trim();
    }
}
