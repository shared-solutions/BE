package friend.spring.web.dto;
import friend.spring.domain.Candidate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddPostResultDTO {
        Long postId;
        LocalDateTime createdAt;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDetailResponse {
        String nickname;
        LocalDateTime createdAt;
        String title;
        String content;
        String file;
        String pollTitle; // 투표글에서만 사용, 후기글에서는 null
        List<PollOptionDTO> pollOption; // 투표글에서만 사용, 후기글에서는 null
        Integer gauge; // 게이지 투표글에서만 사용, 후기글에서는 null
        Integer point; // 투표글에서만 사용, 후기글에서는 null
        ParentPostDTO parentPost; // 후기글에서만 사용, 일반글에서는 null
        Timestamp deadline; // 투표글에서만 사용, 후기글에서는 null
        List<PollOptionDTO> userVote; // 투표글에서 사용자가 투표완료시 투표한 후보
        List<Integer> percent; // 투표글에서 사용자가 투표 완료시 투표한 후보 선택 퍼센트
        List<String> voteResult; // 투표글에서 사용자가 투표 완료시 투표인원/총인원
        Integer view;
        Integer like;
        Integer comment;
        Boolean isLike;
        Boolean isComment;

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PollPostGetListDTO{
        List<PollPostGetResponse> pollPostList;
        private Boolean isEnd;
    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PollPostGetResponse{
        String nickname;
        String title;
        String content;
        LocalDateTime uploadDate;
        List<PollOptionDTO> pollOption;
        List<PollOptionDTO> userVote;
        Integer gauge;
        Integer like;
        Integer comment;
        Boolean isLike;
        Boolean isComment;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewPostGetListDTO{
        List<ReviewPostGetResponse> reviewPostList;
        private Boolean isEnd;
    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewPostGetResponse{
        String nickname;
        String title;
        String content;
        LocalDateTime uploadDate;
        String ReviewPic;
        Integer like;
        Integer comment;
        Boolean isLike;
        Boolean isComment;
    }
}