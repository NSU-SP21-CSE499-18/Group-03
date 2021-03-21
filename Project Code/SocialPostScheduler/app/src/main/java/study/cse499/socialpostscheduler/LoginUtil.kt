package study.cse499.socialpostscheduler

object LoginUtil {
    fun validLoginInput(
        username: String,
        password: String
    ): Boolean{
        if (username.isNotEmpty() && password.isNotEmpty()){
            return true;
        }
        return false;
    }
}