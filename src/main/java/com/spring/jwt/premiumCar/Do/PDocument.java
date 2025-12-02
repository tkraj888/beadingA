package com.spring.jwt.premiumCar.Do;

import com.spring.jwt.dto.DocumentDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pdocument")
public class PDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PDocumentId", nullable = false)
    private Integer PDocumentId;

    @Column(name = "DocumentType", length = 250)
    private String documentType;

    @Column(name = "Documentlink", length = 250)
    private String documentLink;

    @Column(name = "user_userId", nullable = false)
    private Integer userId;

    @Column(name = "premiumCarId", nullable = false)
    private Integer premiumCarId;

    public PDocument() {
    }

    public PDocument(PDocumentDto documentDto) {
        this.documentType = documentDto.getDocumentType();
        this.documentLink = documentDto.getDocumentLink();
        this.userId = documentDto.getUserId();
        this.premiumCarId =documentDto.getPremiumCarId();
    }
}