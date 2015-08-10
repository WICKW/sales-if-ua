package sales.comments.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sales.comments.domain.Comment;
import sales.comments.service.ICommentService;

import java.util.List;

@Api(basePath = "/comment", value = "comments", description = "Endpoint for comments management")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    public CommentController() {}

    @ApiOperation(httpMethod = "POST",
            value = "Add comment",
            notes = "Consumes Comment json (without commentId & date, they set automaticaly)",
            response = Comment.class)
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes="application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@ApiParam(value = "Comment object that needs to be added", required = true)
                              @RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get comments",
            notes = "Get all comments for good with %goodId%",
            response = Comment.class,
            responseContainer="List")
    @RequestMapping(value = "/{goodId}",
            method = RequestMethod.GET,
            produces = "application/json")
    public List<Comment> getCommentsByGoodId(@ApiParam(value = "int goodId - id of good", required = true)
                                             @PathVariable(value = "goodId") int goodId) {
        return commentService.getCommentsByGoodId(goodId);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Remove comments",
            notes = "Remove comment with %commentId%")
    @RequestMapping(value = "/{commentId}",
            method = RequestMethod.DELETE)
    public void removeCommentById(@ApiParam(value = "Comment object that needs to be added", required = true)
                                  @PathVariable(value = "commentId") Long commentId) {
        commentService.removeCommentById(commentId);
    }
}
