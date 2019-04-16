/*
 *
 *  * Copyright (c) 2018 Razeware LLC
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *  *
 *  * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 *  * distribute, sublicense, create a derivative work, and/or sell copies of the
 *  * Software in any work that is designed, intended, or marketed for pedagogical or
 *  * instructional purposes related to programming, coding, application development,
 *  * or information technology.  Permission for such use, copying, modification,
 *  * merger, publication, distribution, sublicensing, creation of derivative works,
 *  * or sale is expressly withheld.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  * THE SOFTWARE.
 *
 *
 */

package com.raywenderlich.android.imet.ui.add

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.navigation.Navigation
import com.raywenderlich.android.imet.R
import com.raywenderlich.android.imet.data.model.People
import com.raywenderlich.android.imet.databinding.FragmentAddPeopleBinding
import kotlinx.android.synthetic.main.fragment_add_people.*

/**
 * The Fragment to add people
 */
class AddPeopleFragment : Fragment() {


    private lateinit var viewModel: AddPeopleViewModel
    private var viewMode = false
    private var areFieldsLocked = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddPeopleViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (arguments?.containsKey("VIEW_EDIT_MODE") == true) {
            //user entered into view/edit mode so set to true
            viewMode = true
            setTitle(getString(R.string.detail_fragment_view))
            val peopleId = arguments?.getInt(getString(R.string.people_id))
            provideData(peopleId)
        } else {
            //user entered into add mode so set to false
            viewMode = false
            viewModel.enableDisableFields()
            setTitle(getString(R.string.detail_fragment_add))
            Toast.makeText(activity, "Add new mode...", Toast.LENGTH_SHORT).show()
        }

        viewModel.navigateToDetails.observe(this, Observer { configActivity(it!!.peekContent()) })

        return DataBindingUtil.inflate<FragmentAddPeopleBinding>(inflater, R.layout.fragment_add_people, container, false)
                .apply {
                    this.lifecycleOwner = this@AddPeopleFragment
                    this.people = viewModel
                }.root
    }



    private fun configActivity(fieldsLocked: Boolean?) {
        //control logic for lock, unlock and save fields when in View/Edit and save when in Add mode
        if (viewMode) {
            if (fieldsLocked == true) {
                areFieldsLocked = false
                setTitle(getString(R.string.detail_fragment_edit))
                Toast.makeText(activity, "Edit mode...", Toast.LENGTH_SHORT).show()
            } else {
                areFieldsLocked = true
                Toast.makeText(activity, "View mode...", Toast.LENGTH_SHORT).show()
                displayFormMode()
            }
        } else {
            displayFormMode()
        }
    }


    private fun provideData(peopleId: Int?) {
        peopleId?.let {
            viewModel.getPeopleDetailsBind(peopleId)
        }
    }


    private fun displayFormMode() {
        if (viewMode) {
            Toast.makeText(activity, "updating person", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "adding new person", Toast.LENGTH_SHORT).show()
        }
        Navigation.findNavController(view!!).navigateUp()
    }


    private fun setTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

}
