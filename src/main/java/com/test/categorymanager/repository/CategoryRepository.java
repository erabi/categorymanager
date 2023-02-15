package com.test.categorymanager.repository;

import com.test.categorymanager.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT c FROM Category c ORDER BY c.name")
    Page<Category> findAllOrderByName(Pageable pageable);

    Category findByName(String name);
}

