package friend.spring.web.dto;
import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.validation.annotation.TitleTextLimit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

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
        PostType postType;
        PostVoteType postVoteType;
        String nickname;
        String userImg;
        LocalDateTime createdAt;
        String title;
        String content;
        Boolean OnGoing;
        Boolean isVoted; //투표글에서만 사용, 후기글에서는 null
        List<FileDTO> file; // 첨부파일 이미지 리스트
        String pollTitle; // 투표글에서만 사용, 후기글에서는 null
        List<PollOptionDTO.PollOptionRes> pollOption; // 투표글에서만 사용, 후기글에서는 null
        Integer gauge; // 게이지 투표글에서만 사용, 후기글에서는 null
        Integer point; // 투표글에서만 사용, 후기글에서는 null
        ParentPostDTO parentPost; // 후기글에서만 사용, 일반글에서는 null
        LocalDateTime deadline; // 투표글에서만 사용, 후기글에서는 null
        List<PollOptionDTO.PollOptionRes> userVote; // 투표글에서 사용자가 투표완료시 투표한 후보
        List<Integer> percent; // 투표글에서 사용자가 투표 완료시 투표한 후보 선택 퍼센트
        List<String> voteResult; // 투표글에서 사용자가 투표 완료시 투표인원/총인원
        Integer view;
        Integer like;
        Integer comment;
        Boolean isLike;
        Boolean isComment;
        Boolean myPost;

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
        Long postId;
        PostVoteType postVoteType;
        String nickname;
        String userImg;
        String title;
        String content;
        LocalDateTime uploadDate;
        List<PollOptionDTO.PollOptionRes> pollOption;
        List<PollOptionDTO.PollOptionRes> userVote;
        String pollTitle; //게이지투표만 해당(게이지 투표 이름)
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
    public static class PostSummaryListRes {
        String title;
        String content;
        Long post_id;
        List<FileDTO> file;
        Integer like;
        Integer comment_cnt;
        LocalDateTime created_at;
        UserResponseDTO.UserSummaryInfo user;
        String postVoteType;
        Long general_poll_id;
        Long gauge_poll_id;
        Long card_poll_id;
        Integer gauge;
        List<CandidateResponseDTO.CandidateSummaryRes> candidateList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScrapCreateRes {
        Long post_scrap_id;
    }
}