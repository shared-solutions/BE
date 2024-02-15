package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.CommentHandler;
import friend.spring.converter.CommentConverter;
import friend.spring.domain.Comment;
import friend.spring.domain.Point;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.repository.*;
import friend.spring.security.JwtTokenProvider;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static friend.spring.apiPayload.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentChoiceRepository commentChoiceRepository;
    private final PointRepository pointRepository;
    private final UserService userService;
    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void checkComment(Boolean flag) {
        if (!flag) {
            throw new CommentHandler(COMMENT_NOT_FOUND);
        }
    }

    @Override
    public void checkCommentLike(Boolean flag) {
        if (!flag) {
            throw new CommentHandler(COMMENT_LIKE_NOT_FOUND);
        } else {
            throw new CommentHandler(COMMENT_LIKE_DUPLICATE);
        }
    }

    @Override
    public void checkCommentChoice(Boolean flag) {
        if (!flag) {
            throw new CommentHandler(COMMENT_CHOICE_OVER_ONE);
        }
    }

    @Override
    public void checkSelectCommentAnotherUser(Boolean flag) {
        if (!flag) {
            throw new CommentHandler(COMMENT_SELECT_MYSELF);
        }
    }

    @Override
    public void checkCommentWriterUser(Boolean flag) {
        if (!flag) {
            throw new CommentHandler(COMMENT_NOT_CORRECT_USER);
        }
    }

    @Override
    @Transactional
    public Comment createComment(Long postId, CommentRequestDTO.commentCreateReq requestBody, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

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
        if (requestBody.getParentId() != null) {
            Optional<Comment> optionalParentComment = commentRepository.findById(requestBody.getParentId());
            if (optionalParentComment.isEmpty()) {
                this.checkComment(false);
            }

            parentComment = optionalParentComment.get();
        }

        Comment comment = CommentConverter.toComment(requestBody, post, user, parentComment);

        return commentRepository.save(comment);
    }

    @Override
    public Comment_like likeComment(Long postId, Long commentId, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

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

        // 댓글 소속이 글과 일치하는지 확인
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new CommentHandler(COMMENT_POST_NOT_MATCH);
        }

        Optional<Comment_like> optionalComment_like = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        if (!optionalComment_like.isEmpty()) {
            this.checkCommentLike(true);
        }

        Comment_like comment_like = CommentConverter.toCommentLike(post, comment, user);
        return commentLikeRepository.save(comment_like);
    }

    @Override
    public List<CommentResponseDTO.commentGetRes> getComments(Long postId, HttpServletRequest request) {
        Long loginUserId = jwtTokenProvider.getCurrentUser(request);
        Optional<User> optionalUser = userRepository.findById(loginUserId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            postService.checkPost(false);
        }

        List<Comment> commentList = commentRepository.findByPostIdAndParentCommentIsNull(postId); // 루트 댓글만 가져옴
        List<CommentResponseDTO.commentGetRes> commentGetResList = new ArrayList<>();
        for (Comment comment : commentList) {
            // 대댓글 처리
            List<CommentResponseDTO.commentGetRes> subComments = new ArrayList<>();
            if (comment.getSubCommentList() != null) {
                for (Comment c : comment.getSubCommentList()) {
                    Boolean isPushedLike_sub = checkIsPushedLike(comment, loginUserId);
                    Boolean isOwnerOfPost_sub = checkIsOwnerOfPost(comment, loginUserId);
                    CommentResponseDTO.commentGetRes subCommentGetRes = CommentConverter.toCommentGetRes(comment, loginUserId, isPushedLike_sub, isOwnerOfPost_sub, new ArrayList<>());
                    subComments.add(subCommentGetRes);
                }
            }

            Boolean isPushedLike = checkIsPushedLike(comment, loginUserId);
            Boolean isOwnerOfPost = checkIsOwnerOfPost(comment, loginUserId);

            CommentResponseDTO.commentGetRes commentGetRes = CommentConverter.toCommentGetRes(comment, loginUserId, isPushedLike, isOwnerOfPost, subComments);
            commentGetResList.add(commentGetRes);
        }

        return commentGetResList;
    }

    public Boolean checkIsPushedLike(Comment comment, Long loginUserId) {
        // 좋아요 이미 눌렀는지 여부
        Optional<Comment_like> optionalComment_like = commentLikeRepository.findByCommentIdAndUserId(comment.getId(), loginUserId);
        Boolean isPushedLike;
        if (optionalComment_like.isEmpty()) {
            isPushedLike = false;
        } else {
            isPushedLike = true;
        }
        return isPushedLike;
    }

    public Boolean checkIsOwnerOfPost(Comment comment, Long loginUserId) {
        // 내가 쓴 글인지 여부
        Boolean isOwnerOfPost;
        if (Objects.equals(comment.getPost().getUser().getId(), loginUserId)) {
            isOwnerOfPost = true;
        } else {
            isOwnerOfPost = false;
        }
        return isOwnerOfPost;
    }

    @Override
    public void dislikeComment(Long postId, Long commentId, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            postService.checkPost(false);
        }

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            this.checkComment(false);
        }

        // 댓글 소속이 글과 일치하는지 확인
        if (!Objects.equals(optionalComment.get().getPost().getId(), optionalPost.get().getId())) {
            throw new CommentHandler(COMMENT_POST_NOT_MATCH);
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
    public Comment_choice selectComment(Long postId, Long commentId, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            postService.checkPost(false);
        }

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            this.checkComment(false);
        }

        // 댓글 소속이 글과 일치하는지 확인
        if (!Objects.equals(optionalComment.get().getPost().getId(), optionalPost.get().getId())) {
            throw new CommentHandler(COMMENT_POST_NOT_MATCH);
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

        // 채택된 사용자에게 포인트 적립
        comment.getUser().setPoint(comment.getUser().getPoint() + post.getPoint());
        Point newPoint = Point.builder()
                .amount(post.getPoint())
                .content("채택된 댓글에 대한 " + post.getPoint() + " 포인트 적립")
                .build();
        newPoint.setUser(user);
        pointRepository.save(newPoint);

        return commentChoiceRepository.save(comment_choice);
    }

    @Override
    @Transactional
    public void editComment(Long postId, Long commentId, CommentRequestDTO.commentEditReq requestBody, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 댓글 소속이 글과 일치하는지 확인
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new CommentHandler(COMMENT_POST_NOT_MATCH);
        }

        // 로그인한 사용자가 이 댓글의 작성자인지 확인
        if (!Objects.equals(user.getId(), comment.getUser().getId())) {
            // 작성자가 아닌 경우 -> 에러 반환
            this.checkCommentWriterUser(false);
        }
        comment.update(requestBody.getContent());
    }

    //한 유저의 모든 댓글
    @Override
    public Page<Comment> getMyCommentList(Long userId, Integer page) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            userService.checkUser(false);
        }
        User user = optionalUser.get();
        Page<Comment> userPage = commentRepository.findAllByUser(user, PageRequest.of(page, 5));
        return userPage;
    }

    @Override
    @Transactional
    public void deleteComment(Long postId, Long commentId, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 댓글 소속이 글과 일치하는지 확인
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new CommentHandler(COMMENT_POST_NOT_MATCH);
        }

        // 로그인한 사용자가 이 댓글의 작성자인지 확인
        if (!Objects.equals(user.getId(), comment.getUser().getId())) {
            // 작성자가 아닌 경우 -> 에러 반환
            this.checkCommentWriterUser(false);
        }
        comment.updateStateToDeleted();
    }
}

