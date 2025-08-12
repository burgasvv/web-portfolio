package org.burgas.portfolioservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "portfolio", schema = "public")
@NamedEntityGraph(
        name = "portfolio-with-identity-and-profession",
        attributeNodes = {
                @NamedAttributeNode(value = "identity"),
                @NamedAttributeNode(value = "profession"),
                @NamedAttributeNode(value = "projects")
        }
)
public final class Portfolio extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "identity_id", referencedColumnName = "id")
    private Identity identity;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "profession_id", referencedColumnName = "id")
    private Profession profession;

    @Column(name = "opened", nullable = false)
    private Boolean opened;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Project> projects;
}
