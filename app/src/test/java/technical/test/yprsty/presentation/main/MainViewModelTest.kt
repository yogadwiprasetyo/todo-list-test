package technical.test.yprsty.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import technical.test.yprsty.MainDispatcherRule
import technical.test.yprsty.data.ToDoRepository
import technical.test.yprsty.getOrAwaitValue
import technical.test.yprsty.model.Activity

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ToDoRepository
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {
        mainViewModel = MainViewModel(repository)
    }

    @Test
    fun `when addActivity is success isAdded change to true`() = runTest {
        val activity = Activity(title = "title", description = "desc", isActive = true)
        `when`(repository.insertActivity(activity)).thenReturn(Unit)
        mainViewModel.addActivity(activity)

        val isAdded = mainViewModel.isAdded.getOrAwaitValue()
        assertTrue(isAdded)
    }

    @Test
    fun `when addActivity is failed isAdded false and error have message`() = runTest {
        val activity = Activity(title = "title", description = "desc", isActive = true)
        `when`(repository.insertActivity(activity)).thenThrow(RuntimeException("Exception"))
        mainViewModel.addActivity(activity)

        val isAdded = mainViewModel.isAdded.getOrAwaitValue()
        val error = mainViewModel.error.getOrAwaitValue()

        assertFalse(isAdded)
        assertEquals("Exception", error)
    }

}