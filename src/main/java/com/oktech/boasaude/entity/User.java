package com.oktech.boasaude.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.oktech.boasaude.dto.CreateUserDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um usuário do sistema.
 * Contém informações como nome, email, CPF, senha, provedor de autenticação e
 * papel do usuário.
 * Implementa UserDetails para integração com o Spring Security.
 * 
 * @author Arlindo Neto
 * @version 1.0
 * @author Lucas Ouro
 * @version 1.1
 * Create a list of addresses associated with the user.
 * 
 * @see CreateUserDto
 * @see UserDetails
 */

@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Unique identifier for the user

    private String name; // Full name of the user

    private String email; // Unique email address

    private String cpf; // Brazilian CPF (Cadastro de Pessoas Físicas)

    private String password; // Encrypted password

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider; // Possible values: LOCAL, GOOGLE, FACEBOOK

    private String providerId; // ID from the external provider (if applicable)

    @Enumerated(EnumType.STRING)
    private UserRole role; // Possible values: USER, ADMIN, PRODUCTOR

    private String phone; // Phone number of the user

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>(); // List of addresses associated with the user

    private boolean isactive; // Indicates if the user account is active
    // Timestamps for creation and last update
    @CreatedDate
    private LocalDateTime createdAt;
    // Timestamp when the user was created
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Construtor para criar um usuário com o papel de USUÁRIO.
     */
    public User(CreateUserDto createUserDto) {
        this.name = createUserDto.name();
        this.email = createUserDto.email();
        this.cpf = createUserDto.cpf();
        this.phone = createUserDto.phone();
        this.authProvider = AuthProvider.LOCAL; // Default to local authentication
        this.role = UserRole.USER; // Default role is USER
        this.isactive = true; // New users are active by default

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role != null
                ? this.role.getAuthorities()
                : List.of(new SimpleGrantedAuthority("ROLE_USER")); // fallback
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isactive;
    }

    public void setEnabled(boolean isactive) {
        this.isactive = isactive;
    }
}
