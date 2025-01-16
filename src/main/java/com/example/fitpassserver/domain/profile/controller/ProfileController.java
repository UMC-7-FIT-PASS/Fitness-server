package com.example.fitpassserver.domain.profile.controller;

import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.profile.dto.ProfileResponseDTO;
import com.example.fitpassserver.domain.profile.service.ProfileService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("")
    @Operation(summary = "프로필 조회 API", description = "사용자의 ID로 프로필을 조회하는 api")
    public ApiResponse<ProfileResponseDTO.GetProfileDTO> getProfile(@CurrentMember Member member) {
        ProfileResponseDTO.GetProfileDTO profile = profileService.getProfile(member.getId());
        return ApiResponse.onSuccess(profile);
    }


    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 변경 API", description = "프로필 이미지 변경하는 api")
    public ApiResponse<?> patchProfile(@CurrentMember Member member, @RequestPart("image") MultipartFile image) {

        String imageUrl = profileService.updateProfileImage(image);

        return ApiResponse.onSuccess(imageUrl);
    }

    @DeleteMapping("")
    @Operation(summary = "프로필 삭제 API", description = "사용자의 프로필 이미지를 삭제하는 API")
    public ApiResponse<Void> deleteProfile(@CurrentMember Member member) {
        profileService.deleteProfile(member.getId());
        return ApiResponse.onSuccess(null);
    }

}