package com.showcase.pricetracker.ui.base

import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.showcase.pricetracker.di.CompositionRoot
import com.showcase.pricetracker.di.Injector
import com.showcase.pricetracker.di.PresentationRoot
import com.showcase.pricetracker.ui.HostActivity

open class BaseFragment : Fragment() {

    private var presentationRoot: PresentationRoot? = null

    @UiThread
    protected fun getInjector(): Injector {
        return Injector(getPresentationRoot())
    }

    private fun getPresentationRoot(): PresentationRoot {
        return presentationRoot
            ?: PresentationRoot(getCompositionRoot())
                .also { presentationRoot = it }
    }


    private fun getCompositionRoot(): CompositionRoot {
        return (activity as HostActivity).getCompositionRoot()
    }

}