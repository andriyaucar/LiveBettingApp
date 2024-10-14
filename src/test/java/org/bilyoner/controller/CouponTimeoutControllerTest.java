package org.bilyoner.controller;

import org.bilyoner.model.CouponTimeout;
import org.bilyoner.service.CouponTimeoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponTimeoutController.class)
public class CouponTimeoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponTimeoutService couponTimeoutService;

    @InjectMocks
    private CouponTimeoutController CouponTimeoutController;

    private CouponTimeout couponTimeout;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        couponTimeout = new CouponTimeout();
        couponTimeout.setId(1L);
        couponTimeout.setTimeoutInMillis(10);
        couponTimeout.setUpdatedAt(LocalDateTime.parse("2024-10-30T15:30:00"));
    }

    @Test
    public void testCreateCouponTimeout_Success() throws Exception {
        when(couponTimeoutService.save(any(Long.class))).thenReturn(couponTimeout);

        mockMvc.perform(post("/coupon-timeout/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("10"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.timeoutInMillis").value(10))
                .andExpect(jsonPath("$.updatedAt").value("2024-10-30T15:30:00"));
    }

    @Test
    public void testGetAllCouponTimeout_Success() throws Exception {
        List<CouponTimeout> couponTimeoutList = Arrays.asList(couponTimeout, couponTimeout);
        when(couponTimeoutService.getAll()).thenReturn(couponTimeoutList);

        mockMvc.perform(get("/coupon-timeout/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].timeoutInMillis").value(10))
                .andExpect(jsonPath("$[0].updatedAt").value("2024-10-30T15:30:00"));
    }

}
