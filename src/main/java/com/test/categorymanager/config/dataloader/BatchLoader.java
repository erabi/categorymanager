package com.test.categorymanager.config.dataloader;

import com.test.categorymanager.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatchLoader implements ApplicationListener<InsertCategoryEvent> {

    private final EntityManager entityManager;

    public BatchLoader(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void onApplicationEvent(InsertCategoryEvent insertCategoryEvent) {
        List<Category> categories = insertCategoryEvent.getCategories();
        System.out.println(categories);
        categories.forEach(entityManager::persist);
        entityManager.flush();
        entityManager.clear();
    }
}
