package friend.spring.converter;
import friend.spring.domain.*;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.service.PostQueryService;
import friend.spring.web.dto.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static friend.spring.domain.enums.PostType.*;

public class PostConverter {

    private static PostQueryService postQueryService;
    public static PostResponseDTO.AddPostResultDTO toAddPostResultDTO(Post post) {
        return PostResponseDTO.AddPostResultDTO.builder()
                .postId(post.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static PollOptionDTO toPollOptionDTO(Candidate candidate){
        return PollOptionDTO.builder()
                .optionString(candidate.getName())
                .optionImg(candidate.getImage()).build();
    }

    public static ParentPostDTO toParentPostDTO(Post parentPost){
        ParentPollDTO parentPollDTO=null;
        Integer parentLikes =parentPost.getPostLikeList().size();
        Integer parentComments= parentPost.getCommentList().size();

        if(parentPost.getVoteType()==PostVoteType.GAUGE) {
            return ParentPostDTO.builder()
                    .nickname(parentPost.getUser().getNickname())
                    .title(parentPost.getTitle())
                    .content(parentPost.getContent())
                    .gauge(parentPost.getGaugePoll().getGauge())
                    .like(parentLikes)
                    .comment(parentComments)
                    .build();
        }

        if(parentPost.getVoteType()==PostVoteType.GENERAL) {
            List<PollOptionDTO> pollOptionDTOList = parentPost.getGeneralPoll().getCandidateList().stream()
                    .map(PostConverter::toPollOptionDTO).collect(Collectors.toList());

            // 총 투표수 계산
            long totalVotes = parentPost.getGeneralPoll().getGeneralVoteList().stream()
                    .flatMap(vote -> vote.getSelect_list().stream())
                    .count();

            // 각 후보별 선택된 횟수 계산
            Map<Long, Long> candidateSelectionCounts = parentPost.getGeneralPoll().getGeneralVoteList().stream()
                    .flatMap(vote -> vote.getSelect_list().stream())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            // 선택률 -> ParentPollDTO 객체 리스트로 변환
            List<ParentPollDTO> candidateInfos = candidateSelectionCounts.entrySet().stream()
                    .map(entry -> ParentPollDTO.builder()
                            .candidateId(entry.getKey())
                            .rate((int) ((double) entry.getValue() / totalVotes * 100))
                            .selection(entry.getValue())
                            .build())
                    .collect(Collectors.toList());

            // 선택률이 가장 높은 후보 찾기
            Optional<ParentPollDTO> highestSelectionCandidate = candidateInfos.stream()
                    .max(Comparator.comparingInt(ParentPollDTO::getRate));

            // 1등 후보의 정보를 ParentPollDTO 객체로 반환
            if(highestSelectionCandidate.isPresent()) {
                parentPollDTO = highestSelectionCandidate.get();
            }


            return ParentPostDTO.builder()
                    .nickname(parentPost.getUser().getNickname())
                    .title(parentPost.getTitle())
                    .content(parentPost.getContent())
                    .pollOption(pollOptionDTOList)
                    .pollContent(parentPollDTO)
                    .like(parentLikes)
                    .comment(parentComments)
                    .build();
        }

            List<PollOptionDTO> pollOptionDTOList = parentPost.getCardPoll().getCandidateList().stream()
                    .map(PostConverter::toPollOptionDTO).collect(Collectors.toList());

            // 총 투표수 계산
            long totalVotes = parentPost.getCardPoll().getCardVoteList().stream()
                    .flatMap(vote -> vote.getSelect_list().stream())
                    .count();

            // 각 후보별 선택된 횟수 계산
            Map<Long, Long> candidateSelectionCounts = parentPost.getCardPoll().getCardVoteList().stream()
                    .flatMap(vote -> vote.getSelect_list().stream())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            // 선택률 -> ParentPollDTO 객체 리스트로 변환
            List<ParentPollDTO> candidateInfos = candidateSelectionCounts.entrySet().stream()
                    .map(entry -> ParentPollDTO.builder()
                            .candidateId(entry.getKey())
                            .rate((int) ((double) entry.getValue() / totalVotes * 100))
                            .selection(entry.getValue())
                            .build())
                    .collect(Collectors.toList());

            // 선택률이 가장 높은 후보 찾기
            Optional<ParentPollDTO> highestSelectionCandidate = candidateInfos.stream()
                    .max(Comparator.comparingInt(ParentPollDTO::getRate));

            // 1등 후보의 정보를 ParentPollDTO 객체로 반환
            parentPollDTO = highestSelectionCandidate.get();


            return ParentPostDTO.builder()
                    .nickname(parentPost.getUser().getNickname())
                    .title(parentPost.getTitle())
                    .content(parentPost.getContent())
                    .pollOption(pollOptionDTOList)
                    .pollContent(parentPollDTO)
                    .like(parentLikes)
                    .comment(parentComments)
                    .build();

    }

    public static Post toPost(PostRequestDTO.AddPostDTO request) {
        PostType postType=null;
        PostVoteType postVoteType=null;

        switch (request.getPostType()){
            case 1:
                postType=PostType.VOTE;
                break;
            case 2:
                postType=PostType.REVIEW;
                break;
            default:
                break;
        }

        if(postType== VOTE) {
            switch (request.getPostVoteType()) {
                case 1:
                    postVoteType = PostVoteType.GENERAL;
                    break;
                case 2:
                    postVoteType = PostVoteType.GAUGE;
                    break;
                case 3:
                    postVoteType = PostVoteType.CARD;
                    break;
                default:
                    break;
            }
        }

        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .file(request.getFile())
                .postType(postType)
                .voteType(postVoteType)
                .point(request.getPoint())
                .view(0)
                .state(PostState.POSTING)
                .build();
    }


    //글 상세 보기
    public static PostResponseDTO.PostDetailResponse postDetailResponse(Post post, Boolean engage,Long userId, Post parentPost){
        Integer likeCount = post.getPostLikeList().size();
        Integer commentCount = post.getCommentList().size();
        List<PollOptionDTO> userChoiceList=null;
        List<Integer> percent=null;
        List<String> voteResult=null;
        Integer value=null;
        Boolean isLike=!post.getPostLikeList().stream().filter(like->like.getUser().getId().equals(userId)).collect(Collectors.toList()).isEmpty();
        Boolean isComment=!post.getCommentList().stream().filter(like->like.getUser().getId().equals(userId)).collect(Collectors.toList()).isEmpty();


        if(post.getPostType()==REVIEW){
            return PostResponseDTO.PostDetailResponse.builder()
                    .nickname(post.getUser().getNickname())
                    .createdAt(post.getCreatedAt())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .file(post.getFile())
                    .parentPost(toParentPostDTO(parentPost))
                    .view(post.getView())
                    .like(likeCount)
                    .comment(commentCount)
                    .isLike(isLike)
                    .isComment(isComment)
                    .build();
        }
        if(post.getVoteType()==PostVoteType.GAUGE){
            if(engage){
                value=post.getGaugePoll().getGauge();
            }
            return PostResponseDTO.PostDetailResponse.builder()
                    .nickname(post.getUser().getNickname())
                    .createdAt(post.getCreatedAt())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .file(post.getFile())
                    .gauge(value)
                    .point(post.getPoint())
                    .deadline(post.getGaugePoll().getDeadline())
                    .view(post.getView())
                    .like(likeCount)
                    .comment(commentCount)
                    .isLike(isLike)
                    .isComment(isComment)
                    .build();
        }
        if(post.getVoteType()==PostVoteType.GENERAL){
            List<PollOptionDTO> pollOptionDTOList=post.getGeneralPoll().getCandidateList().stream()
                    .map(PostConverter::toPollOptionDTO).collect(Collectors.toList());
            if(engage) {
                //투표한 후보에 대한 정보
                Set<Long> selectedOptionIds = post.getGeneralPoll().getGeneralVoteList().stream()
                        .filter(generalVote -> generalVote.getUser().getId().equals(userId))
                        .flatMap(generalVote -> generalVote.getSelect_list().stream())
                        .collect(Collectors.toSet());
                userChoiceList = post.getGeneralPoll().getCandidateList().stream()
                        .filter(candidate -> selectedOptionIds.contains(candidate.getId()))
                        .map(PostConverter::toPollOptionDTO)
                        .collect(Collectors.toList());

                // 총 투표수 계산
                // 사용자가 투표한 후보의 ID
                List<Long> userSelectedCandidateIds = post.getGeneralPoll().getGeneralVoteList().stream()
                        .filter(vote -> vote.getUser().getId().equals(userId))
                        .flatMap(vote -> vote.getSelect_list().stream())
                        .collect(Collectors.toList());

                // 총 투표수 계산
                long totalVotes = post.getGeneralPoll().getGeneralVoteList().stream()
                        .flatMap(vote -> vote.getSelect_list().stream())
                        .count();

                // 각 후보별 선택된 횟수 계산
                Map<Long, Long> candidateSelectionCounts = post.getGeneralPoll().getGeneralVoteList().stream()
                        .flatMap(vote -> vote.getSelect_list().stream())
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                // 사용자가 선택한 후보의 선택률 계산
                percent = userSelectedCandidateIds.stream()
                        .map(candidateId -> {
                            long selectionCount = candidateSelectionCounts.getOrDefault(candidateId, 0L);
                            return (int) ((double) selectionCount / totalVotes * 100);
                        })
                        .collect(Collectors.toList());

                // 사용자가 선택한 후보의 투표 결과 정보 계산 (선택 인원/총 인원)
                voteResult = userSelectedCandidateIds.stream()
                        .map(candidateId -> {
                            long selectionCount = candidateSelectionCounts.getOrDefault(candidateId, 0L);
                            return String.format("%d/%d", selectionCount, totalVotes);
                        })
                        .collect(Collectors.toList());
            }
            return PostResponseDTO.PostDetailResponse.builder()
                    .nickname(post.getUser().getNickname())
                    .createdAt(post.getCreatedAt())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .file(post.getFile())
                    .pollTitle(post.getGeneralPoll().getPollTitle())
                    .pollOption(pollOptionDTOList)
                    .point(post.getPoint())
                    .deadline(post.getGeneralPoll().getDeadline())
                    .userVote(userChoiceList)
                    .percent(percent)
                    .voteResult(voteResult)
                    .view(post.getView())
                    .like(likeCount)
                    .comment(commentCount)
                    .isLike(isLike)
                    .isComment(isComment)
                    .build();
        }
        List<PollOptionDTO> pollOptionDTOList=post.getCardPoll().getCandidateList().stream()
                .map(PostConverter::toPollOptionDTO).collect(Collectors.toList());
        if(engage) {
            //투표한 후보에 대한 정보
            Set<Long> selectedOptionIds = post.getCardPoll().getCardVoteList().stream()
                    .filter(cardVote -> cardVote.getUser().getId().equals(userId))
                    .flatMap(cardVote -> cardVote.getSelect_list().stream())
                    .collect(Collectors.toSet());
            userChoiceList = post.getCardPoll().getCandidateList().stream()
                    .filter(candidate -> selectedOptionIds.contains(candidate.getId()))
                    .map(PostConverter::toPollOptionDTO)
                    .collect(Collectors.toList());

            // 총 투표수 계산
            // 사용자가 투표한 후보의 ID
            List<Long> userSelectedCandidateIds = post.getCardPoll().getCardVoteList().stream()
                    .filter(vote -> vote.getUser().getId().equals(userId))
                    .flatMap(vote -> vote.getSelect_list().stream())
                    .collect(Collectors.toList());

            // 총 투표수 계산
            long totalVotes = post.getCardPoll().getCardVoteList().stream()
                    .flatMap(vote -> vote.getSelect_list().stream())
                    .count();

            // 각 후보별 선택된 횟수 계산
            Map<Long, Long> candidateSelectionCounts = post.getCardPoll().getCardVoteList().stream()
                    .flatMap(vote -> vote.getSelect_list().stream())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            // 사용자가 선택한 후보의 선택률 계산
            percent = userSelectedCandidateIds.stream()
                    .map(candidateId -> {
                        long selectionCount = candidateSelectionCounts.getOrDefault(candidateId, 0L);
                        return (int) ((double) selectionCount / totalVotes * 100);
                    })
                    .collect(Collectors.toList());

            // 사용자가 선택한 후보의 투표 결과 정보 계산 (선택 인원/총 인원)
            voteResult = userSelectedCandidateIds.stream()
                    .map(candidateId -> {
                        long selectionCount = candidateSelectionCounts.getOrDefault(candidateId, 0L);
                        return String.format("%d/%d", selectionCount, totalVotes);
                    })
                    .collect(Collectors.toList());
        }
        return PostResponseDTO.PostDetailResponse.builder()
                .nickname(post.getUser().getNickname())
                .createdAt(post.getCreatedAt())
                .title(post.getTitle())
                .content(post.getContent())
                .file(post.getFile())
                .pollTitle(post.getCardPoll().getPollTitle())
                .pollOption(pollOptionDTOList)
                .point(post.getPoint())
                .deadline(post.getCardPoll().getDeadline())
                .userVote(userChoiceList)
                .percent(percent)
                .voteResult(voteResult)
                .view(post.getView())
                .like(likeCount)
                .comment(commentCount)
                .isLike(isLike)
                .isComment(isComment)
                .build();
    }

    //전체 보기
    public static PostResponseDTO.PollPostGetResponse pollPostGetResponse(Post post, Long userId){
        //로컬 db test
        if(post.getPostType()==VOTE&&post.getVoteType()==null){
            return PostResponseDTO.PollPostGetResponse.builder()
                    .nickname(post.getUser().getNickname())
                    .content(Long.toString(post.getId()))
                    .build();
        }

        Integer likeCount = post.getPostLikeList().size();
        Integer commentCount = post.getCommentList().size();
        List<PollOptionDTO> userChoiceList=null;
        Boolean isLike=!post.getPostLikeList().stream().filter(like->like.getUser().getId().equals(userId)).collect(Collectors.toList()).isEmpty();
        Boolean isComment=!post.getCommentList().stream().filter(like->like.getUser().getId().equals(userId)).collect(Collectors.toList()).isEmpty();
        Boolean engage=false;

        if(post.getVoteType()==PostVoteType.GENERAL) {
            List<PollOptionDTO> pollOptionDTOList=post.getGeneralPoll().getCandidateList().stream()
                    .map(PostConverter::toPollOptionDTO).collect(Collectors.toList());
            if(!post.getGeneralPoll().getGeneralVoteList().stream().filter(cardVote -> cardVote.getUser().getId().equals(userId)).collect(Collectors.toList()).isEmpty()){
                engage=true;
            }
            if(engage) {
                //투표한 후보에 대한 정보
                Set<Long> selectedOptionIds = post.getGeneralPoll().getGeneralVoteList().stream()
                        .filter(cardVote -> cardVote.getUser().getId().equals(userId))
                        .flatMap(cardVote -> cardVote.getSelect_list().stream())
                        .collect(Collectors.toSet());
                userChoiceList = post.getGeneralPoll().getCandidateList().stream()
                        .filter(candidate -> selectedOptionIds.contains(candidate.getId()))
                        .map(PostConverter::toPollOptionDTO)
                        .collect(Collectors.toList());

                return PostResponseDTO.PollPostGetResponse.builder()
                        .nickname(post.getUser().getNickname())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .uploadDate(post.getCreatedAt())
                        .pollOption(pollOptionDTOList)
                        .userVote(userChoiceList)
                        .like(likeCount)
                        .comment(commentCount)
                        .isLike(isLike)
                        .isComment(isComment)
                        .build();

            }
            return PostResponseDTO.PollPostGetResponse.builder()
                    .nickname(post.getUser().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .uploadDate(post.getCreatedAt())
                    .pollOption(pollOptionDTOList)
                    .like(likeCount)
                    .comment(commentCount)
                    .isLike(isLike)
                    .isComment(isComment)
                    .build();
        }
        if(post.getVoteType()==PostVoteType.GAUGE){
            if(!post.getGaugePoll().getGaugeVoteList().stream().filter(cardVote -> cardVote.getUser().getId().equals(userId)).collect(Collectors.toList()).isEmpty()){
                engage=true;
            }
            if(engage) {
                return PostResponseDTO.PollPostGetResponse.builder()
                        .nickname(post.getUser().getNickname())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .uploadDate(post.getCreatedAt())
                        .gauge(post.getGaugePoll().getGauge())
                        .like(likeCount)
                        .comment(commentCount)
                        .isLike(isLike)
                        .isComment(isComment)
                        .build();
            }
            return PostResponseDTO.PollPostGetResponse.builder()
                    .nickname(post.getUser().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .uploadDate(post.getCreatedAt())
                    .like(likeCount)
                    .comment(commentCount)
                    .isLike(isLike)
                    .isComment(isComment)
                    .build();
        }
        List<PollOptionDTO> pollOptionDTOList=post.getCardPoll().getCandidateList().stream()
                .map(PostConverter::toPollOptionDTO).collect(Collectors.toList());
        System.out.println("fuck");
        if(!post.getCardPoll().getCardVoteList().stream().filter(cardVote -> cardVote.getUser().getId().equals(userId)).collect(Collectors.toList()).isEmpty()){
            engage=true;
        }
        if(engage) {
            //투표한 후보에 대한 정보
            Set<Long> selectedOptionIds = post.getCardPoll().getCardVoteList().stream()
                    .filter(cardVote -> cardVote.getUser().getId().equals(userId))
                    .flatMap(cardVote -> cardVote.getSelect_list().stream())
                    .collect(Collectors.toSet());
            userChoiceList = post.getCardPoll().getCandidateList().stream()
                    .filter(candidate -> selectedOptionIds.contains(candidate.getId()))
                    .map(PostConverter::toPollOptionDTO)
                    .collect(Collectors.toList());

            return PostResponseDTO.PollPostGetResponse.builder()
                    .nickname(post.getUser().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .uploadDate(post.getCreatedAt())
                    .pollOption(pollOptionDTOList)
                    .userVote(userChoiceList)
                    .like(likeCount)
                    .comment(commentCount)
                    .isLike(isLike)
                    .isComment(isComment)
                    .build();

        }
        return PostResponseDTO.PollPostGetResponse.builder()
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .uploadDate(post.getCreatedAt())
                .pollOption(pollOptionDTOList)
                .like(likeCount)
                .comment(commentCount)
                .isLike(isLike)
                .isComment(isComment)
                .build();
    }

    public static PostResponseDTO.PollPostGetListDTO pollPostGetListDTO(Page<Post> postList,Long userId){
        List<PostResponseDTO.PollPostGetResponse> pollPostGetListDTO = postList.stream()
                .map(post->pollPostGetResponse(post,userId)).collect(Collectors.toList());
        return PostResponseDTO.PollPostGetListDTO.builder()
                .pollPostList(pollPostGetListDTO)
                .isEnd(postList.isLast())
                .build();
    }

    public static PostResponseDTO.ReviewPostGetResponse reviewPostGetResponse(Post post, Long userId){
        Integer likeCount = post.getPostLikeList().size();
        Integer commentCount = post.getCommentList().size();
        Boolean isLike=!post.getPostLikeList().stream().filter(like->like.getUser().getId().equals(userId)).collect(Collectors.toList()).isEmpty();
        Boolean isComment=!post.getCommentList().stream().filter(like->like.getUser().getId().equals(userId)).collect(Collectors.toList()).isEmpty();
        return PostResponseDTO.ReviewPostGetResponse.builder()
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .ReviewPic(post.getFile())
                .uploadDate(post.getCreatedAt())
                .like(likeCount)
                .comment(commentCount)
                .isLike(isLike)
                .isComment(isComment)
                .build();



    }

    public static PostResponseDTO.ReviewPostGetListDTO reviewPostGetListDTO(Page<Post> postList,Long userId){
        List<PostResponseDTO.ReviewPostGetResponse> reviewPostGetListDTO = postList.stream()
                .map(post->reviewPostGetResponse(post,userId)).collect(Collectors.toList());
        return PostResponseDTO.ReviewPostGetListDTO.builder()
                .reviewPostList(reviewPostGetListDTO)
                .isEnd(postList.isLast())
                .build();
    }

    public static ParentPostDTO.CandidatePostDTO candidatePostDTO(Post post){
        Integer likeCount = post.getPostLikeList().size();
        Integer commentCount = post.getCommentList().size();
        return ParentPostDTO.CandidatePostDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .like(likeCount)
                .comment(commentCount)
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static ParentPostDTO.ParentPostGetListDTO parentPostGetListDTO(Page<Post> postList,Long userId){
        List<ParentPostDTO.CandidatePostDTO> parentPostGetListDTO = postList.stream()
                .map(post->candidatePostDTO(post)).collect(Collectors.toList());
        return ParentPostDTO.ParentPostGetListDTO.builder()
                .candidatePostDTOList(parentPostGetListDTO)
                .isEnd(postList.isLast())
                .build();
    }
}


