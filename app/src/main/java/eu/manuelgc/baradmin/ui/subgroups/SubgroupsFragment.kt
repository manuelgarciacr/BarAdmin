package eu.manuelgc.baradmin.ui.subgroups

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eu.manuelgc.baradmin.BarAdminApplication
import eu.manuelgc.baradmin.R
import eu.manuelgc.baradmin.databinding.FragmentSubgroupsBinding
import eu.manuelgc.baradmin.repositories.ProductViewModel
import eu.manuelgc.baradmin.repositories.ProductViewModelFactory

class SubgroupsFragment : Fragment() {
    private val model: ProductViewModel by viewModels {
        ProductViewModelFactory((requireActivity().application as BarAdminApplication).productRepository)
    }
    private lateinit var binding: FragmentSubgroupsBinding
    private val subgroupListener = View.OnClickListener { _ ->
        findNavController().navigate(R.id.product, null)
        true
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
        binding = FragmentSubgroupsBinding.inflate(inflater, container, false)

        val recyclerView = binding.rvSubgroups
        val adapter = SubgroupsAdapter(null, subgroupListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
//        model.allSubgroups.observe(viewLifecycleOwner) { subgroups ->
//            // Update the cached copy of the words in the adapter.
//            subgroups.let { adapter.submitList(it) }
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
 }