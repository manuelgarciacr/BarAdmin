package eu.manuelgc.baradmin.ui.groups

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import eu.manuelgc.baradmin.BarAdminApplication
import eu.manuelgc.baradmin.databinding.FragmentGroupsBinding
import eu.manuelgc.baradmin.model.*
import eu.manuelgc.baradmin.repositories.ProductViewModel
import eu.manuelgc.baradmin.repositories.ProductViewModelFactory


/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class GroupsFragment : Fragment() {
    private val model: ProductViewModel by viewModels {
        ProductViewModelFactory((requireActivity().application as BarAdminApplication).productRepository)
    }
    private lateinit var binding: FragmentGroupsBinding
    private val groupListener = View.OnClickListener { view ->
//        val options = navOptions {
//            anim {
//                enter = R.anim.slide_in_right
//                exit = R.anim.slide_out_left
//                popEnter = R.anim.slide_in_left
//                popExit = R.anim.slide_out_right
//            }
//        }
//        val options = navOptions {
//            anim {
//                enter = R.anim.fade_in
//                exit = R.anim.fade_out
//                popEnter = R.anim.fade_in
//                popExit = R.anim.fade_out
//            }
//        }
//        findNavController().navigate(R.id.subgroupsFragment, null, options)

        val tag = view.tag as Triple<String, String, Pair<String, String>>
//Toast.makeText(this.requireContext(), "tag: '${tag.first} ${tag.second} ${tag.third.first} ${tag.third.second}'", Toast.LENGTH_LONG).show()
        val action = if (tag.third.second == "")
            GroupsFragmentDirections.nextLevel(tag.first, tag.second, tag.third.first, tag.third.second)
        else
            GroupsFragmentDirections.ticketAction(tag.first, tag.second, tag.third.first, tag.third.second)
        findNavController().navigate(action)
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val flags =
//            View.SYSTEM_UI_FLAG_LOW_PROFILE or
//                    View.SYSTEM_UI_FLAG_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        activity?.window?.decorView?.systemUiVisibility = flags
//
//        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController

            if(controller != null) {
                controller.hide(WindowInsets.Type.statusBars())
                //controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        binding = FragmentGroupsBinding.inflate(inflater, container, false)

        val safeArgs: GroupsFragmentArgs by navArgs()
        model.setItemFilter(safeArgs.groupId, safeArgs.subgroupId, safeArgs.productId)
//        val groupId = safeArgs.groupId
//        val subgroupId = safeArgs.subgroupId
//        val productId = C
//        Toast.makeText(this.requireContext(), "groupId: '$groupId' '$subgroupId' '$productId'", Toast.LENGTH_LONG).show()

        val recyclerView = binding.rvGroups
        val adapter = GroupsAdapter(null, groupListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
//        var mGroups = listOf<Subgroup>()
//        var mSubgroups = listOf<Subgroup>()
//        model.groups.observe(viewLifecycleOwner) { groups ->
//            // Update the cached copy of the words in the adapter.
//            groups.let {
//                mGroups = it
//                if (mSubgroups.isNotEmpty())
//                adapter.submitList(it + mSubgroups)
//            }
//        }
//        model.subgroups.observe(viewLifecycleOwner) { subgroups ->
//            // Update the cached copy of the words in the adapter.
//            subgroups.let {
//                mSubgroups = it
//                if (mGroups.isNotEmpty())
//                    adapter.submitList(mGroups + it)
//            }
//        }

        model.dataItems.observe(viewLifecycleOwner) { dataItems ->
            // Update the cached copy of the words in the adapter.
//Toast.makeText(this.requireContext(), "dataItems: '${dataItems.size}'", Toast.LENGTH_LONG).show()
            adapter.submitList(dataItems)
//            dataItems.let {
//                adapter.submitList(it)
//            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}