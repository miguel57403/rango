package mb.safeEat.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mb.safeEat.R

// TODO: Remove this fragment
class NotificationFragment(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        onInit()
        return view
    }

    private fun onInit() {
        navigation.navigateTo(NotificationInitialFragment(navigation))
    }
}