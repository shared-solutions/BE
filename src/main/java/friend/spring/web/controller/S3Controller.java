package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.aws.s3.AmazonS3Manager;
import friend.spring.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/uploadImage")
public class S3Controller {
    private final S3Service s3Service;

    // 유저 이미지 올리기
    @PostMapping(value = "", consumes = "multipart/form-data")
    public ApiResponse uploadUserImage(@RequestPart(value = "file", required = false) MultipartFile file) {
        String url = s3Service.uploadUserImage(file);
        return ApiResponse.onSuccess(url);
    }
}
