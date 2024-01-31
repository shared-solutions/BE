package friend.spring.converter;

import friend.spring.domain.Candidate;
import friend.spring.domain.General_poll;

import java.util.List;

public class General_PollConverter {

    public static General_poll toGeneralPollList(List<Candidate> pollOptions) {
        return General_poll.builder()
                .candidateList(pollOptions)
                .build();
    }
}