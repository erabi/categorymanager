package com.test.categorymanager.config.dataloader;

import com.test.categorymanager.model.Category;
import com.test.categorymanager.service.CategoryManipulationService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataPublisher {

    private ApplicationEventPublisher applicationEventPublisher;
    private final CategoryManipulationService categoryManipulationService;
    private final int ORPHAN_CATEGORIES = 1000;
    private final int DESCENDANTS = 4;
    private final int MAX_CHILDREN = 10;
    private final int BATCH_SIZE = 100;

    public DataPublisher(CategoryManipulationService categoryManipulationService,
                         ApplicationEventPublisher applicationEventPublisher) {
        this.categoryManipulationService = categoryManipulationService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener
    public void createCategories(ApplicationReadyEvent event) {
        List<Category> categories = new ArrayList<>();
        List<Category> childCategories = new ArrayList<>();

        for (int i = 1; i <= ORPHAN_CATEGORIES; i++) {
            List<Integer> list = List.of(i);
            categories.add(Category.fromList(list));
            createChildren(list, 0, childCategories);

            if (categories.size() == 100) {
                publishInsertCategoriesEvent(categories);
                categories.clear();
            }
        }
    }

    private void createChildren(List<Integer> list, int index, List<Category> childCategories) {
        if (list.size() == DESCENDANTS + 1) {
            childCategories.add(Category.fromList(list));

            if (childCategories.size() == BATCH_SIZE) {
                publishInsertCategoriesEvent(childCategories);
                childCategories.clear();
            }
        } else {
            int countChildren = (int) ((Math.random() * (MAX_CHILDREN - 1)) + 1);
            for (int i = 0; i < countChildren; i++) {
                List<Integer> childList = new ArrayList<>(list);
                childList.add(i + 1);
                createChildren(childList, index + 1, childCategories);
            }
        }
    }

    private void publishInsertCategoriesEvent(final List<Category> categories) {
        InsertCategoryEvent insertCategoryEvent = new InsertCategoryEvent(this, categories);
        applicationEventPublisher.publishEvent(insertCategoryEvent);
    }
}
