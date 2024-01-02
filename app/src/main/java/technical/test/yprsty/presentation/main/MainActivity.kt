package technical.test.yprsty.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import technical.test.yprsty.R
import technical.test.yprsty.databinding.ActivityMainBinding
import technical.test.yprsty.model.Activity
import technical.test.yprsty.presentation.detail.DetailActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModel<MainViewModel>()

    /**
     * Container to save subscriber and
     * do unsubscribe if done listening observable
     *
     * note: Call on [onDestroy] to unsubscribe all listener
     * */
    private val disposables = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener { insertActivity() }
        binding.btnMoveDetail.setOnClickListener { moveToDetail() }
    }

    override fun onResume() {
        super.onResume()

        setupReactiveInput()
        mainViewModel.error.observe(this, ::onError)
        mainViewModel.isAdded.observe(this, ::onAdded)
    }

    private fun insertActivity() {
        val title = binding.etTitle.text.toString()
        val desc = binding.etDescription.text.toString()
        val activity = Activity(
            title = title,
            description = desc,
            isActive = true
        )

        mainViewModel.addActivity(activity)
    }

    private fun moveToDetail() {
        startActivity(Intent(this, DetailActivity::class.java))
    }

    /**
     * It's use reactive programming for validation
     *
     * Basically, it is all validation for the input from user and button state
     *
     * - titleStream, descStream: Validation for empty field
     * - buttonStateStream: Enable/disable button after all field is pass validation
     * */
    private fun setupReactiveInput() {
        val titleStream = binding.etTitle.textChanges()
            .skipInitialValue()
            .map { title -> title.isBlank() }
        val disposableTitle = titleStream.subscribe { isNotValid ->
            binding.tilTitle.error = getString(R.string.error_validate_empty)
            binding.tilTitle.isErrorEnabled = isNotValid
        }

        val descStream = binding.etDescription.textChanges()
            .skipInitialValue()
            .map { desc -> desc.isBlank() }
        val disposableDesc = descStream.subscribe { isNotValid ->
            binding.tilDescription.error = getString(R.string.error_validate_empty)
            binding.tilDescription.isErrorEnabled = isNotValid
        }

        val buttonStateStream = Observable.combineLatest(
            titleStream,
            descStream
        ) { titleInvalid, descInvalid ->
            titleInvalid.not() && descInvalid.not()
        }

        val disposableButtonState = buttonStateStream.subscribe { isValid ->
            binding.btnAdd.isEnabled = isValid
        }

        disposables.addAll(
            disposableTitle,
            disposableDesc,
            disposableButtonState
        )
    }

    private fun onAdded(isAdded: Boolean) {
        fun doClearAndInfo() {
            binding.apply {
                etTitle.text?.clear()
                etDescription.text?.clear()
            }
            Toast.makeText(
                this,
                R.string.success_add_activity,
                Toast.LENGTH_SHORT
            ).show()
        }

        if (isAdded) {
            doClearAndInfo()
        }
    }

    /**
     * Observe error message when add new activity,
     * if error snack bar will show
     * */
    private fun onError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}