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

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);

    List<Category> findByNameIn(List<String> ancestors);

    List<Category> findByNameStartsWith(String name);

    @Query(nativeQuery = true,
            value = "SELECT * FROM category c WHERE REGEXP_MATCHES(c.name, ?1)")
    List<Category> findChildren(String regex);

}

