package com.zuyatna.storyapp.ui.viewmodel

import com.zuyatna.storyapp.data.remote.model.register.RegisterRepository
import com.zuyatna.storyapp.data.remote.model.register.RegisterResponse
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
class RegisterViewModelTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var registerViewModel: RegisterViewModel

    @Mock
    private lateinit var registerRepository: RegisterRepository

    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyName = "Antasuy22"
    private val dummyEmail = "antasuy22@mail.com"
    private val dummyPassword = "antasuy22"

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(registerRepository)
    }

    @Test
    fun `Register is successfully - NetworkResult Success`(): Unit = runTest {
        val expectedResponse = flowOf(NetworkResult.Success(dummyRegisterResponse))

        Mockito.`when`(registerRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        registerViewModel.register(dummyName, dummyEmail, dummyPassword).collect { result ->
            Assert.assertNotNull(result.data)
            Assert.assertSame(result.data, dummyRegisterResponse)
        }

        Mockito.verify(registerRepository).register(dummyName, dummyEmail, dummyPassword)
    }

    @Test
    fun `Register is Failed - NetworkResult Failed`(): Unit = runTest {
        val expectedResponse : Flow<NetworkResult<RegisterResponse>> = flowOf(NetworkResult.Error("failed"))

        Mockito.`when`(registerRepository.register(dummyName,dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        registerViewModel.register(dummyName, dummyEmail, dummyPassword).collect { result ->
            Assert.assertNotNull(result.message)
        }

        Mockito.verify(registerRepository).register(dummyName,dummyEmail, dummyPassword)
    }
}