package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.converter.PostConverter;
import friend.spring.domain.*;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.repository.*;
import friend.spring.web.dto.PollOptionDTO;
import friend.spring.web.dto.PostRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static friend.spring.domain.enums.PostType.*;
import static friend.spring.domain.enums.PostVoteType.*;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final General_PollRepository generalPollRepository;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final Gauge_PollRepository gaugePollRepository;
    private final Card_PollRepository cardPollRepository;
    private final PointRepository pointRepository;
    @Override
    public void checkPost(Boolean flag) {
        if (!flag) {
            throw new UserHandler(ErrorStatus.POST_NOT_FOUND);
        }
    }

    @Override
    public Boolean checkPoint(PostRequestDTO.AddPostDTO request, User user) {
        if(request.getPoint()>user.getPoint()){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    @Override
    @Transactional
    public Post joinPost(PostRequestDTO.AddPostDTO request, Long userId) {

        
        Post newPost= PostConverter.toPost(request);
        User user=userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("\""+userId+"\"해당 유저가 없습니다"));
        newPost.setUser(user);

//일반 투표 api
        if(newPost.getPostType()==VOTE&&newPost.getVoteType()==GENERAL&&request.getPollOption()!=null){
            //포인트 차감 관련 코드
            if(request.getPoint()!=null) {
                if (!checkPoint(request, user)) {
                    throw new RuntimeException("\""+userId+"\"해당 유저의 포인트가 부족 합니다");
                }
                user.setPoint(user.getPoint() - request.getPoint());
                Point newPoint=Point.builder()
                        .amount(user.getPoint())
                        .content("일반 투표 작성에 대한 "+request.getPoint()+" 포인트 차감")
                        .build();
                newPoint.setUser(user);
                pointRepository.save(newPoint);
            }

            General_poll generalPoll = General_poll.builder()
                    .pollTitle(request.getPollTitle())
                    .deadline(request.getDeadline())
                    .build();
            if(request.getMultipleChoice()!=null){
                generalPoll.setMultipleChoice(request.getMultipleChoice());
            }
            newPost.setGeneralPoll(generalPoll);
            generalPollRepository.save(generalPoll);


            for (PollOptionDTO option : request.getPollOption()) {
                Candidate candidate = Candidate.builder()
                        .name(option.getOptionString())
                        .image(option.getOptionImg())
                        .build();

                candidate.setGeneralPoll(generalPoll);
                candidateRepository.save(candidate);
            }
        }
//카드 투표 api
        if(newPost.getPostType()==VOTE&&newPost.getVoteType()==CARD&&request.getPollOption()!=null){
            //포인트 차감 관련 코드
            if(request.getPoint()!=null) {
                if (!checkPoint(request, user)) {
                    throw new RuntimeException("\""+userId+"\"해당 유저의 포인트가 부족 합니다");
                }
                user.setPoint(user.getPoint() - request.getPoint());
                Point newPoint=Point.builder()
                        .amount(user.getPoint())
                        .content("게이지 투표 등록에 대한 "+request.getPoint()+" 포인트 차감")
                        .build();
                newPoint.setUser(user);
                pointRepository.save(newPoint);
            }

            if(!checkPoint(request, user)&&request.getPoint()!=null){
                throw new RuntimeException("\""+userId+"\"해당 유저의 포인트가 부족 합니다");
            }

            Card_poll cardPoll = Card_poll.builder()
                    .pollTitle(request.getPollTitle())
                    .deadline(request.getDeadline())
                    .build();
            if(request.getMultipleChoice()!=null){
                cardPoll.setMultipleChoice(request.getMultipleChoice());
            }
            newPost.setCardPoll(cardPoll);
            cardPollRepository.save(cardPoll);

            for (PollOptionDTO option : request.getPollOption()) {
                Candidate candidate = Candidate.builder()
                        .name(option.getOptionString())
                        .image(option.getOptionImg())
                        .build();

                candidate.setCardPoll(cardPoll);
                candidateRepository.save(candidate);
            }
        }
//게이지 투표 api
        if(newPost.getPostType()==VOTE&&newPost.getVoteType()==GAUGE){
            //포인트 차감 관련 코드
            if(request.getPoint()!=null) {
                if (!checkPoint(request, user)) {
                    throw new RuntimeException("\""+userId+"\"해당 유저의 포인트가 부족 합니다");
                }
                user.setPoint(user.getPoint() - request.getPoint());
                Point newPoint=Point.builder()
                        .amount(user.getPoint())
                        .content("카드 투표 등록에 대한 "+request.getPoint()+" 포인트 차감")
                        .build();
                newPoint.setUser(user);
                pointRepository.save(newPoint);
            }

            if(!checkPoint(request, user)&&request.getPoint()!=null){
                throw new RuntimeException("\""+userId+"\"해당 유저의 포인트가 부족 합니다");
            }

            Gauge_poll gaugePoll = Gauge_poll.builder()
                    .pollTitle(request.getPollTitle())
                    .gauge(0)
                    .deadline(request.getDeadline())
                    .build();

            newPost.setGaugePoll(gaugePoll);
            gaugePollRepository.save(gaugePoll);
        }

        if(newPost.getPostType()==REVIEW&&request.getParent_id()!=null){
            Post parent=postRepository.findById(request.getParent_id())
                            .orElseThrow(()->new RuntimeException("\""+request.getParent_id()+"\"해당 글이 없습니다"));
            newPost.setParentPost(parent);
        }

        return postRepository.save(newPost);

    }

    @Override
    @Transactional
    public void editPost(Long postId,PostRequestDTO.PostEditReq request, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post=postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        if(!user.getId().equals(post.getUser().getId())){
            throw new RuntimeException("수정 권환이 없습니다 글이 없습니다");
        }
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post=postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        if(!user.getId().equals(post.getUser().getId())){
            throw new RuntimeException("삭제 권환이 없습니다 글이 없습니다");
        }
        post.setStateDel();
    }


}
