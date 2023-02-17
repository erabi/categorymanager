package com.test.categorymanager.config.dataloader;

import com.test.categorymanager.model.Category;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class InsertCategoryEvent extends ApplicationEvent {

    private List<Category> categories;

    public InsertCategoryEvent(Object source, List<Category> categories) {
        super(source);
        this.categories = categories;
    }
}
