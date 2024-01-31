package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.CommentHandler;
import friend.spring.converter.CommentConverter;
import friend.spring.domain.Comment;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.repository.*;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentChoiceRepository commentChoiceRepository;
    private final UserService userService;
    private final PostService postService;

    @Override
    public void checkComment(Boolean flag) {
        if (!flag) {
            throw new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND);
        }
    }

    @Override
    public void checkCommentLike(Boolean flag) {
        if (!flag) {
            throw new CommentHandler(ErrorStatus.COMMENT_LIKE_NOT_FOUND);
        }
    }

    @Override
    public void checkCommentChoice(Boolean flag) {
        if (!flag) {
            throw new CommentHandler(ErrorStatus.COMMENT_CHOICE_OVER_ONE);
        }
    }

    @Override
    public void checkSelectCommentAnotherUser(Boolean flag) {
        if (!flag) {
            throw new CommentHandler(ErrorStatus.COMMENT_SELECT_MYSELF);
        }
    }

    @Override
    public void checkCommentWriterUser(Boolean flag) {
        if (!flag) {
            throw new CommentHandler(ErrorStatus.COMMENT_NOT_CORRECT_USER);
        }
    }

    @Override
    @Transactional
    public Comment createComment(Long postId, CommentRequestDTO.commentCreateReq request, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            postService.checkPost(false);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Comment parentComment = null;
        Post post = optionalPost.get();
        User user = optionalUser.get();

        // 대댓글인 경우
        if (request.getParentId() != null) {
            Optional<Comment> optionalParentComment = commentRepository.findById(request.getParentId());
            if (optionalParentComment.isEmpty()) {
                this.checkComment(false);
            }

            parentComment = optionalParentComment.get();
        }

        Comment comment = CommentConverter.toComment(request, post, user, parentComment);

        return commentRepository.save(comment);
    }

    @Override
    public Comment_like likeComment(Long postId, Long commentId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            postService.checkPost(false);
        }

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            this.checkComment(false);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Post post = optionalPost.get();
        Comment comment = optionalComment.get();
        User user = optionalUser.get();

        Comment_like comment_like = CommentConverter.toCommentLike(post, comment, user);
        return commentLikeRepository.save(comment_like);
    }

    @Override
    public Page<CommentResponseDTO.commentGetRes> getComments(Long postId, Integer page, Integer size) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            postService.checkPost(false);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByPostIdAndParentCommentIsNull(postId, pageable); // 루트 댓글만 가져옴
        List<CommentResponseDTO.commentGetRes> commentGetResList = commentPage
                .map(comment -> {
                    CommentResponseDTO.commentGetRes commentGetRes = CommentConverter.toCommentGetRes(comment);
                    return commentGetRes;
                })
                .filter(Objects::nonNull) // null인 요소는 필터링
                .get()
                .collect(Collectors.toList());

        return new PageImpl<>(commentGetResList, pageable, commentPage.getTotalElements());
    }

    @Override
    public void dislikeComment(Long postId, Long commentId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            postService.checkPost(false);
        }

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            this.checkComment(false);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Optional<Comment_like> optionalComment_like = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        if (optionalComment_like.isEmpty()) {
            this.checkCommentLike(false);
        }

        Comment_like comment_like = optionalComment_like.get();
        commentLikeRepository.delete(comment_like);
    }

    @Override
    public Comment_choice selectComment(Long postId, Long commentId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            postService.checkPost(false);
        }

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            this.checkComment(false);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        // 이 사용자가 존재하는지 확인
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Post post = optionalPost.get();
        Comment comment = optionalComment.get();
        User user = optionalUser.get();

        // 로그인한 사용자가 이 글의 작성자인지 확인
        if (!Objects.equals(user.getId(), post.getUser().getId())) {
            // 작성자가 아닌 경우 -> 에러 반환
            postService.checkPostWriterUser(false);
        }

        // 자기 자신을 채택했는지 확인
        if (Objects.equals(user.getId(), comment.getUser().getId())) {
            this.checkSelectCommentAnotherUser(false);
        }

        Optional<Comment_choice> optionalComment_choice = commentChoiceRepository.findByPostId(postId);
        if (!optionalComment_choice.isEmpty()) { // 이미 1명 채택을 한 상태이므로 에러로 반환
            this.checkCommentChoice(false);
        }

        Comment_choice comment_choice = CommentConverter.toCommentChoice(post, comment);
        return commentChoiceRepository.save(comment_choice);
    }

    @Override
    @Transactional
    public void editComment(Long postId, Long commentId, CommentRequestDTO.commentEditReq request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
        // 로그인한 사용자가 이 댓글의 작성자인지 확인
        if (!Objects.equals(user.getId(), comment.getUser().getId())) {
            // 작성자가 아닌 경우 -> 에러 반환
            this.checkCommentWriterUser(false);
        }
        comment.update(request.getContent());
    }
}
