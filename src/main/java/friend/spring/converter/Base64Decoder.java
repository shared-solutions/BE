package friend.spring.converter;

import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.Base64;

public class Base64Decoder {

    public static MultipartFile decodeBase64ToMultipartFile(String base64String) throws IOException {
        // Base64 문자열을 디코딩하여 byte 배열로 변환
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        // 디코딩된 byte 배열을 이용하여 MultipartFile 생성
        MultipartFile multipartFile = createMultipartFile(decodedBytes);

        return multipartFile;
    }

    private static MultipartFile createMultipartFile(byte[] content) throws IOException {
        // MultipartFile을 구현한 클래스를 사용하여 MultipartFile 생성
        return new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                // 임의의 파일 이름 또는 고정된 확장자를 사용할 수 있습니다.
                return "example.jpg";
            }

            @Override
            public String getContentType() {
                // 이미지 타입에 맞게 Content-Type 설정
                return "image/jpeg"; // 예시로 JPEG 이미지로 설정
            }

            @Override
            public boolean isEmpty() {
                return content.length == 0;
            }

            @Override
            public long getSize() {
                return content.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return content;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(content);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                try (OutputStream os = new FileOutputStream(dest)) {
                    os.write(content);
                }
            }
        };
    }
}
