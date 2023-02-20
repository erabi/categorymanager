package com.test.categorymanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator")
    @SequenceGenerator(name = "category_generator", sequenceName = "category_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

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

    public static boolean isValidName(String name) {
        return Pattern.matches("^(category)(\\.[1-9]\\d*)(\\.([1-9]|10)){0,10}$", name);
    }

    public String getParentName() {
        return isValidName(this.getName()) ? this.getName().substring(0, this.getName().lastIndexOf('.')) : "";
    }
}
