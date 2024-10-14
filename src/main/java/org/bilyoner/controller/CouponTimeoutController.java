package org.bilyoner.controller;

import org.bilyoner.model.CouponTimeout;
import org.bilyoner.service.CouponTimeoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon-timeout")
public class CouponTimeoutController {

    @Autowired
    private CouponTimeoutService couponTimeoutService;

    @PostMapping(value = "/save")
    public ResponseEntity<CouponTimeout> create(@RequestBody Long timeoutInMillis) {
        CouponTimeout savedCoupon = couponTimeoutService.save(timeoutInMillis);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCoupon);
    }

    @GetMapping(value = "get-all")
    public List<CouponTimeout> getAll() {
        return couponTimeoutService.getAll();
    }
}
