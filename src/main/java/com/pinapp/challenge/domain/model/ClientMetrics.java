package com.pinapp.challenge.domain.model;

import java.util.Objects;

public class ClientMetrics {
    private Double averageAge;
    private Double standardDeviation;
    private Long totalClients;

    public ClientMetrics() {}

    public ClientMetrics(Double averageAge, Double standardDeviation, Long totalClients) {
        this.averageAge = averageAge;
        this.standardDeviation = standardDeviation;
        this.totalClients = totalClients;
    }

    public Double getAverageAge() {
        return averageAge;
    }

    public void setAverageAge(Double averageAge) {
        this.averageAge = averageAge;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public Long getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(Long totalClients) {
        this.totalClients = totalClients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientMetrics that = (ClientMetrics) o;
        return Objects.equals(averageAge, that.averageAge) &&
                Objects.equals(standardDeviation, that.standardDeviation) &&
                Objects.equals(totalClients, that.totalClients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(averageAge, standardDeviation, totalClients);
    }

    @Override
    public String toString() {
        return "ClientMetrics{" +
                "averageAge=" + averageAge +
                ", standardDeviation=" + standardDeviation +
                ", totalClients=" + totalClients +
                '}';
    }
}
