package org.bilyoner.service;

import jakarta.annotation.PostConstruct;
import org.bilyoner.exception.CustomException;
import org.bilyoner.model.CouponTimeout;
import org.bilyoner.repository.CouponTimeoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CouponTimeoutService {

    @Autowired
    private CouponTimeoutRepository couponTimeoutRepository;

    @PostConstruct
    public void createInitialTimeout() {
        List<CouponTimeout> couponTimeouts = getAll();

        if(couponTimeouts.isEmpty()) {
            CouponTimeout couponTimeout = new CouponTimeout(2000L, LocalDateTime.now());
            couponTimeoutRepository.save(couponTimeout);
        }
    }

    public CouponTimeout save(Long timeoutInMillis) {
        CouponTimeout existingCouponTimeout = getOne();

        CouponTimeout couponTimeout = new CouponTimeout(timeoutInMillis, LocalDateTime.now());
        couponTimeout.setId(existingCouponTimeout.getId());


        return couponTimeoutRepository.save(couponTimeout);
    }

    public List<CouponTimeout> getAll() {
        return couponTimeoutRepository.findAll();
    }

    public CouponTimeout getOne() {
        List<CouponTimeout> couponTimeouts = couponTimeoutRepository.findAll();

        if(couponTimeouts.size() > 1) {
            throw new CustomException("Birden fazla timeout bilgisi oluşturulamaz");
        }

        Optional<CouponTimeout> couponTimeout = couponTimeouts.stream().findFirst();

        if(!couponTimeout.isPresent()) {
            throw new CustomException("Öncelikle Timeout bilgisi oluşturulmalıdır");
        }

        return couponTimeout.get();

    }
}
