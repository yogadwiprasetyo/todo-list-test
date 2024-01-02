package technical.test.yprsty.presentation.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import technical.test.yprsty.R
import technical.test.yprsty.databinding.ActivityDetailBinding
import technical.test.yprsty.model.Activity
import technical.test.yprsty.model.GeoIp

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModel<DetailViewModel>()
    private lateinit var activityAdapter: ActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityAdapter = ActivityAdapter()

        setupRecyclerView()
        setupSearchBar()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvActivity.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = activityAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@DetailActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupSearchBar() {
        binding.apply {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val query = searchView.text.toString()
                    if (query.isBlank()) {
                        detailViewModel.loadActivities()
                    } else {
                        detailViewModel.searchActivityByTitle(query)
                    }
                    searchBar.setText(query)
                    searchView.hide()
                    false
                }
        }
    }

    private fun observeViewModel() {
        val owner = this
        with(detailViewModel) {
            geoInfo.observe(owner, ::onGeoInfoReceived)
            errorMessage.observe(owner, ::onError)
            uiState.observe(owner) {
                when (it) {
                    is DetailUiState.Loading -> {}
                    is DetailUiState.Error -> onError(it.exception)
                    is DetailUiState.Success -> onSuccess(it.activities)
                }
            }
        }
    }

    private fun onGeoInfoReceived(geoInfo: GeoIp) {
        val location = "${geoInfo.countryCode} - ${geoInfo.country} - ${geoInfo.region}"
        binding.tvInfo.text = getString(R.string.title_location, location)
        binding.tvInfo.isVisible = true
    }

    private fun onError(error: Throwable) {
        error.printStackTrace()
        Snackbar.make(binding.root, error.message.orEmpty(), Snackbar.LENGTH_SHORT).show()
        binding.tvInfo.isVisible = false
    }

    private fun onSuccess(listActivity: List<Activity>) {
        activityAdapter.submitList(null)
        if (listActivity.isNotEmpty()) {
            activityAdapter.submitList(listActivity)
        }
    }
}