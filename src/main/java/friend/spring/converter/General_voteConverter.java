package friend.spring.converter;

import friend.spring.domain.General_vote;

import java.util.List;
import java.util.stream.Collectors;

public class General_voteConverter {

    public static General_vote  toGeneralVoteList(List<String> pollOptions) {
        return General_vote.builder()
                .options(pollOptions)
                .build();
    }
}