package com.example.fitpassserver.domain.coinPaymentHistory.service;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.CoinSinglePayRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.KakaoPaymentRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PlanSubScriptionRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.SinglePayApproveRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.dto.request.SubscriptionCancelRequestDTO;
import com.example.fitpassserver.domain.plan.dto.response.FirstSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.KakaoCancelResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.PlanSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.SubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPaymentService {
    @Value("${kakaopay.secret-key}")
    private String secretKey;
    @Value("${kakaopay.cid}")
    private String cid;
    @Value("${kakaopay.monthly-fee-cid}")
    private String monthlyCid;
    @Value("kakaopay.order-id")
    private String orderId;
    @Value("kakaopay.user-id")
    private String userId;
    private final String BASE_URL = "https://open-api.kakaopay.com/online/v1/payment";
    private final String PLAN_APPROVE_URL = "http://localhost:8080/coin/pay/success";
    private final String PLAN_CANCEL_URL = "http://localhost:8080/coin/pay/cancel";
    private final String PLAN_FAIL_URL = "http://localhost:8080/coin/pay/fail";
    private final String APPROVE_URL = "http://localhost:8080/coin/pay/success";
    private final String CANCEL_URL = "http://localhost:8080/coin/pay/cancel";
    private final String FAIL_URL = "http://localhost:8080/coin/pay/fail";
    private final PlanRepository planRepository;

    @NotNull
    private WebClient getKakaoClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + secretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    /*
    결제 Ready
     */
    //단건 결제
    public KakaoPaymentResponseDTO ready(CoinSinglePayRequestDTO dto) {
        WebClient kakao = getKakaoClient();
        KakaoPaymentRequestDTO request = new KakaoPaymentRequestDTO(
                cid,
                orderId,
                userId,
                dto.itemName(),
                dto.quantity(),
                dto.totalAmount(),
                0,
                APPROVE_URL,
                CANCEL_URL,
                FAIL_URL
        );
        Mono<KakaoPaymentResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/ready")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(KakaoPaymentResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

    //정기 결제 첫번째
    public FirstSubscriptionResponseDTO ready(PlanSubScriptionRequestDTO dto) {
        WebClient kakao = getKakaoClient();
        KakaoPaymentRequestDTO request = new KakaoPaymentRequestDTO(
                monthlyCid,
                orderId,
                userId,
                dto.itemName(),
                1,
                dto.totalAmount(),
                0,
                PLAN_APPROVE_URL,
                PLAN_CANCEL_URL,
                PLAN_FAIL_URL
        );
        Mono<FirstSubscriptionResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/ready")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(FirstSubscriptionResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

    //정기 결제 두번째 회차
    public SubscriptionResponseDTO ready(Member member) {
        Plan plan = planRepository.findByMember(member).orElseThrow(
                () -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND)
        );

        WebClient kakao = getKakaoClient();
        KakaoPaymentRequestDTO request = new KakaoPaymentRequestDTO(
                monthlyCid,
                orderId,
                userId,
                plan.getPlanType().getName(),
                1,
                plan.getPlanType().getPrice(),
                0,
                PLAN_APPROVE_URL,
                PLAN_CANCEL_URL,
                PLAN_FAIL_URL
        );
        Mono<SubscriptionResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/subscription")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SubscriptionResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

    /*
    결제 approve
     */
    //단건 결제 성공
    public KakaoPaymentApproveDTO approve(String pgToken, String tid) {
        WebClient kakao = getKakaoClient();
        SinglePayApproveRequestDTO request = new SinglePayApproveRequestDTO(
                cid,
                tid,
                orderId,
                userId,
                pgToken
        );
        Mono<KakaoPaymentApproveDTO> response = kakao.post()
                .uri(BASE_URL + "/approve")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(KakaoPaymentApproveDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

    public PlanSubscriptionResponseDTO approveSubscription(String pgToken, String tid) {
        WebClient kakao = getKakaoClient();
        SinglePayApproveRequestDTO request = new SinglePayApproveRequestDTO(
                monthlyCid,
                tid,
                orderId,
                userId,
                pgToken
        );
        Mono<PlanSubscriptionResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/approve")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PlanSubscriptionResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

    //정기 구독 끊기
    public KakaoCancelResponseDTO subscriptionCancel(Member member) {
        Plan plan = planRepository.findByMember(member).orElseThrow(
                () -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND)
        );
        WebClient kakao = getKakaoClient();
        SubscriptionCancelRequestDTO request = new SubscriptionCancelRequestDTO(
                monthlyCid,
                plan.getSid()
        );
        Mono<KakaoCancelResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/manage/subscription/inactive")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(KakaoCancelResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }


}
