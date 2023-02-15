package com.test.categorymanager.config;

import com.test.categorymanager.model.Category;
import com.test.categorymanager.service.CategoryService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {

    private final CategoryService categoryService;

    public DataLoader(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @EventListener
    public void insertCategories(ApplicationReadyEvent event) {
        for (int i = 1; i < 4; i++) {
            List<Integer> list = List.of(i);
            categoryService.save(Category.fromList(list));
            System.out.println(list);
            createChildren(list, 0);
        }
    }

    private void createChildren(List<Integer> list, int index) {
        if (list.size() == 4) {
            return;
        } else {
            int countChildren = (int) ((Math.random() * (10 - 1)) + 1);
            for (int i = 0; i < countChildren; i++) {
                List<Integer> childList = new ArrayList<>(list);
                childList.add(i + 1);
                categoryService.save(Category.fromList(childList));
                System.out.println(childList);
                createChildren(childList, index + 1);
            }
        }
    }
}
