package com.renansantiago.restaurantmenu.presentation.ui.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.renansantiago.restaurantmenu.exception.DefaultException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    val showLoading = ObservableBoolean()

    private val compositeDisposable = CompositeDisposable()

    protected fun <T> subscribeSingle(
        observable: Single<T>,
        success: ((T) -> Unit)? = null,
        error: ((DefaultException) -> Unit)? = null
    ): Disposable {
        val disposable = observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                success,
                {
                    when (it) {
                        is DefaultException -> error?.invoke(it)
                        else -> error?.invoke(DefaultException())
                    }
                }
            )

        compositeDisposable.add(disposable)

        return disposable
    }

    protected fun dispose(disposable: Disposable?) {
        disposable?.let { compositeDisposable.remove(it) }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}