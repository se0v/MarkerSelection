package com.example.markerselection

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.alpha
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.markerselection.databinding.FragmentMarkersBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MarkersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MarkersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val chooseItems: MutableList<ChooseItem> = mutableListOf(

        ChooseItem("How many persons in the blacklist?", listOf("15","5", "10")), ChooseItem("How many status police in career?", listOf("6", "5", "7")),
        ChooseItem("What kind of car does Bull have?", listOf("Mercedes-Benz McLaren", "Ferrari", "Lada"))
    )

    lateinit var currentChooseItem: ChooseItem
    lateinit var answers: MutableList<String>
    private var chooseItemIndex = 0
    private val numberOfQuestions = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val binding = DataBindingUtil.inflate<FragmentMarkersBinding>(
             inflater, R.layout.fragment_markers, container, false
         )

        getRandomChooseItem()

        binding.chooseFragment = this //связывание этого класса с разметкой

        binding.enterButton.setOnClickListener{ view: View ->

            val selectedCheckboxId = binding.markersRadioGroup.checkedRadioButtonId

            if (selectedCheckboxId != -1){

                var answerIndex = 0
                when (selectedCheckboxId){

                    R.id.firstRadioButton -> answerIndex = 0
                    R.id.secondRadioButton -> answerIndex = 1
                    R.id.thirdRadioButton -> answerIndex = 2

                }
//логика ответов на вопросы
                if (answers[answerIndex] == currentChooseItem.answerList[0]){

                    chooseItemIndex++
                    if (chooseItemIndex < numberOfQuestions){

                        setChooseItem()
                        binding.invalidateAll() //показать все изменения в разметке
                    } else {
                        binding.loadImageView.animate()
                            .alpha(0f)
                            .duration = 500

                        Handler().postDelayed({
                            //переход в winFragment
                            view.findNavController().navigate(
                                R.id.action_markersFragment_to_winFragment
                            )
                        }, 800)


                    }

                } else {
                    binding.loadImageView.animate()
                        .alpha(0.6f)
                        .duration = 200
                    Handler().postDelayed({
                        binding.loadImageView.animate()
                            .alpha(1.0f)
                            .duration = 200
                    },200)
                    Handler().postDelayed({
                        binding.loadImageView.animate()
                            .alpha(0.6f)
                            .duration = 400
                    },400)

                    Handler().postDelayed({
                        //переход в loseFragment
                        view.findNavController().navigate(
                            R.id.action_markersFragment_to_loseFragment
                        )
                    }, 800)


                }

            }

        }

        (activity as AppCompatActivity).supportActionBar?.title = "Marker Selection"

        setHasOptionsMenu(true) //бахнуть меню в этом фрагменте

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(

            item, requireView().findNavController()

        ) || super.onOptionsItemSelected(item)
    }

    private fun getRandomChooseItem() {

        chooseItems.shuffle()
        chooseItemIndex = 0
        setChooseItem()

    }

    private fun setChooseItem(){

        currentChooseItem = chooseItems[chooseItemIndex]
        answers = currentChooseItem.answerList.toMutableList()
        answers.shuffle()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MarkersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MarkersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}