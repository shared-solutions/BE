package friend.spring.service;

import friend.spring.aws.s3.AmazonS3Manager;
import friend.spring.converter.FileConverter;
import friend.spring.domain.*;
import friend.spring.domain.enums.S3ImageType;
import friend.spring.repository.FileRepository;
import friend.spring.repository.UuidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager s3Manager;

    private final FileRepository fileRepository;

    public List<File> uploadPostImages(List<MultipartFile> multipartFiles, S3ImageType type, Post post) {
        List<File> fileList = new ArrayList<>();
        // forEach 구문을 통해 multipartFiles 리스트로 넘어온 파일들을 순차적으로 fileNameList 에 추가
        multipartFiles.forEach(file -> {
            String pictureUrl = s3Manager.uploadFile(s3Manager.generatePostKeyName(createFileName()), file);
            File newFile = fileRepository.save(FileConverter.toFile(pictureUrl, null, post, null));
            fileList.add(newFile);
        });
        return fileList;
    }

    public File uploadSingleImage(MultipartFile file, S3ImageType type, User user, Candidate candidate) {
        File newFile;
        if (type == S3ImageType.USER && user != null) { // 사용자 프로필 이미지인 경우
            String pictureUrl = s3Manager.uploadFile(s3Manager.generateUserKeyName(createFileName()), file);
            newFile = fileRepository.save(FileConverter.toFile(pictureUrl, user, null, null));
        } else { // 후보 이미지인 경우
            String pictureUrl = s3Manager.uploadFile(s3Manager.generateCandidateKeyName(createFileName()), file);
            newFile = fileRepository.save(FileConverter.toFile(pictureUrl, null, null, candidate));
        }
        return newFile;
    }

    // 유저 프로필 사진 변경(이미 데이터가 있는 경우)
    @Transactional
    public File editSingleImage(MultipartFile file, User user) {
        File newFile;
        String pictureUrl = s3Manager.uploadFile(s3Manager.generateUserKeyName(createFileName()), file);
        newFile = fileRepository.findByUserId(user.getId()).get();
        newFile.setUrl(pictureUrl);
        return newFile;
    }


    // 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    public Uuid createFileName() {
        String uuid = UUID.randomUUID().toString();
        return uuidRepository.save(Uuid.builder().uuid(uuid).build());
    }
}
