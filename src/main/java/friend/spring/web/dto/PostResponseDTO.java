package friend.spring.web.dto;
import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.PostVoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

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
        Boolean myPost;
        Boolean OnGoing;
        Boolean isVoted; //투표글에서만 사용, 후기글에서는 null
        PostType postType;
        PostVoteType postVoteType;
        String nickname;
        String userImg;
        LocalDateTime createdAt;
        String title;
        String content;
        List<FileDTO> file; // 첨부파일 이미지 리스트
        String pollTitle; // 투표글에서만 사용, 후기글에서는 null
        List<PollOptionDTO.PollOptionRes> pollOption; // 투표글에서만 사용, 후기글에서는 null
        List<PollOptionDTO.PollOptionRes> topCandidate;
        List<PollOptionDTO.PollOptionRes> userVote;// 투표글에서 사용자가 투표완료시 투표한 후보
        List<Integer> userVotePercent; // 투표글에서 사용자가 투표 완료시 투표한 후보 선택 퍼센트
        List<Integer> topCandidatePercent;
        List<Integer> allCandidatePercent;
        List<String> userVoteResult; // 투표글에서 사용자가 투표 완료시 투표인원/총인원
        List<String> topVoteResult;
        Integer userGauge; // 게이지 투표글에서만 사용, 후기글에서는 null
        Integer totalGauge;
        Integer point; // 투표글에서만 사용, 후기글에서는 null
        ParentPostDTO parentPost; // 후기글에서만 사용, 일반글에서는 null
        LocalDateTime deadline; // 투표글에서만 사용, 후기글에서는 null
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
        Boolean onGoing;
        Boolean isVoted;
        Long postId;
        PostVoteType postVoteType;
        String nickname;
        String userImg;
        String title;
        String content;
        LocalDateTime uploadDate;
        List<PollOptionDTO.PollOptionRes> pollOption;
        List<PollOptionDTO.PollOptionRes> topCandidate;
        List<PollOptionDTO.PollOptionRes> userVote;
        List<Integer> userVotePercent;
        List<Integer> topCandidatePercent;
        List<Integer> allCandidatePercent;
        String pollTitle; //게이지투표만 해당(게이지 투표 이름)
        Integer userGauge;
        Integer totalGauge;
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
        Long postId;
        String nickname;
        String userImg;
        String title;
        String content;
        LocalDateTime uploadDate;
        List<FileDTO> ReviewPicList;
        Integer like;
        Integer comment;
        Boolean isLike;
        Boolean isComment;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPostDTO {
        Long postId;
        String nickName;
        LocalDateTime createdAt;
        String title;
        Integer postLike;
        Integer comment;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostLikeRes {
        Long post_like_id;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScrapCreateRes {
        Long post_scrap_id;
    }
}