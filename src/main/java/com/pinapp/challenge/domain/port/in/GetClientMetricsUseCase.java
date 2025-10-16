package com.pinapp.challenge.domain.port.in;

import com.pinapp.challenge.domain.model.ClientMetrics;

public interface GetClientMetricsUseCase {
    ClientMetrics getClientMetrics();
}
