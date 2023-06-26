package com.dicoding.submissionone.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionone.data.response.LoginResponse
import com.dicoding.submissionone.utils.StoryRepository
import com.dicoding.submissionone.data.DummyData
import com.dicoding.submissionone.utils.MainDispatcherRule
import com.dicoding.submissionone.utils.getOrAwaitValue
import com.dicoding.submissionone.utils.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    @Mock
    private val repository = mock(StoryRepository::class.java)
    private val dummyLogin = DummyData.generateDummyLoginResponse()
    private val dummyUser = DummyData.generateDummyGetUser()

    @Before
    fun setUp() {
        viewModel = LoginViewModel(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun saveUserInformationSuccess() = runTest {
        viewModel.saveUser(dummyUser)
        Mockito.verify(repository).saveUserData(dummyUser)
    }

    @Test
    fun clearUserInformationSuccess() = runTest {
        viewModel.logout()
        Mockito.verify(repository).logout()
    }

    @Test
    fun whenLoginReturnSuccess() {
        val expectedUser = MutableLiveData<Result<LoginResponse>>()
        expectedUser.value = Result.Success(dummyLogin)
        `when`(repository.repoLogin(email, password)).thenReturn(expectedUser)

        val actualUser = viewModel.login(email, password).getOrAwaitValue()
        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Result.Success)

        Mockito.verify(repository).repoLogin(email, password)
    }

    @Test
    fun whenLoginReturnError() {
        val expectedUser = MutableLiveData<Result<LoginResponse>>()
        expectedUser.value = Result.Error("Error")
        `when`(repository.repoLogin("", "")).thenReturn(expectedUser)

        val actualUser = viewModel.login("", "").getOrAwaitValue()
        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Result.Error)

        Mockito.verify(repository).repoLogin("", "")
    }

    companion object {
        private const val email = "email"
        private const val password = "password"
    }
}
