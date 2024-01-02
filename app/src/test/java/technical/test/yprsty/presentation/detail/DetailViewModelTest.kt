package technical.test.yprsty.presentation.detail

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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ToDoRepository
    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(repository)
    }

    @Test
    fun `when loadActivities throw exception then uiState is Error`() {
        val exception = RuntimeException("Exception")
        `when`(repository.loadAllActivity()).thenThrow(exception)
        detailViewModel.loadActivities()

        val uiState = detailViewModel.uiState.getOrAwaitValue()

        assertTrue(uiState is DetailUiState.Error)
    }

    @Test
    fun `when searchActivityByTitle throw exception then uiState is Error`() {
        val exception = RuntimeException("Exception")
        `when`(repository.searchActivityByTitle("title")).thenThrow(exception)
        detailViewModel.searchActivityByTitle("title")

        val uiState = detailViewModel.uiState.getOrAwaitValue()

        assertTrue(uiState is DetailUiState.Error)
    }

    @Test
    fun `when fetchGeoInfo throw exception then errorMessage has value`() {
        val exception = RuntimeException("Exception")
        `when`(repository.loadGeoApi()).thenThrow(exception)
        detailViewModel.fetchGeoInfo()

        val errorMessage = detailViewModel.errorMessage.getOrAwaitValue()

        assertTrue(errorMessage.message.orEmpty().isNotBlank())
        assertNotNull(errorMessage)
    }
}