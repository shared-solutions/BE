//package friend.spring.web.controller;
//
//import friend.spring.apiPayload.ApiResponse;
//import friend.spring.service.S3Service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/uploadImage")
//public class S3Controller {
//    private final S3Service s3Service;
//
//    // 유저 이미지 올리기
//    @PostMapping(value = "", consumes = "multipart/form-data")
//    public ApiResponse uploadUserImage(@RequestParam("file") MultipartFile file) {
//        String url = s3Service.uploadUserImage(file);
//        return ApiResponse.onSuccess(url);
//    }
//}
