package com.jowk.parcel.payment;

import com.jowk.parcel.payment.dto.CreatePaymentRequest;
import com.jowk.parcel.payment.dto.CreatePaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.jowk.common.security.domain.AuthenticatedUser;
import java.util.UUID;

@Tag(name = "Payments", description = "Payment operations (simulated)")
@RequestMapping("/api/payments")
public interface PaymentApi {

    @Operation(summary = "Create a payment checkout session (simulated)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Payment session created")
    })
        @PostMapping("/checkout")
        ResponseEntity<CreatePaymentResponse> createCheckout(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody @Valid CreatePaymentRequest request);

    @Operation(summary = "Confirm payment (simulated webhook)")
    @PostMapping("/{paymentId}/confirm")
    ResponseEntity<Void> confirmPayment(@AuthenticationPrincipal AuthenticatedUser user,
                                         @PathVariable("paymentId") UUID paymentId);

}
