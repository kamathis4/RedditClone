package com.adikmt.routes

import com.adikmt.dtos.CommentId
import com.adikmt.dtos.CommentRequest
import com.adikmt.dtos.CommentResponse
import com.adikmt.dtos.PostId
import com.adikmt.dtos.SerializedException
import com.adikmt.dtos.UserName
import com.adikmt.usecases.AddCommentUsecase
import com.adikmt.usecases.DownvoteCommentUsecase
import com.adikmt.usecases.GetAllCommentByUserUsecase
import com.adikmt.usecases.GetAllCommentsByPostUsecase
import com.adikmt.usecases.GetCommentUsecase
import com.adikmt.usecases.UpvoteCommentUsecase
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

fun Routing.commentRoutes() {
    addComment()
    getCommentById()
    getCommentsByPost()
    getCommentByUserId()
    upvoteComment()
    downvoteComment()
}

private fun Routing.downvoteComment() {
    val downvoteCommentUsecase by inject<DownvoteCommentUsecase>(named("DownvoteCommentUsecase"))
    delete("/comments/downvote/{commentId}") {
        try {
            val commentId = call.parameters["commentId"]
            commentId?.let {
                downvoteCommentUsecase.downvote(UserName(""), CommentId(it))
                call.respond(HttpStatusCode.Gone, CommentId(it))
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.upvoteComment() {
    val upvoteCommentUsecase by inject<UpvoteCommentUsecase>(named("UpvoteCommentUsecase"))
    post("/comments/upvote/{commentId}") {
        try {
            val commentId = call.parameters["commentId"]
            commentId?.let {
                upvoteCommentUsecase.upvote(UserName(""), CommentId(it))
                call.respond(HttpStatusCode.Gone, CommentId(it))
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.getCommentByUserId() {
    val getAllCommentByUserUsecase by inject<GetAllCommentByUserUsecase>(named("GetAllCommentByUserUsecase"))
    get("/comments/userId/{userId}") {
        try {
            val userId = call.parameters["userId"]
            userId?.let {
                getAllCommentByUserUsecase.get(UserName(it))
                call.respond(HttpStatusCode.Gone, CommentId(it))
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.getCommentById() {
    val getCommentUsecase by inject<GetCommentUsecase>(named("GetCommentUsecase"))
    get("/comments/id/{commentId}") {
        try {
            val commentId = call.parameters["commentId"]
            commentId?.let {
                getCommentUsecase.get(CommentId(it))
                call.respond(HttpStatusCode.Gone, CommentId(it))
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.getCommentsByPost() {
    val getAllCommentsByPostUsecase by inject<GetAllCommentsByPostUsecase>(named("GetAllCommentsByPostUsecase"))
    get("/comments/postId/{postId}") {
        try {
            val postId = call.parameters["postId"]
            postId?.let {
                getAllCommentsByPostUsecase.get(PostId(it))
                call.respond(HttpStatusCode.Gone, CommentId(it))
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}

private fun Routing.addComment() {
    val addCommentUsecase by inject<AddCommentUsecase>(named("AddCommentUsecase"))
    post("/comments") {
        try {
            val comment = call.receive<CommentRequest>()
            comment?.let {
                addCommentUsecase.add(it)
                call.respond(HttpStatusCode.Gone, CommentResponse)
            }
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SerializedException(e.message))
        }
    }
}
