import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.opsc7312_poe_birdwatching.SignIn
import com.example.opsc7312_poe_birdwatching.SignUp

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SignIn()
            1 -> SignUp()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getCount(): Int {
        return 2 // Number of tabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Sign In"
            1 -> "Sign Up"
            else -> null
        }
    }
}
