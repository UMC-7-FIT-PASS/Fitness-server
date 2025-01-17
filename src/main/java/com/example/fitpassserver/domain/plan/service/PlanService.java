package com.example.fitpassserver.domain.plan.service;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;

    public void createNewPlan(Member member, String planName, String sid) {
        planRepository.save(Plan.builder()
                .planType(PlanType.getPlanType(planName))
                .planDate(LocalDate.now())
                .sid(sid)
                .member(member)
                .build());
    }

    @Transactional
    public void cancelNewPlan(Member member) {
        Plan plan = planRepository.findByMember(member).orElseThrow(
                () -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND)
        );
        plan.changePlanType(PlanType.NONE);
        planRepository.save(plan);
    }
}