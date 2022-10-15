package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.s3.S3FileUploadService;
import cmc.feelim.domain.user.User;
import cmc.feelim.domain.user.UserRepository;
import cmc.feelim.domain.user.dto.GetProfileRes;
import cmc.feelim.domain.user.dto.PatchProfileReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final S3FileUploadService fileUploadService;

    /** 유저 프로필 반환 **/
    public GetProfileRes getProfile() throws BaseException {
        User user = authService.getUserFromAuth();
        GetProfileRes profile = new GetProfileRes(user);
        return profile;
    }

    /** 프로필 수정 **/
    @Transactional
    public Long updateProfile(PatchProfileReq patchProfileReq) throws BaseException {
        User user = authService.getUserFromAuth();

        if(patchProfileReq.getImage() != null) {
            user.updateImage(fileUploadService.uploadImageFromUser(patchProfileReq.getImage(), user));
        }

        user.updateProfile(patchProfileReq);

        return user.getId();
    }
}
