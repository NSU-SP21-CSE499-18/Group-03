package study.cse499.socialpostscheduler.repository

interface AuthRepository {
    fun signIn(token: String): Boolean
}