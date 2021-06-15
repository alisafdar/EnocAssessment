package com.alisafdar.enocassessment.resources.network

import com.alisafdar.enocassessment.data.network.apis.AuthApi
import com.alisafdar.enocassessment.data.responses.LoginResponse
import com.alisafdar.enocassessment.resources.CoroutineTestRule
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class LoginServiceTest : ApiAbstract<AuthApi>() {

  private val email = "abc@abc.com"
  private val password = "123456"

  private lateinit var service: AuthApi

  @ExperimentalCoroutinesApi
  @get:Rule
  var coroutinesRule = CoroutineTestRule()

  @Before
  fun initService() {
    service = createService(AuthApi::class.java)
  }

  @Throws(IOException::class)
  @Test
  fun fetchPokemonListFromNetworkTest() = runBlocking {
    enqueueResponse("/LoginResponse.json")
    val response = service.login(email, password)
    mockWebServer.takeRequest()

    assertThat(response.user.id, `is`(1))
    assertThat(response.user.email, `is`(email))
    assertThat(response.user.access_token, `is`("Y2ssj235S1sfX"))
  }
}
