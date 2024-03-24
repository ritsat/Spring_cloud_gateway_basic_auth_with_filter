package com.ecl.postservice.service;

import com.ecl.postservice.model.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private List<Post> postList = new ArrayList(){
        {
            add(new Post(1, "First Post", "It is my first post"));
            add(new Post(2, "Second Post", "It is my second post"));
        }
    };


    public Post addPost(Post post){
        post.setPostId(postList.size()+1);
        postList.add(post);
        return post;
    }

    public List<Post> getAllPost() {
        return postList;
    }

    public Post getPostById(int postId) {
        return postList.stream().filter(post -> post.getPostId() == postId).findFirst().get();
    }

}
