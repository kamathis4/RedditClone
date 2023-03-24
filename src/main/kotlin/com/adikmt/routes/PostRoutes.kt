package com.adikmt.routes

import com.adikmt.dtos.PostHeading
import com.adikmt.dtos.PostId
import com.adikmt.dtos.PostRequest
import com.adikmt.dtos.SerializedException
import com.adikmt.dtos.SubredditName
import com.adikmt.dtos.UserName
import com.adikmt.usecases.AddPostUsecase
import com.adikmt.usecases.DownvotePostUsecase
import com.adikmt.usecases.GetPostBySubredditUsecase
import com.adikmt.usecases.GetPostByUserUsecase
import com.adikmt.usecases.GetPostFeedByUserUsecase
import com.adikmt.usecases.GetPostUsecase
import com.adikmt.usecases.SearchPostByHeadingUsecase
import com.adikmt.usecases.UpvotePostUsecase
import com.adikmt.utils.deconstructResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Routing.postRoutes() {
    addPost()
    getPostFeed()
    getPostById()
    getPostListByHeading()
    getPostListBySubredditName()
    getPostListByUserId()
    upvotePostById()
    downvotePostById()
}

private fun Routing.getPostFeed() {
    //TODO check auth -> Then share by list as followed
    val getPostFeedByUserUsecase by inject<GetPostFeedByUserUsecase>(named("GetPostFeedByUserUsecase"))
    get("/posts") {
        try {
            //TODO get user through auth
            getPostFeedByUserUsecase.get(UserName(""))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.downvotePostById() {
    val downvotePostUsecase by inject<DownvotePostUsecase>(named("DownvotePostUsecase"))
    delete("posts/unFavourite/{postId}") {
        try {
            val postId = call.parameters["postId"]
            postId?.let {
                val data = downvotePostUsecase.downvote(UserName(""), PostId(it))
                deconstructResult(this, data, HttpStatusCode.Gone)
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.upvotePostById() {
    val upvotePostUsecase by inject<UpvotePostUsecase>(named("UpvotePostUsecase"))
    post("posts/favourite/{postId}") {
        try {
            val postId = call.parameters["postId"]
            postId?.let {
                val data = upvotePostUsecase.upvote(UserName(""), PostId(it))
                deconstructResult(this, data, HttpStatusCode.Created)
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.getPostListByUserId() {
    val getPostByUserUsecase by inject<GetPostByUserUsecase>(named("GetPostByUserUsecase"))
    get("posts/userId/{userId}") {
        try {
            val userId = call.parameters["userId"]
            userId?.let {
                val data = getPostByUserUsecase.get(UserName(it))
                deconstructResult(this, data, HttpStatusCode.OK)
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.getPostListBySubredditName() {
    val getPostBySubredditUsecase by inject<GetPostBySubredditUsecase>(named("GetPostBySubredditUsecase"))
    get("posts/subredditName/{subredditName}") {
        try {
            val subredditName = call.parameters["subredditName"]
            subredditName?.let {
                val data = getPostBySubredditUsecase.get(SubredditName(it))
                deconstructResult(this, data, HttpStatusCode.OK)
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.getPostListByHeading() {
    val searchPostByHeadingUsecase by inject<SearchPostByHeadingUsecase>(named("SearchPostByHeadingUsecase"))
    get("posts/search/{heading}") {
        try {
            val heading = call.parameters["heading"]
            heading?.let {
                val data = searchPostByHeadingUsecase.search(PostHeading(it))
                deconstructResult(this, data, HttpStatusCode.OK)
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.getPostById() {
    val getPostUseCase by inject<GetPostUsecase>(named("GetPostUsecase"))
    get("posts/id/{postId}") {
        try {
            val postId = call.parameters["postId"]
            postId?.let {
                val data = getPostUseCase.get(PostId(it))
                deconstructResult(this, data, HttpStatusCode.OK)
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.addPost() {
    val addPostUsecase by inject<AddPostUsecase>(named("AddPostUsecase"))
    post("posts/") {
        try {
            val post = call.receive<PostRequest>()
            post?.let {
                val data = addPostUsecase.add(UserName(""), post)
                deconstructResult(this, data, HttpStatusCode.Created)
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}
