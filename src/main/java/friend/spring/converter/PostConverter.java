package friend.spring.converter;
import friend.spring.domain.*;
import friend.spring.domain.enums.PostCategory;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.web.dto.PollOptionDTO;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static friend.spring.domain.enums.PostType.*;

public class PostConverter {

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



    public static Post toPost(PostRequestDTO.AddPostDTO request) {
        PostType postType=null;
        PostVoteType postVoteType=null;
        PostCategory category=null;

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

        switch (request.getCategory()){
            case 1:
                category=PostCategory.EDUCATION;
                break;
            case 2:
                category=PostCategory.ENTERTAINMENT;
                break;
            case 3:
                category=PostCategory.LIFESTYLE;
                break;
            case 4:
                category=PostCategory.ECONOMY;
                break;
            case 5:
                category=PostCategory.SHOPPING;
                break;
            case 6:
                category=PostCategory.OTHERS;
                break;
            default:
                break;
        }


        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .postType(postType)
                .category(category)
                .voteType(postVoteType)
                .point(request.getPoint())
                .view(0)
                .state(PostState.POSTING)
                .build();
    }

    public static PostResponseDTO.PostDetailResponse postDetailResponse(Post post, Boolean engage,Long userId){
        Integer likeCount = post.getPostLikeList().size();
        Integer commentCount = post.getCommentList().size();
        List<PollOptionDTO> userChoiceList=null;
        List<Integer> percent=null;
        List<String> voteResult=null;
        Integer value=null;


        if(post.getPostType()==REVIEW){
            return PostResponseDTO.PostDetailResponse.builder()
                    .nickname(post.getUser().getNickname())
                    .createdAt(post.getCreatedAt())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .parentId(post.getParentPost().getId())
                    .view(post.getView())
                    .like(likeCount)
                    .comment(commentCount)
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
                    .gauge(value)
                    .point(post.getPoint())
                    .deadline(post.getGaugePoll().getDeadline())
                    .view(post.getView())
                    .like(likeCount)
                    .comment(commentCount)
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
                .build();
    }
}


