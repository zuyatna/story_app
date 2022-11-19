package com.zuyatna.storyapp.data.remote.model.register

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
class RegisterRepositoryTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var registerSource: RegisterSource
    private lateinit var registerRepository: RegisterRepository

    private val dummyName = "Antasuy22"
    private val dummyEmail = "antasuy22@gmail.com"
    private val dummyPassword = "antasuy22"

    @Before
    fun setup() {
        registerRepository = RegisterRepository(registerSource)
    }

    @Test
    fun `Register successfully`(): Unit = runTest {
        val expectedResponse = DataDummy.generateDummyRegisterResponse()

        Mockito.`when`(registerSource.register(dummyName, dummyEmail, dummyPassword))
            .thenReturn(Response.success(expectedResponse))

        registerRepository.register(dummyName, dummyEmail, dummyPassword).collect { result ->

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
    fun `User register failed`(): Unit = runTest {
        Mockito.`when`(
            registerSource.register(dummyName, dummyEmail, dummyPassword)
        ).then { throw Exception() }

        registerRepository.register(dummyName, dummyEmail, dummyPassword).collect { result ->
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