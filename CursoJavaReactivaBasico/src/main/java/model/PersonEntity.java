package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person")
public class PersonEntity {
    @Id
    private Long id;
    @Column("name")
    private String name;
    @Column("age")
    private Integer age;
    @Column("gender")
    private String gender;
    @Column("date_of_birth")
    private LocalDate dateOfBirth;
    @Column("blood_type")
    private String bloodType;

    public PersonEntity(long id, String johnDoe, int age) {
    }
}
