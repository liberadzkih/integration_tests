package edu.iis.mto.blog.rest.test;

import org.junit.jupiter.api.Test;

public class LikePostTest extends FunctionalTests {

    private static final String LIKE_POST_API = "/blog/user/{userId}/like/{postId}";
    
    @Test
    public void confirmedUserShouldBeAbleToLikePost(){
        
    }
    
    @Test
    public void notConfirmedUserShouldNotBeAbleToLikePost(){
        
    }
    
    @Test
    public void userShouldNotBeAbleToLikeHisOwnPost(){
        
    }
    
    @Test
    public void likingThePostTwiceShouldNotAffectOnStatus(){
        
    }
}
