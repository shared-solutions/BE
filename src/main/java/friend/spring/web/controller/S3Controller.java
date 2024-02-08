package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.domain.enums.S3ImageType;
import friend.spring.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class S3Controller {
    private final S3Service s3Service;

    // 유저 프로필 이미지 올리기
//    @PostMapping(value = "/uploadImage", consumes = "multipart/form-data")
//    public ApiResponse<List<String>> uploadUserImage(@RequestParam("file") List<MultipartFile> file) {
//        List<String> url = s3Service.uploadImage(file, S3ImageType.USER);
//        return ApiResponse.onSuccess(url);
//    }
}
