package org.bilyoner.controller;

import org.bilyoner.dto.CouponDto;
import org.bilyoner.model.Coupon;
import org.bilyoner.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping(value = "/create")
    public ResponseEntity<Coupon> create(@RequestBody CouponDto couponDto) {
        Coupon createdCoupon = couponService.create(couponDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
    }

    @PostMapping(value = "/bulk-create/{quantity}")
    public ResponseEntity<List<Coupon>> bulkCreate(@RequestBody CouponDto couponDto, @PathVariable int quantity) {
        List<Coupon> createdCoupons = couponService.bulkCreate(couponDto, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupons);
    }

    @GetMapping(value = "get-all")
    public List<Coupon> getAll() {
        return couponService.getAll();
    }
}
