package com.spring.jwt.repository;

import com.spring.jwt.dto.BidCarDto;
import com.spring.jwt.entity.BidCarPhoto;
import com.spring.jwt.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBidDoc extends JpaRepository<BidCarPhoto,Integer> {
    @Query("SELECT b FROM BidCarPhoto b WHERE b.beadingCarId = :beadingCarId AND b.doctype = :doctype")
    List<BidCarPhoto> findBydocTypeAndbeadingCarId(@Param("beadingCarId") Integer beadingCarId,@Param("doctype") String doctype);

    @Query("SELECT b FROM BidCarPhoto b WHERE b.beadingCarId = :beadingCarId")
    List<BidCarPhoto> findByCarId(@Param("beadingCarId") Integer beadingCarId);

    List<BidCarPhoto> findBybeadingCarIdAndDocumentType(Integer beadingCarId, String documentType);
}