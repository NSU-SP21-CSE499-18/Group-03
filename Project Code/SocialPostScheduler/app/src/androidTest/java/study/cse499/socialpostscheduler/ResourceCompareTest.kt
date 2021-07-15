package study.cse499.socialpostscheduler

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ResourceCompareTest {
    private lateinit var  resoruceComparer: ResourceCompare

    @Before
    fun setup(){
        resoruceComparer = ResourceCompare()
    }
    @Test
    fun stringResourceSameAsGivenString_returnTrue(){
        resoruceComparer = ResourceCompare()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resoruceComparer.isEqual(context, R.string.app_name, "SocialPostScheduler")
        assertThat(result).isTrue();
    }

    @Test
    fun stringResourceDifferentAsGivenString_returnFalse(){
        resoruceComparer = ResourceCompare()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resoruceComparer.isEqual(context, R.string.app_name, "SocialPostSchedulerr")
        assertThat(result).isFalse();
    }

}