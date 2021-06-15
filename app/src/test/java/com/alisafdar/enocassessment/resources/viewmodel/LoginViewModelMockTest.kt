package com.alisafdar.enocassessment.resources.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.DataStore
import androidx.lifecycle.Observer
import com.alisafdar.enocassessment.data.persistance.UserPreferences
import com.alisafdar.enocassessment.data.network.apis.AuthApi
import com.alisafdar.enocassessment.data.repository.AuthRepository
import com.alisafdar.enocassessment.data.repository.UserRepository
import com.alisafdar.enocassessment.data.responses.LoginResponse
import com.alisafdar.enocassessment.datastore.data.PreferenceData
import com.alisafdar.enocassessment.resources.CoroutineTestRule
import com.alisafdar.enocassessment.ui.auth.LoginDataState
import com.alisafdar.enocassessment.ui.auth.LoginViewModel
import com.alisafdar.enocassessment.utils.LoginValidator
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(LoginValidator::class)
class LoginViewModelMockTest {
    companion object {
        private const val EMAIL = "abc@abc.com"
        private const val PASSWORD = "123456"
    }

    @Mock
    private lateinit var authApi: AuthApi

    @Mock
    private lateinit var dataStore: DataStore<PreferenceData>

    @Mock
    private lateinit var userPreferences: UserPreferences

    @Mock
    private lateinit var authRepo: AuthRepository

    @Mock
    private lateinit var userRepo: UserRepository

    @Mock
    private lateinit var loginResponse: LoginResponse

    private val mockObserverForStates = mock<Observer<LoginDataState>>()
    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var loginViewModel: LoginViewModel

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coRoutineTestRule = CoroutineTestRule()

    @Before
    fun before() {
        mockStatic(LoginValidator::class.java)

        loginViewModel = LoginViewModel(authRepo, userRepo)
        loginViewModel.getObserverState().observeForever(mockObserverForStates)
    }

    @Test
    fun testIfEmailInvalid_ReportEmailError() {
        `when`(LoginValidator.isEmailValid(anyString())).thenAnswer { false }

        loginViewModel.login(EMAIL, PASSWORD)

        verify(mockObserverForStates).onChanged(LoginDataState.InValidEmailState)
        verifyNoMoreInteractions(mockObserverForStates)
    }


    @Test
    fun testIPasswordInvalid_ReportPasswordError() {
        `when`(LoginValidator.isEmailValid(anyString())).thenAnswer { true }
        `when`(LoginValidator.isPasswordValid(anyString())).thenAnswer { false }

        loginViewModel.login(EMAIL, PASSWORD)

        verify(mockObserverForStates).onChanged(LoginDataState.InValidPasswordState)
        verifyNoMoreInteractions(mockObserverForStates)
    }

    @Test
    fun testIfEmailAndPasswordValid_DoLogin() {
        `when`(LoginValidator.isEmailValid(anyString())).thenAnswer { true }
        `when`(LoginValidator.isPasswordValid(anyString())).thenAnswer { true }

        runBlockingTest {
            `when`(authApi.login(anyString(), anyString())).thenReturn(
                loginResponse
            )
        }

        loginViewModel.login(EMAIL, PASSWORD)

        verify(mockObserverForStates).onChanged(LoginDataState.ValidCredentialsState)
        verify(
            mockObserverForStates, times(2)
        ).onChanged(LoginDataState.Success(ArgumentMatchers.any()))
        verifyNoMoreInteractions(mockObserverForStates)
    }

    @Test
    fun testThrowError_OnLoginFailed() {
        `when`(LoginValidator.isEmailValid(anyString())).thenAnswer { true }
        `when`(LoginValidator.isPasswordValid(anyString())).thenAnswer { true }

        val error = RuntimeException()
        runBlockingTest {
            `when`(authApi.login(anyString(), anyString())).thenThrow(
                error
            )
        }

        loginViewModel.login(EMAIL, PASSWORD)

        verify(mockObserverForStates).onChanged(LoginDataState.ValidCredentialsState)
        verify(mockObserverForStates, times(2)).onChanged(LoginDataState.Error(ArgumentMatchers.any()))
        verifyNoMoreInteractions(mockObserverForStates)
    }

    @After
    @Throws(Exception::class)
    fun tearDownClass() {
        framework().clearInlineMocks()
        loginViewModel.getObserverState().removeObserver(mockObserverForStates)
    }
}