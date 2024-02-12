package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.converter.VoteConverter;
import friend.spring.domain.*;
import friend.spring.repository.*;
import friend.spring.web.dto.VoteRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService{
    private final UserRepository userRepository;
    private final General_VoteRepository generalVoteRepository;
    private final Gauge_VoteRepository gaugeVoteRepository;
    private final Card_VoteRepository cardVoteRepository;
    private final PostRepository postRepository;
    private final PointRepository pointRepository;
    private final Gauge_PollRepository gaugePollRepository;
    @Override
    @Transactional
    public General_vote castGeneralVote(VoteRequestDTO.GeneralVoteRequestDTO request, Long PostId, Long userId) {
        General_vote newGeneralVote = VoteConverter.toGeneralVote(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        newGeneralVote.setUser(user);

        Post post=postRepository.findById(PostId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        General_poll generalPoll=post.getGeneralPoll();
        newGeneralVote.setGeneralPoll(generalPoll);

        List<Long> selectedCandidateIds = request.getSelectList();
        List<Long> validCandidateIds = generalPoll.getCandidateList().stream()
                .map(Candidate::getId)
                .collect(Collectors.toList());
        for (Long selectedId : selectedCandidateIds) {
            if (!validCandidateIds.contains(selectedId)) {
                throw new GeneralException(ErrorStatus.CANDIDATE_NOT_FOUND);
            }
        }

        if (request.getSelectList() != null) {
            newGeneralVote.setSelect_list(request.getSelectList());
        }
        //나노초 단위로 마감 여부 확인
        LocalDateTime now = LocalDateTime.now();
        long nanosUntilDeadline = ChronoUnit.NANOS.between(now, post.getGeneralPoll().getDeadline());
        Boolean voteOnGoing = nanosUntilDeadline > 0;
        if(!voteOnGoing){
            throw new GeneralException(ErrorStatus.DEADLINE_OVER);
        }

        user.setPoint(user.getPoint() + 5);
        Point newPoint=Point.builder()
                .amount(user.getPoint())
                .content("일반 투표에 대한 "+5+" 포인트 획득")
                .build();
        newPoint.setUser(user);
        pointRepository.save(newPoint);

        return generalVoteRepository.save(newGeneralVote);
    }

    @Override
    @Transactional
    public Gauge_vote castGaugeVote(VoteRequestDTO.GaugeVoteRequestDTO request, Long PostId, Long userId){
        Gauge_vote newGaugeVote = VoteConverter.toGaugeVote(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        newGaugeVote.setUser(user);

        Post post=postRepository.findById(PostId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Gauge_poll gaugePoll=post.getGaugePoll();
        newGaugeVote.setGaugePoll(gaugePoll);

        //나노초 단위로 마감 여부 확인
        LocalDateTime now = LocalDateTime.now();
        long nanosUntilDeadline = ChronoUnit.NANOS.between(now, post.getGaugePoll().getDeadline());
        Boolean voteOnGoing = nanosUntilDeadline > 0;
        if(!voteOnGoing){
            throw new GeneralException(ErrorStatus.DEADLINE_OVER);
        }

        user.setPoint(user.getPoint() + 5);
        Point newPoint=Point.builder()
                .amount(user.getPoint())
                .content("게이지 투표에 대한 "+5+" 포인트 획득")
                .build();
        newPoint.setUser(user);
        pointRepository.save(newPoint);

        Integer value=request.getValue();
        gaugePollRepository.findById(PostId);
        Optional<Post> optionalPost=postRepository.findById(PostId);
        Post gaugePost=optionalPost.get();
        Integer currentGauge=gaugePost.getGaugePoll().getGauge();
        Integer engagedUser=gaugePost.getGaugePoll().getGaugeVoteList().size();
        gaugePost.getGaugePoll().setGauge((currentGauge+request.getValue())/engagedUser);


        return gaugeVoteRepository.save(newGaugeVote);
    }
    @Override
    @Transactional
    public Card_vote castCardVote(VoteRequestDTO.CardVoteRequestDTO request,Long PostId, Long userId){
        Card_vote newCardVote = VoteConverter.toCardVote(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        newCardVote.setUser(user);

        Post post=postRepository.findById(PostId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Card_poll cardPoll=post.getCardPoll();
        newCardVote.setCardPoll(cardPoll);

        List<Long> selectedCandidateIds = request.getSelectList();
        List<Long> validCandidateIds = cardPoll.getCandidateList().stream()
                .map(Candidate::getId)
                .collect(Collectors.toList());
        for (Long selectedId : selectedCandidateIds) {
            if (!validCandidateIds.contains(selectedId)) {
                throw new GeneralException(ErrorStatus.CANDIDATE_NOT_FOUND);
            }
        }

        if (request.getSelectList() != null) {
            newCardVote.setSelect_list(request.getSelectList());
        }

        //나노초 단위로 마감 여부 확인
        LocalDateTime now = LocalDateTime.now();
        long nanosUntilDeadline = ChronoUnit.NANOS.between(now, post.getCardPoll().getDeadline());
        Boolean voteOnGoing = nanosUntilDeadline > 0;
        if(!voteOnGoing){
            throw new GeneralException(ErrorStatus.DEADLINE_OVER);
        }

        user.setPoint(user.getPoint() + 5);
        Point newPoint=Point.builder()
                .amount(user.getPoint())
                .content("카드 투표에 대한 "+5+" 포인트 획득")
                .build();
        newPoint.setUser(user);
        pointRepository.save(newPoint);

        return cardVoteRepository.save(newCardVote);
    }

}
