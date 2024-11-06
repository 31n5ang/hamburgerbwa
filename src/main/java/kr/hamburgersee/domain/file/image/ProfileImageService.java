package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.file.FileNameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static kr.hamburgersee.domain.aws.storage.StorageUploadFileType.MEMBER_PROFILE_IMAGE;

@Service
@RequiredArgsConstructor
public class ProfileImageService {
    private final ProfileImageRepository profileImageRepository;
    private final ImageManager imageManager;

    @Transactional
    public Long uploadProfileImage(MultipartFile profileFile) {
        try {
            String originalFilename = profileFile.getOriginalFilename();
            String ext = FileNameUtils.parseExt(originalFilename);
            String uploadFilename = FileNameUtils.generateUploadFilename(ext);
            String uploadedUrl = imageManager.uploadImage(profileFile.getBytes(), uploadFilename, MEMBER_PROFILE_IMAGE);

            ProfileImage profileImage = ProfileImage.create(uploadedUrl, originalFilename);
            ProfileImage savedProfileImage = profileImageRepository.save(profileImage);

            return savedProfileImage.getId();
        } catch (IOException e) {
            throw new ProfileImageUploadFailException("프로필 이미지 업로드에 실패했습니다.", e);
        }
    }


}
