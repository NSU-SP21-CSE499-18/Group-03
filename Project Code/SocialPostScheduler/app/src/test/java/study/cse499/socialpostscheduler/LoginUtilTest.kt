package study.cse499.socialpostscheduler

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LoginUtilTest{
    @Test
    fun testLoginEmptyInput(){
        val result = LoginUtil.validLoginInput(
            username = "",
            password = ""
        )
        assertThat(result).isFalse()
    }
    @Test
    fun testLoginMessingInput(){
        val result = LoginUtil.validLoginInput(
            username = "arif",
            password = ""
        )
        assertThat(result).isFalse()
    }
    @Test
    fun testLoginInput(){
        val result = LoginUtil.validLoginInput(
            username = "arif",
            password = "12345"
        )
        assertThat(result).isTrue()
    }

}