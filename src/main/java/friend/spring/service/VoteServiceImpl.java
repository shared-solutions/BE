package friend.spring.service;

import friend.spring.converter.VoteConverter;
import friend.spring.domain.*;
import friend.spring.repository.*;
import friend.spring.web.dto.VoteRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService{
    private final UserRepository userRepository;
    private final General_VoteRepository generalVoteRepository;
    private final Gauge_VoteRepository gaugeVoteRepository;
    private final Card_VoteRepository cardVoteRepository;
    private final PostRepository postRepository;
    @Override
    @Transactional
    public General_vote castGeneralVote(VoteRequestDTO.GeneralVoteRequestDTO request, Long userId) {
        General_vote newGeneralVote = VoteConverter.toGeneralVote(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("\"" + userId + "\"해당 유저가 없습니다"));
        newGeneralVote.setUser(user);

        Post post=postRepository.findById(request.getPostId())
                .orElseThrow(()-> new RuntimeException("\"" + request.getPostId() + "\"해당 글이 없습니다"));

        General_poll generalPoll=post.getGeneralPoll();
        newGeneralVote.setGeneralPoll(generalPoll);

        List<Long> selectedCandidateIds = request.getSelectList();
        List<Long> validCandidateIds = generalPoll.getCandidateList().stream()
                .map(Candidate::getId)
                .collect(Collectors.toList());
        for (Long selectedId : selectedCandidateIds) {
            if (!validCandidateIds.contains(selectedId)) {
                throw new RuntimeException("해당 후보 ID가 존재하지 않습니다: " + selectedId);
            }
        }

        if (request.getSelectList() != null) {
            newGeneralVote.setSelect_list(request.getSelectList());
        }
        return generalVoteRepository.save(newGeneralVote);
    }

    @Override
    @Transactional
    public Gauge_vote castGaugeVote(VoteRequestDTO.GaugeVoteRequestDTO request, Long userId){
        Gauge_vote newGaugeVote = VoteConverter.toGaugeVote(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("\"" + userId + "\"해당 유저가 없습니다"));
        newGaugeVote.setUser(user);

        Post post=postRepository.findById(request.getPostId())
                .orElseThrow(()-> new RuntimeException("\"" + request.getPostId() + "\"해당 글이 없습니다"));

        Gauge_poll gaugePoll=post.getGaugePoll();
        newGaugeVote.setGaugePoll(gaugePoll);

        return gaugeVoteRepository.save(newGaugeVote);
    }
    @Override
    @Transactional
    public Card_vote castCardVote(VoteRequestDTO.CardVoteRequestDTO request, Long userId){
        Card_vote newCardVote = VoteConverter.toCardVote(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("\"" + userId + "\"해당 유저가 없습니다"));
        newCardVote.setUser(user);

        Post post=postRepository.findById(request.getPostId())
                .orElseThrow(()-> new RuntimeException("\"" + request.getPostId() + "\"해당 글이 없습니다"));

        Card_poll cardPoll=post.getCardPoll();
        newCardVote.setCardPoll(cardPoll);

        List<Long> selectedCandidateIds = request.getSelectList();
        List<Long> validCandidateIds = cardPoll.getCandidateList().stream()
                .map(Candidate::getId)
                .collect(Collectors.toList());
        for (Long selectedId : selectedCandidateIds) {
            if (!validCandidateIds.contains(selectedId)) {
                throw new RuntimeException("해당 후보 ID가 존재하지 않습니다: " + selectedId);
            }
        }

        if (request.getSelectList() != null) {
            newCardVote.setSelect_list(request.getSelectList());
        }
        return cardVoteRepository.save(newCardVote);
    }

}
