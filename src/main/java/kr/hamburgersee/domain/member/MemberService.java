package kr.hamburgersee.domain.member;

import kr.hamburgersee.domain.file.image.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder encoder;
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final ProfileImageService profileImageService;
    private final ProfileImageRepository profileImageRepository;

    @Transactional(readOnly = true)
    public MemberAuthenticatedInfo authenticate(MemberLoginForm form) {
        return memberValidator.login(form.getEmail(), form.getRawPassword());
    }

    @Transactional
    public Long join(MemberJoinForm form) {
        Member createdMember = Member.create(
                form.getEmail(),
                encoder.encode(form.getRawPassword()),
                form.getNickname(),
                form.getRegion(),
                form.getGender(),
                form.getBio());
        Member joinedMember = join(createdMember);
        return joinedMember.getId();
    }

    private Member join(Member member) {
        memberValidator.join(member);
        return memberRepository.save(member);
    }

    @Transactional
    public void updateProfileImage(Long joinedMemberId, MultipartFile profileFile) {
        Optional<Member> optionalMember = memberRepository.findById(joinedMemberId);
        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException("해당 회원의 id를 찾을 수 없습니다.");
        }
        Member member = optionalMember.get();
        try {
            // 프로필 이미지를 업로드 후, 저장합니다.
            Long profileImageId = profileImageService.uploadProfileImage(profileFile);
            Optional<ProfileImage> optionalProfileImage = profileImageRepository.findById(profileImageId);
            if (optionalProfileImage.isEmpty()) {
                throw new ProfileImageNotFoundException("해당 프로필 이미지 id를 찾을 수 없습니다.");
            }
            ProfileImage profileImage = optionalProfileImage.get();

            // 저장된 프로필 이미지를 회원에 부착합니다.
            member.updateProfileImage(profileImage);
        } catch (ProfileImageException e) {
            throw new MemberException("회원 프로필 업로드에 실패했습니다.", e);
        }
    }
}
