package com.test.categorymanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {

    @Id
    @NonNull
    @Column(name = "name")
    private String name;

    public static Category fromList(List<Integer> list) {
        if (list == null) {
            throw new IllegalArgumentException("list cannot be null");
        }

        return new Category("category." +
                list.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(".")));
    }
}
