package org.bilyoner.controller;

import org.bilyoner.dto.CouponDto;
import org.bilyoner.exception.CustomException;
import org.bilyoner.model.Coupon;
import org.bilyoner.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponController.class)
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @InjectMocks
    private CouponController couponController;

    private CouponDto couponDto;
    private Coupon coupon;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        couponDto = new CouponDto();
        couponDto.setMatchId(1L);
        couponDto.setBetType(1);
        couponDto.setUserName("testUser");
        couponDto.setAmount(10.0);

        coupon = new Coupon();
        coupon.setId(1L);
        coupon.setUserName("testUser");
        coupon.setAmount(10.0);
    }

    @Test
    public void testCreateCoupon_Success() throws Exception {
        when(couponService.create(any(CouponDto.class))).thenReturn(coupon);

        mockMvc.perform(post("/coupon/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"matchId\": 1, \"betType\": 1, \"userName\": \"testUser\", \"amount\": 10.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userName").value("testUser"))
                .andExpect(jsonPath("$.amount").value(10.0));
    }

    @Test
    public void testBulkCreateCoupons_Success() throws Exception {
        List<Coupon> couponList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Coupon coupon = new Coupon();
            coupon.setId((long) (i + 1));
            coupon.setUserName("testUser" + i);
            coupon.setAmount(10.0 + i);
            couponList.add(coupon);
        }

        when(couponService.bulkCreate(any(CouponDto.class), anyInt())).thenReturn(couponList);

        mockMvc.perform(post("/coupon/bulk-create/500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"matchId\": 1, \"betType\": 1, \"userName\": \"testUser\", \"amount\": 10.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.size()").value(500))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userName").value("testUser0"))
                .andExpect(jsonPath("$[0].amount").value(10.0));
    }

    @Test
    public void testBulkCreateCoupons_ExceedLimit() throws Exception {
        when(couponService.bulkCreate(any(CouponDto.class), eq(501)))
                .thenThrow(new CustomException("Tek seferde en fazla 500 kupon oluşturulabilir"));

        mockMvc.perform(post("/coupon/bulk-create/501")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"matchId\": 1, \"betType\": 1, \"userName\": \"testUser\", \"amount\": 10.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Tek seferde en fazla 500 kupon oluşturulabilir"));
    }

    @Test
    public void testGetAllCoupons_Success() throws Exception {
        List<Coupon> couponList = Arrays.asList(coupon, coupon);
        when(couponService.getAll()).thenReturn(couponList);

        mockMvc.perform(get("/coupon/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userName").value("testUser"))
                .andExpect(jsonPath("$[0].amount").value(10.0));
    }

}
