package com.example.fitpassserver.domain.profile.entity;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "profile")
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "picture_url", nullable = false)
    private String pictureUrl;

    @Column(name = "picture_key", nullable = false)
    private String pictureKey;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updateProfile(String pictureKey) {
        if (pictureKey != null) {
            this.pictureKey = pictureKey;
        }
    }


}
