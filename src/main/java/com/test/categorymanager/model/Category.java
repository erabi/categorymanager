package com.test.categorymanager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

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
