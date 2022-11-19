package com.zuyatna.storyapp.data.remote.model.login

import com.zuyatna.storyapp.utils.CoroutinesTestRule
import com.zuyatna.storyapp.utils.DataDummy
import com.zuyatna.storyapp.utils.NetworkResult
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginRepositoryTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var loginSource: LoginSource
    private lateinit var loginRepository: LoginRepository

    private val dummyEmail = "antasuy22@gmail.com"
    private val dummyPassword = "antasuy22"

    @Before
    fun setup() {
        loginRepository = LoginRepository(loginSource)
    }

    @Test
    fun `Login successfully`(): Unit = runTest {
        val expectedResponse = DataDummy.generateDummyLoginResponse()

        Mockito.`when`(loginSource.login(dummyEmail, dummyPassword)).thenReturn(Response.success(expectedResponse))

        loginRepository.login(dummyEmail, dummyPassword).collect { result ->

            when(result) {
                is NetworkResult.Success -> {
                    Assert.assertTrue(true)
                    Assert.assertNotNull(result.data)
                    assertEquals(expectedResponse, result.data)
                }
                is NetworkResult.Error -> {
                    Assert.assertFalse(result.data!!.error)
                    Assert.assertNull(result)
                }
            }
        }
    }

    @Test
    fun `Login Failed`(): Unit = runTest {
        Mockito.`when`(
            loginSource.login(dummyEmail, dummyPassword)
        ).then { throw Exception() }

        loginRepository.login(dummyEmail, dummyPassword).collect { result ->
            when(result) {
                is NetworkResult.Success -> {
                    Assert.assertTrue(false)
                    Assert.assertFalse(result.data!!.error)
                }
                is NetworkResult.Error -> {
                    Assert.assertNotNull(result.message)
                }
            }
        }
    }
}