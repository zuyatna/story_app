package com.zuyatna.storyapp.ui.viewmodel

import com.zuyatna.storyapp.data.remote.model.login.LoginRepository
import com.zuyatna.storyapp.data.remote.model.login.LoginResponse
import com.zuyatna.storyapp.utils.CoroutinesTestRule
import com.zuyatna.storyapp.utils.DataDummy
import com.zuyatna.storyapp.utils.NetworkResult
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var loginRepository: LoginRepository
    private lateinit var loginViewModel: LoginViewModel

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyEmail = "sam@mail.com"
    private val dummyPassword = "123456"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(loginRepository)
    }

    @Test
    fun `Login is successfully - NetworkResult Success`(): Unit = runTest {
        val expectedResponse = flowOf(NetworkResult.Success(dummyLoginResponse))

        Mockito.`when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.login(dummyEmail, dummyPassword).collect { result ->
            when(result) {
                is NetworkResult.Success -> {
                    Assert.assertTrue(true)
                    Assert.assertNotNull(result.data)
                    Assert.assertSame(result.data, dummyLoginResponse)

                }

                is NetworkResult.Error -> {
                    Assert.assertFalse(result.data!!.error)
                }
            }
        }
        Mockito.verify(loginRepository).login(dummyEmail, dummyPassword)
    }

    @Test
    fun `Login is Failed - NetworkResult Failed`(): Unit = runTest {
        val expectedResponse : Flow<NetworkResult<LoginResponse>> = flowOf(NetworkResult.Error("failed"))

        Mockito.`when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.login(dummyEmail, dummyPassword).collect { result ->
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

        Mockito.verify(loginRepository).login(dummyEmail, dummyPassword)
    }
}