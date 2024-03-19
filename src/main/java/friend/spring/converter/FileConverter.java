package friend.spring.converter;

import friend.spring.domain.Candidate;
import friend.spring.domain.File;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.web.dto.FileDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileConverter {

    public static File toFile(String pictureUrl, User user, Post post, Candidate candidate) {
        return File.builder()
                .url(pictureUrl)
                .user(user)
                .post(post)
                .candidate(candidate)
                .build();
    }

    public static List<FileDTO> toFileDTO(List<File> fileList) {
        return fileList.stream()
                .map(file -> FileDTO.builder()
                        .imageId(file.getId())
                        .imageUrl(file.getUrl())
                        .build())
                .collect(Collectors.toList());
    }
}
