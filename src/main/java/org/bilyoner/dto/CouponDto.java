package org.bilyoner.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CouponDto {
    @NotNull(message = "Maç Id's boş olamaz")
    private Long matchId;

    @NotNull(message = "Bahis tipi boş olamaz")
    @Min(value = 0, message = "Bahis tipi 0, 1, 2 olabilir. Ev Sahibi takım için 1, Deplasman takımı için 2, berabere skor için 0 seçilmelidir")
    @Max(value = 2, message = "Bahis tipi 0, 1, 2 olabilir. Ev Sahibi takım için 1, Deplasman takımı için 2, berabere skor için 0 seçilmelidir")
    private int betType;

    @NotNull(message = "Kullanıcı adı boş olamaz")
    private String userName;

    @NotNull(message = "Bahis tutarı boş olamaz")
    @DecimalMin(value = "1", message = "Bahis tutarı en az 1 olmalıdır")
    private double amount;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public int getBetType() {
        return betType;
    }

    public void setBetType(int betType) {
        this.betType = betType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
