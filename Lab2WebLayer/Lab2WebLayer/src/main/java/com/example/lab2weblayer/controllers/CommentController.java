package com.example.lab2weblayer.controllers;

import com.example.lab2weblayer.model.Client;
import com.example.lab2weblayer.model.Comment;
import com.example.lab2weblayer.repos.ClientRepo;
import com.example.lab2weblayer.repos.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/comments")
public class CommentController {

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private ClientRepo clientRepo;

    @PostMapping("/createComment")
    public String createComment(@RequestBody Comment comment) {
        comment.setTimestamp(LocalDateTime.now());
        commentRepo.save(comment);
        return "Comment created successfully.";
    }

    @PostMapping("/validateComment")
    public EntityModel<Comment> validateComment(@RequestBody Comment comment) {
        if (comment.getTitle() == null || comment.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Title is required.");
        }
        if (comment.getBody() == null || comment.getBody().trim().isEmpty()) {
            throw new RuntimeException("Body is required.");
        }
        if (comment.getClient() == null) {
            throw new RuntimeException("Client is required.");
        }

        Optional<Client> clientOptional = clientRepo.findById(comment.getClient().getId());
        if (clientOptional.isEmpty()) {
            throw new RuntimeException("Client not found.");
        }

        return EntityModel.of(comment,
                linkTo(methodOn(CommentController.class).getAllComments()).withRel("allComments"));
    }

    @GetMapping("/allComments")
    public Iterable<Comment> getAllComments() {
        return commentRepo.findAll();
    }

    @GetMapping("/comment/{id}")
    public EntityModel<Comment> getCommentById(@PathVariable Integer id) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID " + id));

        return EntityModel.of(comment,
                linkTo(methodOn(CommentController.class).getCommentById(id)).withSelfRel(),
                linkTo(methodOn(CommentController.class).getAllComments()).withRel("allComments"));
    }

    @PutMapping("/updateLatestComment")
    public EntityModel<Comment> updateLatestComment(@RequestBody Comment updatedComment) {
        Comment latestComment = commentRepo.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No comments found"));

        latestComment.setTitle(updatedComment.getTitle());
        latestComment.setBody(updatedComment.getBody());
        latestComment.setTimestamp(LocalDateTime.now());

        commentRepo.save(latestComment);

        return EntityModel.of(latestComment,
                linkTo(methodOn(CommentController.class).getCommentById(latestComment.getId())).withSelfRel(),
                linkTo(methodOn(CommentController.class).getAllComments()).withRel("allComments"));
    }

    @DeleteMapping("/deleteLatestComment")
    public EntityModel<Comment> deleteLatestComment() {
        Comment latestComment = commentRepo.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No comments found"));

        String debugInfo = latestComment.getCommentOwner() != null
                ? latestComment.getCommentOwner().getName()
                : "No comment owner";
        System.out.println("Deleting Comment: " + latestComment.getId() + " Owner: " + debugInfo);

        commentRepo.delete(latestComment);
        commentRepo.flush();

        return EntityModel.of(latestComment,
                linkTo(methodOn(CommentController.class).getAllComments()).withRel("allComments"));
    }

    @GetMapping("/findByChat/{chatId}")
    public List<Comment> findCommentsByChat(@PathVariable Integer chatId) {
        return commentRepo.findByChatId(chatId);
    }

    @GetMapping("/findByClient/{clientId}")
    public List<Comment> findCommentsByClient(@PathVariable Integer clientId) {
        return commentRepo.findByClientId(clientId);
    }
}
