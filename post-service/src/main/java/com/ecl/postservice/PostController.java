package com.ecl.postservice;

import com.ecl.postservice.model.Post;
import com.ecl.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/post/add")
    public Post addPost(@RequestBody Post post) {
        return postService.addPost(post);
    }

    @GetMapping("/post/list")
    public List<Post> getAllPosts() {
        return postService.getAllPost();
    }

    @GetMapping("/post/{id}")
    public Post getPostById(@PathVariable int id) {
        return postService.getPostById(id);
    }

}
