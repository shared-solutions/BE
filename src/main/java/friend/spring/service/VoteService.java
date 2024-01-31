package friend.spring.service;

import friend.spring.domain.General_vote;
import friend.spring.domain.Post;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.VoteRequestDTO;

public interface VoteService {

    General_vote castVote(VoteRequestDTO.GeneralVoteRequestDTO request, Long userId);
}
