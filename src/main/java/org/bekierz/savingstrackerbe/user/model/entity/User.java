package org.bekierz.savingstrackerbe.user.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.bekierz.savingstrackerbe.saving.model.entity.Saving;

import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String email;
    @NotEmpty
    @Setter
    private String password;
    @NotEmpty
    private String name;
    @NotEmpty
    @Column(name = "last_name")
    private String lastName;

    @OneToMany
    private List<Saving> savingList;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return id.equals(user.id)
                && email.equals(user.email)
                && password.equals(user.password)
                && name.equals(user.name)
                && lastName.equals(user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, lastName);
    }
}
