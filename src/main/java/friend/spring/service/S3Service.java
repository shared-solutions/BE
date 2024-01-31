//package friend.spring.service;
//
//import friend.spring.aws.s3.AmazonS3Manager;
//import friend.spring.domain.Uuid;
//import friend.spring.repository.UuidRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.UUID;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class S3Service {
//    private final UuidRepository uuidRepository;
//    private final AmazonS3Manager s3Manager;
//
//    // 유저 프로필 사진 올리기인 경우
//    public String uploadUserImage(MultipartFile file) {
//        String uuid = UUID.randomUUID().toString();
//        Uuid savedUuid = uuidRepository.save(Uuid.builder()
//                .uuid(uuid).build());
//
//        String pictureUrl = s3Manager.uploadFile(s3Manager.generateUserKeyName(savedUuid), file);
//        return pictureUrl;
//    }
//}
