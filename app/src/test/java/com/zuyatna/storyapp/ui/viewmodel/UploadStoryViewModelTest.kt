package com.zuyatna.storyapp.ui.viewmodel

import androidx.paging.ExperimentalPagingApi
import com.zuyatna.storyapp.data.remote.model.upload.UploadStoryRepository
import com.zuyatna.storyapp.data.remote.model.upload.UploadStoryResponse
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
class UploadStoryViewModelTest {
    @Mock
    private lateinit var uploadStoryRepository: UploadStoryRepository

    @Mock
    private lateinit var uploadStoryViewModel: UploadStoryViewModel
    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyUploadResponse = DataDummy.generateDummyFileUploadResponse()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        uploadStoryViewModel = UploadStoryViewModel(uploadStoryRepository)
    }

    @Test
    fun `Upload file successfully`() = runTest {
        val expectedResponse = flowOf(NetworkResult.Success(dummyUploadResponse))

        Mockito.`when`(
            uploadStoryViewModel.uploadStory(
                dummyToken,
                dummyDescription,
                "",
                "",
                dummyMultipart
            )
        ).thenReturn(expectedResponse)

        uploadStoryRepository.uploadStory(dummyToken, dummyDescription, "", "", dummyMultipart)
            .collect { result ->
                when(result){
                    is NetworkResult.Success -> {
                        Assert.assertNotNull(result.data)
                        Assert.assertTrue(true)
                        Assert.assertSame(dummyUploadResponse, result.data)
                    }

                    is NetworkResult.Error -> Assert.assertFalse(result.data!!.error)
                }
            }

        Mockito.verify(uploadStoryRepository).uploadStory(dummyToken, dummyDescription, "", "", dummyMultipart)
    }

    @Test
    fun `Upload file Failed`() = runTest {
        val expectedResponse : Flow<NetworkResult<UploadStoryResponse>> = flowOf(NetworkResult.Error("failed"))

        Mockito.`when`(
            uploadStoryViewModel.uploadStory(
                dummyToken,
                dummyDescription,
                "",
                "",
                dummyMultipart
            )
        ).thenReturn(expectedResponse)

        uploadStoryRepository.uploadStory(dummyToken, dummyDescription, "", "", dummyMultipart)
            .collect { result ->
                when(result){
                    is NetworkResult.Success -> {
                        Assert.assertTrue(false)
                        Assert.assertFalse(result.data!!.error)
                    }

                    is NetworkResult.Error -> {
                        Assert.assertNotNull(result.message)
                    }
                }
            }

        Mockito.verify(uploadStoryRepository).uploadStory(dummyToken, dummyDescription, "", "", dummyMultipart)
    }
}