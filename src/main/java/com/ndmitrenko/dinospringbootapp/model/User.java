package com.ndmitrenko.dinospringbootapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "last_name", unique = true)
    @NotNull
    private String lastName;

    @Column(name = "user_id")
    @NotNull
    @NaturalId
    private Long userId;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserContact> userContacts;

    public User(@NotNull String firstName, @NotNull String lastName, @NotNull Long userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
    }
}

