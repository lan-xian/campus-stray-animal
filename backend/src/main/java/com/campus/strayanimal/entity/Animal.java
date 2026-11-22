// backend/src/main/java/com/campus/strayanimal/entity/Animal.java
package com.campus.strayanimal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "t_animal", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "type"})
}, indexes = {
        // 列表页常见查询：按类型筛选并按创建时间倒序分页
        @Index(name = "idx_type_create_time", columnList = "type, create_time"),
        // 名称模糊搜索的前缀匹配索引
        @Index(name = "idx_name", columnList = "name")
})
@Data
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 10)
    private String type;

    @Column(length = 100)
    private String area;

    @Column(length = 255)
    private String coverImage;

    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"animal"})
    private List<CheckIn> checkIns;
}