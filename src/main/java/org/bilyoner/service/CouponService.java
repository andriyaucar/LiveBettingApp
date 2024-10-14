package org.bilyoner.service;

import org.bilyoner.dto.CouponDto;
import org.bilyoner.exception.CustomException;
import org.bilyoner.model.Coupon;
import org.bilyoner.model.Match;
import org.bilyoner.repository.CouponRepository;
import org.bilyoner.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MatchService matchService;

    @Autowired
    private CouponTimeoutService couponTimeoutService;

    @Autowired
    private ValidationUtil<CouponDto> validationService;

    @Transactional
    public Coupon create(CouponDto couponDto) {
        validationService.validateMatchDto(couponDto);
        Optional<Match> match = matchService.getOneById(couponDto.getMatchId());

        if(!match.isPresent()) {
            throw new CustomException("İlgili Maç bulunamadı");
        }

        Coupon coupon = new Coupon(
                match.get(),
                couponDto.getBetType(),
                getBetOdds(match.get(), couponDto),
                couponDto.getUserName(),
                couponDto.getAmount(),
                LocalDateTime.now()
        );

        if (match.get().getMatchStartTime().toInstant(ZoneOffset.UTC).isBefore(coupon.getCreatedAt().toInstant(ZoneOffset.UTC))) {
            throw new CustomException("Maç başladıktan sonra kupon oluşturulamaz");
        }
        return saveIndividualCoupon(coupon, couponTimeoutService.getOne().getTimeoutInMillis());
    }

    @Transactional
    public List<Coupon> bulkCreate(CouponDto couponDto, int quantity) {
        validationService.validateMatchDto(couponDto);
        Optional<Match> match = matchService.getOneById(couponDto.getMatchId());

        if(!match.isPresent()) {
            throw new CustomException("İlgili Maç bulunamadı");
        }

        if(quantity > 500) {
            throw new CustomException("Tek seferde en fazla 500 kupon oluşturulabilir");
        }

        if (match.get().getMatchStartTime().toInstant(ZoneOffset.UTC).isBefore(LocalDateTime.now().toInstant(ZoneOffset.UTC))) {
            throw new CustomException("Maç başladıktan sonra kupon oluşturulamaz");
        }

        List<Coupon> coupons = IntStream.range(0, quantity)
                .mapToObj(i -> new Coupon(
                        match.get(),
                        couponDto.getBetType(),
                        getBetOdds(match.get(), couponDto),
                        couponDto.getUserName(),
                        couponDto.getAmount(),
                        LocalDateTime.now()
                ))
                .collect(Collectors.toList());
        return saveAll(coupons, couponTimeoutService.getOne().getTimeoutInMillis());
    }

    private List<Coupon> saveAll(List<Coupon> coupons, long timeout) {
        List<Coupon> savedCoupons = new ArrayList<>();

        coupons.forEach(newCoupon -> {
            savedCoupons.add(saveIndividualCoupon(newCoupon, timeout));
        });
        return savedCoupons;
    }

    private Coupon saveIndividualCoupon(Coupon newCoupon, Long timeout) {
        long startTime = System.currentTimeMillis();
        Coupon createdCoupon = couponRepository.save(newCoupon);

        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime > timeout) {
            throw new CustomException("Kupon kaydetme işlemi " + timeout / 1000.0 + " saniyede tamamlanamadı");
        }
        return createdCoupon;
    }

    private double getBetOdds(Match match, CouponDto couponDto) {
        return couponDto.getBetType() == 1 ? match.getHomeWinOdds() : couponDto.getBetType() == 2 ? match.getAwayWinOdds() : match.getDrawOdds();
    }

    public List<Coupon> getAll() {
        return couponRepository.findAll();
    }
}
