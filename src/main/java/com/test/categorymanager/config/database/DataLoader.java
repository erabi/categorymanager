package com.test.categorymanager.config.database;

import com.test.categorymanager.model.Category;
import com.test.categorymanager.service.CategoryManagementService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {
    private final CategoryManagementService categoryManagementService;
    private final int ORPHAN_CATEGORIES = 100;
    private final int DESCENDANTS = 4;
    private final int MAX_CHILDREN = 10;

    public DataLoader(CategoryManagementService categoryManagementService) {
        this.categoryManagementService = categoryManagementService;
    }

    @EventListener
    public void insertCategories(ApplicationReadyEvent event) {
        for (int i = 1; i <= ORPHAN_CATEGORIES; i++) {
            List<Integer> list = List.of(i);
            categoryManagementService.save(Category.fromList(list));
            createChildren(list, 0);
        }
    }

    private void createChildren(List<Integer> list, int index) {
        if (list.size() == DESCENDANTS + 1) {
        } else {
            int countChildren = (int) ((Math.random() * (MAX_CHILDREN - 1)) + 1);
            for (int i = 0; i < countChildren; i++) {
                List<Integer> childList = new ArrayList<>(list);
                childList.add(i + 1);
                categoryManagementService.save(Category.fromList(childList));
                createChildren(childList, index + 1);
            }
        }
    }
}
