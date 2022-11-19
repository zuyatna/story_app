package com.zuyatna.storyapp.data.remote.model.upload

import com.zuyatna.storyapp.utils.CoroutinesTestRule
import com.zuyatna.storyapp.utils.DataDummy
import com.zuyatna.storyapp.utils.NetworkResult
import junit.framework.Assert
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
class UploadStoryRepositoryTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var uploadStorySource: UploadStorySource
    private lateinit var uploadStoryRepository: UploadStoryRepository
    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()

    @Before
    fun setup() {
        uploadStoryRepository = UploadStoryRepository(uploadStorySource)
    }

    @Test
    fun `Upload image file - successfully`() = runTest {
        val expectedResponse = DataDummy.generateDummyFileUploadResponse()

        Mockito.`when`(
            uploadStorySource.uploadStory(
                "Bearer $dummyToken",
                "text",
                "",
                "",
                dummyMultipart
            )
        ).thenReturn(Response.success(expectedResponse))

        uploadStoryRepository.uploadStory(dummyToken, "text", "", "",dummyMultipart)
            .collect { result ->
                when(result){
                    is NetworkResult.Success -> {
                        Assert.assertNotNull(result.data)
                        Assert.assertTrue(true)
                    }

                    is NetworkResult.Error -> {}
                }
            }

        Mockito.verify(uploadStorySource)
            .uploadStory(
                "Bearer $dummyToken",
                "text",
                "",
                "",
                dummyMultipart
            )
    }

    @Test
    fun `Upload image file - throw exception`() = runTest {

        Mockito.`when`(
            uploadStorySource.uploadStory(
                "Bearer $dummyToken",
                dummyDescription,
                "",
                "",
                dummyMultipart
            )
        ).then { throw Exception() }

        uploadStoryRepository.uploadStory(dummyToken, dummyDescription, "", "",dummyMultipart)
            .collect { result ->
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

        Mockito.verify(uploadStorySource).uploadStory(
            "Bearer $dummyToken",
            dummyDescription,
            "",
            "",
            dummyMultipart
        )
    }
}