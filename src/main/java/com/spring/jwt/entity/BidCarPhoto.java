package com.spring.jwt.entity;

import com.spring.jwt.dto.BidCarDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "BidCarPhoto")
public class BidCarPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DocumentId", nullable = false)
    private Integer DocumentId;

    @Column(name = "DocumentType", length = 250)
    private String documentType;

    @Column(name = "Documentlink", length = 250)
    private String documentLink;

    @Column(name = "doc", length = 250)
    private String doc;

    @Column(name = "beadingCarId")
    private Integer beadingCarId;

    @Column(name = "doctype", length = 250)
    private String doctype;

    @Column(name = "subtype", length = 250)
    private String subtype;

    @Column(name = "comment", length = 250)
    private String comment;

    public BidCarPhoto() {
    }

    public BidCarPhoto(BidCarDto documentDto) {
        this.comment = documentDto.getComment();
        this.subtype = documentDto.getSubtype();
        this.doctype = documentDto.getDoctype();
        this.doc = documentDto.getDoc();
        this.documentLink = documentDto.getDocumentLink();
        this.beadingCarId = documentDto.getBeadingCarId();
        this.documentType = documentDto.getDocumentType();
    }


}