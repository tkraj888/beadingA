package com.spring.jwt.entity;


import com.spring.jwt.Wallet.Entity.WalletAccount;
import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.dto.RegisterDto;
import com.spring.jwt.dto.UserProfileDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", initialValue = 1000)
    private Integer id;

    @NotBlank(message = "Email cannot be blank")
    @Email
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @Column(name = "email", nullable = false, length = 250, unique = true)
    private String email;

    @NotBlank(message = "Mobile number Cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be of 10 digits")
    @Column(name = "mobile_no", unique = true)
    private String mobileNo;

    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password", nullable = false, length = 250)
    private String password;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    private Long profilePhotoId;

    private Integer profilePhotoId1;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Userprofile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private InspectorProfile inspectorProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Dealer dealer;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private WalletAccount account;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private SalesPerson salesPerson;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Set<Role> roles;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    public User(RegisterDto registerDto, DealerDto dealerDto) {
        this.email = registerDto.getEmail();
        this.mobileNo = registerDto.getMobileNo();
        this.password = registerDto.getPassword();
        if (dealerDto != null) {
            this.dealer = new Dealer(dealerDto);
            this.dealer.setUser(this);
        }

    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public User(UserProfileDto userProfileDto) {
        this.email = userProfileDto.getEmail();
        this.mobileNo = userProfileDto.getMobile_no();
    }
}




