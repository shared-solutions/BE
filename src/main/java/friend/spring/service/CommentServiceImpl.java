package friend.spring.service;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.converter.CommentConverter;
import friend.spring.domain.Comment;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.repository.CommentLikeRepository;
import friend.spring.repository.CommentRepository;
import friend.spring.repository.PostRepository;
import friend.spring.repository.UserRepository;
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
    private final UserService userService;
    private final PostService postService;

    @Override
    public void checkComment(Boolean flag) {
        if (!flag) {
            throw new UserHandler(ErrorStatus.COMMENT_NOT_FOUND);
        }
    }

    @Override
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
}
