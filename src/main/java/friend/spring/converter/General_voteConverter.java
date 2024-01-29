package friend.spring.converter;

import friend.spring.domain.Candidate;
import friend.spring.domain.General_poll;
import friend.spring.web.dto.PollOptionDTO;

import java.util.List;

public class General_voteConverter {

    public static General_poll toGeneralVoteList(List<Candidate> pollOptions) {
        return General_poll.builder()
                .candidateList(pollOptions)
                .build();
    }
}