package org.bilyoner.service;

import org.bilyoner.exception.CustomException;
import org.bilyoner.model.CouponTimeout;
import org.bilyoner.repository.CouponTimeoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CouponTimeoutServiceTest {
    @InjectMocks
    private CouponTimeoutService couponTimeoutService;

    @Mock
    private CouponTimeoutRepository couponTimeoutRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateInitialCouponTimeout_Success() {
        CouponTimeout couponTimeout = new CouponTimeout(1L, 1000L, LocalDateTime.now());
        when(couponTimeoutRepository.findAll()).thenReturn(Arrays.asList(couponTimeout));
        when(couponTimeoutRepository.save(any(CouponTimeout.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CouponTimeout createdCouponTimeout = couponTimeoutService.save(2000L);

        verify(couponTimeoutRepository).save(createdCouponTimeout);

        assertEquals(1L, createdCouponTimeout.getId());
        assertEquals(2000L, createdCouponTimeout.getTimeoutInMillis());
    }

    @Test
    public void testCreateInitialCouponTimeout_Fail1() {
        CouponTimeout couponTimeout1 = new CouponTimeout(1L, 1000L, LocalDateTime.now());
        CouponTimeout couponTimeout2 = new CouponTimeout(2L, 1000L, LocalDateTime.now());
        when(couponTimeoutRepository.findAll()).thenReturn(Arrays.asList(couponTimeout1, couponTimeout2));

        CustomException exception = assertThrows(CustomException.class, () -> {
            couponTimeoutService.save(1000L);
        });

        assertEquals("Birden fazla timeout bilgisi oluşturulamaz", exception.getMessage());
    }

    @Test
    public void testCreateInitialCouponTimeout_Fail2() {
        when(couponTimeoutRepository.findAll()).thenReturn(new ArrayList<>());

        CustomException exception = assertThrows(CustomException.class, () -> {
            couponTimeoutService.save(1000L);
        });

        assertEquals("Öncelikle Timeout bilgisi oluşturulmalıdır", exception.getMessage());
    }

    @Test
    public void testGetAll() {
        List<CouponTimeout> mockTimeouts = Arrays.asList(new CouponTimeout(), new CouponTimeout());
        when(couponTimeoutRepository.findAll()).thenReturn(mockTimeouts);

        List<CouponTimeout> result = couponTimeoutService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(couponTimeoutRepository, times(1)).findAll();
    }
}
