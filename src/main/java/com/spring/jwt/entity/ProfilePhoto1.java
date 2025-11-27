package com.spring.jwt.entity;

import com.spring.jwt.dto.ProfilePhotoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ProfilePhoto1")
public class ProfilePhoto1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProfilePhoto1Id", nullable = false)
    private Integer ProfilePhoto1Id;

    @Column(name = "Documentlink", length = 250)
    private String documentLink;

    @Column(name = "userId")
    private Integer userId;



    public ProfilePhoto1(ProfilePhotoDto profilePhotoDto) {
        this.documentLink = profilePhotoDto.getDocumentLink();
        this.userId = profilePhotoDto.getUserId();
    }


}
