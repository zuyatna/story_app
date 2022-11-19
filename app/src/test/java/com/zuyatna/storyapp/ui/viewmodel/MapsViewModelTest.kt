package com.zuyatna.storyapp.ui.viewmodel

import androidx.paging.ExperimentalPagingApi
import com.zuyatna.storyapp.data.remote.model.main.MainResponse
import com.zuyatna.storyapp.data.remote.model.maps.MapsRepository
import com.zuyatna.storyapp.utils.DataDummy
import com.zuyatna.storyapp.utils.NetworkResult
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @Mock
    private lateinit var mapsRepository: MapsRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyToken = DataDummy.generateDummyToken()

    @Before
    fun setup() {
        mapsViewModel = MapsViewModel(mapsRepository)
    }

    @Test
    fun `Get location story is successfully - NetworkResult Success`(): Unit = runTest {
        val expectedResponse = flowOf(NetworkResult.Success(dummyStoriesResponse))

        Mockito.`when`(mapsViewModel.getStoriesLocation(dummyToken)).thenReturn(expectedResponse)

        mapsViewModel.getStoriesLocation(dummyToken).collect { result ->
            when(result) {
                is NetworkResult.Success -> {
                    Assert.assertTrue(true)
                    Assert.assertNotNull(result.data)
                    Assert.assertSame(result.data, dummyStoriesResponse)

                }

                is NetworkResult.Error -> {
                    Assert.assertFalse(result.data!!.error)
                }
            }
        }

        Mockito.verify(mapsRepository).getStoriesLocation(dummyToken)
    }

    @Test
    fun `Get location story is failed - NetworkResult Error`(): Unit = runTest {
        val expectedResponse : Flow<NetworkResult<MainResponse>> = flowOf(NetworkResult.Error("failed"))

        Mockito.`when`(mapsViewModel.getStoriesLocation(dummyToken)).thenReturn(expectedResponse)

        mapsViewModel.getStoriesLocation(dummyToken).collect { result ->
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

        Mockito.verify(mapsRepository).getStoriesLocation(dummyToken)
    }
}