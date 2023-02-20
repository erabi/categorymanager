package com.test.categorymanager.config.dataloader;

import com.test.categorymanager.model.Category;
import com.test.categorymanager.service.CategoryManipulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Afin que l'application se lance rapidement dans le cadre d'un environnement
 * de test assimilé à un POC, j'ai choisi une infrastructure légère (HSQLDB pour la
 * bdd) avec un chargmement des données au démarrage de l'application.
 * <p>
 * Pour gagner en rapidité au lancement et pour des raisons de performances
 * j'ai limité le nombre de catégories orphelines à 50 (au lieu de 1000) et
 * le nombre de descendants à 5 au lieu de 10 (l'impact d'ajout de niveau de
 * descendant étant exponentiel le gain et conséquent).
 */
@Component
public class DataPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataPublisher.class);
    private final CategoryManipulationService categoryManipulationService;
    private final int ORPHAN_CATEGORIES = 50;
    private final int DESCENDANTS = 5;
    private final int MAX_CHILDREN = 10;
    private final int BATCH_SIZE = 10;
    private final ApplicationEventPublisher applicationEventPublisher;

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

            if (categories.size() == BATCH_SIZE) {
                publishInsertCategoriesEvent(categories);
                categories.clear();
            }
        }
        LOGGER.info("Finished loading categories into database.");
    }

    private void createChildren(List<Integer> list, int index, List<Category> childCategories) {
        if (childCategories.size() == BATCH_SIZE) {
            publishInsertCategoriesEvent(childCategories);
            childCategories.clear();
        }

        if (list.size() < DESCENDANTS + 1) {
            int countChildren = (int) ((Math.random() * (MAX_CHILDREN - 1)) + 1);
            for (int i = 0; i < countChildren; i++) {
                List<Integer> childList = new ArrayList<>(list);
                childList.add(i + 1);
                childCategories.add(Category.fromList(childList));
                createChildren(childList, index + 1, childCategories);
            }
        }
    }

    private void publishInsertCategoriesEvent(final List<Category> categories) {
        InsertCategoryEvent insertCategoryEvent = new InsertCategoryEvent(this, categories);
        applicationEventPublisher.publishEvent(insertCategoryEvent);
    }
}
