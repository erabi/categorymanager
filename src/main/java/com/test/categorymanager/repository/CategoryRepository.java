package com.test.categorymanager.repository;

import com.test.categorymanager.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT c FROM Category c ORDER BY c.name")
    Page<Category> findAllOrderByName(Pageable pageable);

    Optional<Category> findByName(String name);

    Optional<Category> findById(Long id);

    List<Category> findByNameStartsWith(String name);
}

