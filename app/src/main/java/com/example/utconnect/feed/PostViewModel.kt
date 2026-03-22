package com.example.utconnect.feed

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

data class Post(
    val id: Int,
    val user: String,
    val time: String,
    val content: String,
    val hasImage: Boolean = false,
    val imageUrl: String? = null,
    val likes: Int = 0,
    val isLiked: Boolean = false,
    val commentCount: Int = 0
)

data class Comment(
    val id: Int,
    val user: String,
    val content: String,
    val time: String
)

class PostViewModel : ViewModel() {
    private val _posts = mutableStateListOf<Post>(
        Post(
            1,
            "Jesus Adrian Cardenas Calderon",
            "7h",
            "Quiero felicitar a mi querido amigo el cual fue el ganador del torneo de Ajedrez!!!",
            hasImage = true,
            imageUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=1000&q=80"
        ),
        Post(
            2,
            "Jesus Adrian Cardenas Calderon",
            "7h",
            "Hola, alguien me puede decir si llueve?"
        ),
        Post(
            3,
            "Jesus Adrian Cardenas Calderon",
            "7h",
            "Alguien que me ayude con calculo por favor !!!!! #miedo"
        ),
        Post(
            4,
            "Andres Flores",
            "8h",
            "¿Alguien sabe a qué hora cierran la biblioteca hoy?"
        ),
        Post(
            5,
            "Roberto",
            "10h",
            "¡Ya salieron los resultados del examen de física!",
            hasImage = true,
            imageUrl = "https://images.unsplash.com/photo-1543286386-713bcd534a70?auto=format&fit=crop&w=1000&q=80"
        ),
        Post(
            6,
            "Toper",
            "12h",
            "Mañana hay partido de fútbol en las canchas de la uni, ¡caiganle!"
        ),
        Post(
            7,
            "Jesus Adrian Cardenas Calderon",
            "1d",
            "Vendo calculadora científica casi nueva, pregunten por DM."
        ),
        Post(
            8,
            "Andres Flores",
            "1d",
            "Buscando equipo para el hackathon del próximo mes."
        )
    )
    val posts: List<Post> get() = _posts

    private val _comments = mutableStateMapOf<Int, MutableList<Comment>>()

    fun addPost(content: String, hasImage: Boolean) {
        val trimmedContent = content.trim()
        if (trimmedContent.isEmpty() && !hasImage) return

        val newId = if (_posts.isEmpty()) 1 else _posts.maxOf { it.id } + 1
        val newPost = Post(
            id = newId,
            user = "Jesus Adrian Cardenas Calderon",
            time = "Justo ahora",
            content = trimmedContent,
            hasImage = hasImage,
            imageUrl = if (hasImage) "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=1000&q=80" else null
        )
        _posts.add(0, newPost) // Add at the top
    }

    fun deletePost(postId: Int) {
        _posts.removeAll { it.id == postId }
        _comments.remove(postId) // Also remove comments for that post
    }

    fun toggleLike(postId: Int) {
        val index = _posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            val post = _posts[index]
            val newIsLiked = !post.isLiked
            val newLikes = if (newIsLiked) post.likes + 1 else post.likes - 1
            _posts[index] = post.copy(isLiked = newIsLiked, likes = newLikes)
        }
    }

    fun getComments(postId: Int): List<Comment> {
        return _comments[postId] ?: emptyList()
    }

    fun addComment(postId: Int, content: String) {
        val trimmedContent = content.trim()
        if (trimmedContent.isEmpty()) return

        val postComments = _comments.getOrPut(postId) { mutableStateListOf() }
        val newId = if (postComments.isEmpty()) 1 else postComments.maxOf { it.id } + 1
        postComments.add(Comment(newId, "Yo", trimmedContent, "Ahora"))
        
        // Update comment count in post
        val index = _posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            val post = _posts[index]
            _posts[index] = post.copy(commentCount = postComments.size)
        }
    }

    fun deleteComment(postId: Int, commentId: Int) {
        val postComments = _comments[postId]
        if (postComments != null) {
            postComments.removeAll { it.id == commentId }
            
            // Update comment count in post
            val index = _posts.indexOfFirst { it.id == postId }
            if (index != -1) {
                val post = _posts[index]
                _posts[index] = post.copy(commentCount = postComments.size)
            }
        }
    }
}
