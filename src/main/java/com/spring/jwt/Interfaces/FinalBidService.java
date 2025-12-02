package com.spring.jwt.Interfaces;
import com.spring.jwt.dto.FinalBidDto;
import com.spring.jwt.entity.Final1stBid;
import com.spring.jwt.exception.BidAmountLessException;
import com.spring.jwt.exception.BidForSelfAuctionException;

import java.util.List;

public interface FinalBidService {
    public String FinalPlaceBid(FinalBidDto finalBidDto) throws BidAmountLessException, BidForSelfAuctionException;

    public List<FinalBidDto> getByUserId(Integer userId);

    public  FinalBidDto getByCarID(Integer bidCarId );

    public Final1stBid getByPlacedBidId(Integer placedBidId);

    public FinalBidDto getById(Integer placedBidId);

    public  FinalBidDto getTopOne(Integer bidCarId);

}
