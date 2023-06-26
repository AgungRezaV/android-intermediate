package com.dicoding.submissionone.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionone.data.DummyData
import com.dicoding.submissionone.data.response.RegisterResponse
import com.dicoding.submissionone.utils.getOrAwaitValue
import com.dicoding.submissionone.utils.StoryRepository
import com.dicoding.submissionone.utils.Result
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegisterViewModel

    @Mock
    private var repository = mock(StoryRepository::class.java)
    private val dummyRegister = DummyData.generateDummyRegisterResponse()

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(repository)
    }

    @Test
    fun whenLoginReturnSuccess() {
        val expectedUser = MutableLiveData<Result<RegisterResponse>>()
        expectedUser.value = Result.Success(dummyRegister)
        `when`(repository.repoRegister(name, email, password)).thenReturn(expectedUser)

        val actualUser = viewModel.register(name, email, password).getOrAwaitValue()
        Mockito.verify(repository).repoRegister(name, email, password)
        Assert.assertNotNull(actualUser)

        Assert.assertTrue(actualUser is Result.Success)
    }
    @Test
    fun whenLoginReturnError() {
        val expectedUser = MutableLiveData<Result<RegisterResponse>>()
        expectedUser.value = Result.Error("true")
        `when`(repository.repoRegister(name, email, password)).thenReturn(expectedUser)

        val actualUser = viewModel.register(name, email, password).getOrAwaitValue()
        Mockito.verify(repository).repoRegister(name, email, password)
        Assert.assertNotNull(actualUser)

        Assert.assertTrue(actualUser is Result.Error)
    }

    companion object {
        private const val name = "name"
        private const val email = "email"
        private const val password = "password"
    }
}