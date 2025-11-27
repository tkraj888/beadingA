package com.spring.jwt.entity;



import com.spring.jwt.dto.DocumentDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DocumentId", nullable = false)
    private Integer DocumentId;

    @Column(name = "DocumentType", length = 250)
    private String documentType;

    @Column(name = "Documentlink", length = 250)
    private String documentLink;

    @Column(name = "user_userId", nullable = false)
    private Integer userId;

    @Column(name = "carId", nullable = false)
    private Integer carId;

    public Document() {
    }

    public Document(DocumentDto documentDto) {
        this.documentType = documentDto.getDocumentType();
        this.documentLink = documentDto.getDocumentLink();
        this.userId = documentDto.getUserId();
        this.carId =documentDto.getCarId();
    }
}