package com.kalachev.aviv.layer.presentation.feed

import android.os.RemoteException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.kalachev.aviv.layer.domain.model.Flat
import com.kalachev.aviv.layer.domain.usecase.GetAllFlats
import com.kalachev.aviv.layer.domain.usecase.SyncFlats
import com.kalachev.aviv.layer.presentation.feed.model.FeedErrorCode
import com.kalachev.aviv.layer.presentation.feed.model.FeedEvent
import com.kalachev.aviv.layer.presentation.feed.model.FeedItemModel
import com.kalachev.aviv.layer.presentation.feed.model.FeedUiAction
import com.kalachev.aviv.layer.presentation.feed.model.mapping.FeedErrorCodeMappings
import com.kalachev.aviv.layer.presentation.feed.model.mapping.FeedItemMappings
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.reflect.full.declaredFunctions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.math.BigDecimal
import kotlin.reflect.jvm.isAccessible

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getAllFlats: GetAllFlats = mock(GetAllFlats::class.java)
    private val syncFlats: SyncFlats = mock(SyncFlats::class.java)
    private val savedStateHandle: SavedStateHandle = mock(SavedStateHandle::class.java)
    private val feedItemMappings: FeedItemMappings = mock(FeedItemMappings::class.java)
    private val feedErrorCodeMappings: FeedErrorCodeMappings = mock(FeedErrorCodeMappings::class.java)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: FeedViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FeedViewModel(
            getAllFlats,
            syncFlats,
            savedStateHandle,
            feedItemMappings,
            feedErrorCodeMappings
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `a remote sync and a local update occur when the screen is opened`() = runTest {
        val feedItems = listOf(
            Flat(
                id = 3,
                bedrooms = 4,
                rooms = 4,
                city = "City 2",
                area = 150.0,
                imageUrl = "url2",
                price = BigDecimal(150000),
                professional = "Professional 2",
                offerType = 2,
                propertyType = "Type 2"
            )
        )

        `when`(getAllFlats()).thenReturn(flowOf(Result.success(feedItems)))
        `when`(syncFlats()).thenReturn(Result.success(Unit))
        `when`(feedItemMappings.toPresentation()).thenReturn { flat: Flat ->
            FeedItemModel(
                id = flat.id,
                city = flat.city,
                thumbUrl = flat.imageUrl,
                price = flat.price?.toBigInteger().toString(),
            )
        }

        viewModel.handleEvent(FeedEvent.ScreenOpened)
        advanceTimeBy(1_000L)

        verify(getAllFlats).invoke()
        verify(syncFlats).invoke()
    }

    @Test
    fun `remote sync occurs on a pull-to-refresh`() = runTest {
        `when`(syncFlats()).thenReturn(Result.success(Unit))

        viewModel.handleEvent(FeedEvent.PullToRefresh)
        advanceTimeBy(1_000L)

        verify(syncFlats).invoke()
    }

    @Test
    fun `the details screen should be opened upon an item tap`() = runTest {
        val clientId = 123L

        viewModel.handleEvent(FeedEvent.ItemTapped(clientId))

        val emittedAction = viewModel.feedUiAction.first()
        assert(emittedAction is FeedUiAction.OpenDetails && emittedAction.id == clientId)

    }

    @Test
    fun `an error should be passed to the state upon a network error`() = runTest {
        val exception = Exception("Network error")
        `when`(syncFlats()).thenReturn(Result.failure(exception))
        `when`(feedErrorCodeMappings.toPresentation()).thenReturn { throwable: Throwable ->
            when (throwable) {
                is RemoteException,
                is ServerResponseException -> FeedErrorCode.REMOTE_EXCEPTION

                is ClientRequestException,
                is IllegalArgumentException -> FeedErrorCode.LOCAL_EXCEPTION

                else -> FeedErrorCode.UNKNOWN_EXCEPTION
            }
        }
        val method = FeedViewModel::class.declaredFunctions.find { it.name == "refreshFeed" }
        method?.isAccessible = true
        method?.call(viewModel)
        advanceTimeBy(1_000L)

        verify(syncFlats).invoke()
        assert(viewModel.feedViewState.value.error == FeedErrorCode.UNKNOWN_EXCEPTION)
    }

    @Test
    fun `observeItems should update state as success on valid data`() = runTest {
        val flats = listOf(
            Flat(
                id = 3,
                bedrooms = 4,
                rooms = 4,
                city = "City 2",
                area = 150.0,
                imageUrl = "url2",
                price = BigDecimal(150000),
                professional = "Professional 2",
                offerType = 2,
                propertyType = "Type 2"
            )
        )
        val feedItems = listOf(
            FeedItemModel(
                id = 3,
                thumbUrl = "url2",
                price = "150000",
                city = "City 2"
            )
        )
        `when`(getAllFlats()).thenReturn(flowOf(Result.success(flats)))
        `when`(feedItemMappings.toPresentation()).thenReturn { flat: Flat ->
            FeedItemModel(
                id = flat.id,
                city = flat.city,
                thumbUrl = flat.imageUrl,
                price = flat.price?.toBigInteger().toString(),
            )
        }

        val method = FeedViewModel::class.declaredFunctions
            .find { it.name == "observeItems" }
        method?.isAccessible = true
        method?.call(viewModel)
        advanceTimeBy(1_000L)

        verify(getAllFlats).invoke()
        assert(viewModel.feedViewState.value.items == feedItems)
    }
}