package org.burgas.portfolioservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "identity", schema = "public")
@NamedEntityGraph(
        name = "identity-with-image-and-portfolios",
        attributeNodes = {
                @NamedAttributeNode(value = "image"),
                @NamedAttributeNode(value = "portfolios", subgraph = "portfolio-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(name = "portfolio-subgraph", type = Portfolio.class, attributeNodes = @NamedAttributeNode(value = "profession"))
        }
)
public final class Identity extends AbstractEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "authority", nullable = false)
    private Authority authority;

    @OneToMany(mappedBy = "identity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Portfolio> portfolios;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public String getUsernameNotUserDetails() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return enabled || !UserDetails.super.isEnabled();
    }
}
