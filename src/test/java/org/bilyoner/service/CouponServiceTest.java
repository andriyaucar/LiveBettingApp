package org.bilyoner.service;

import org.bilyoner.dto.CouponDto;
import org.bilyoner.exception.CustomException;
import org.bilyoner.model.Coupon;
import org.bilyoner.model.CouponTimeout;
import org.bilyoner.model.Match;
import org.bilyoner.repository.CouponRepository;
import org.bilyoner.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CouponServiceTest {
    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private MatchService matchService;

    @Mock
    private CouponTimeoutService couponTimeoutService;

    @Mock
    private ValidationUtil<CouponDto> validationService;

    private CouponDto couponDto;
    private Match match;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        couponDto = new CouponDto();
        couponDto.setMatchId(1L);
        couponDto.setBetType(1);
        couponDto.setUserName("testUser");
        couponDto.setAmount(100.0);

        match = new Match();
        match.setHomeWinOdds(1.5);
        match.setAwayWinOdds(2.5);
        match.setDrawOdds(3.0);
        match.setMatchStartTime(LocalDateTime.now().plusDays(1));
    }

    @Test
    public void testCreateCoupon_Success() {
        when(matchService.getOneById(1L)).thenReturn(Optional.of(match));
        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(couponTimeoutService.getOne()).thenReturn(createCouponTimeout(2000L));

        Coupon createdCoupon = couponService.create(couponDto);

        verify(validationService).validateMatchDto(couponDto);
        verify(matchService).getOneById(1L);
        verify(couponRepository).save(any(Coupon.class));

        assertEquals(1.5, createdCoupon.getBetOdds());
        assertEquals(100.0, createdCoupon.getAmount());
    }

    @Test
    public void testCreateCoupon_MatchNotFound() {
        when(matchService.getOneById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            couponService.create(couponDto);
        });

        assertEquals("İlgili Maç bulunamadı", exception.getMessage());
    }

    @Test
    public void testCreateCoupon_InvalidMatchStartTime() {
        match.setMatchStartTime(LocalDateTime.now().minusDays(1));
        when(matchService.getOneById(1L)).thenReturn(Optional.of(match));

        CustomException exception = assertThrows(CustomException.class, () -> {
            couponService.create(couponDto);
        });

        assertEquals("Maç başladıktan sonra kupon oluşturulamaz", exception.getMessage());
    }

    @Test
    public void testBulkCreate_Success() {
        when(matchService.getOneById(1L)).thenReturn(Optional.of(match));
        when(couponTimeoutService.getOne()).thenReturn(new CouponTimeout(1L, 2000L, LocalDateTime.now()));

        List<Coupon> createdCoupons = couponService.bulkCreate(couponDto, 500);

        assertEquals(createdCoupons.size(), 500);
    }

    @Test
    public void testBulkCreate_ExceedsLimit() {
        when(matchService.getOneById(1L)).thenReturn(Optional.of(match));

        CustomException exception = assertThrows(CustomException.class, () -> {
            couponService.bulkCreate(couponDto, 501);
        });

        assertEquals("Tek seferde en fazla 500 kupon oluşturulabilir", exception.getMessage());
    }

    @Test
    public void testCreateCoupon_TimeoutExceeded() {
        when(matchService.getOneById(1L)).thenReturn(Optional.of(match));
        when(couponTimeoutService.getOne()).thenReturn(createCouponTimeout(2000L));

        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> {
            Thread.sleep(3000);
            return invocation.getArgument(0);
        });

        CustomException exception = assertThrows(CustomException.class, () -> {
            couponService.create(couponDto);
        });

        assertEquals("Kupon kaydetme işlemi 2.0 saniyede tamamlanamadı", exception.getMessage());
    }

    private CouponTimeout createCouponTimeout(long timeoutInMillis) {
        CouponTimeout couponTimeout = new CouponTimeout();
        couponTimeout.setTimeoutInMillis(timeoutInMillis);
        return couponTimeout;
    }

}
