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

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Toast
import androidx.navigation.Navigation
import com.raywenderlich.android.imet.BR
import com.raywenderlich.android.imet.R
import com.raywenderlich.android.imet.data.model.People
import com.raywenderlich.android.imet.databinding.FragmentAddPeopleBinding
import kotlinx.android.synthetic.main.fragment_add_people.*
import kotlinx.android.synthetic.main.fragment_add_people.view.*

/**
 * The Fragment to add people
 */
class AddPeopleFragment : Fragment(), View.OnClickListener {

  private lateinit var viewModel: AddPeopleViewModel
  private var viewMode = false
  private var areFieldsLocked = true



  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(AddPeopleViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

      val vv = DataBindingUtil.inflate<FragmentAddPeopleBinding>(inflater,R.layout.fragment_add_people, container, false)
    //val v = inflater.inflate(R.layout.fragment_add_people, container, false)

    vv.setVariable(BR.people, viewModel)

    vv.addFab.setOnClickListener(this)

    if(arguments?.containsKey("VIEW_EDIT_MODE")==true){
      //user entered into view/edit mode so set to true
      viewMode = true
      setTitle(getString(R.string.detail_fragment_view))
      val peopleId = arguments?.getInt(getString(R.string.people_id))
      provideData(peopleId)
    }else{
      //user entered into add mode so set to false
      viewMode = false
      setTitle(getString(R.string.detail_fragment_add))
    }
    return vv.root
  }

  override fun onResume() {
    super.onResume()

  }

  override fun onClick(v: View?) {
    when(v?.id){
      addFab.id -> {
        fabButtonConfig()
      }
    }
  }

  private fun fabButtonConfig(){
    //control logic for lock, unlock and save fields when in View/Edit and save when in Add mode
    if(viewMode){
      if(areFieldsLocked){
        areFieldsLocked = false
        setTitle(getString(R.string.detail_fragment_edit))
        unlockFields()
      }
      else{
        areFieldsLocked = true
        lockFields()
        savePeopleInfo()
      }
    }else{
      savePeopleInfo()
    }
  }

  private fun provideData(peopleId:Int?){
    peopleId?.let {
      viewModel.getPeopleDetails(peopleId).observe(this, Observer { peopleDetails ->
        populatePeopleDetails(peopleDetails)
        lockFields()
        Toast.makeText(activity, "got person id:" + peopleDetails?.id, Toast.LENGTH_SHORT).show()
      })
    }
  }


  /**
   * Saves people info from user input and returns to PeopleListActivity
   */
  private fun savePeopleInfo() {
    val people = People(
        textEditName.text.toString(),
        textEditMetAt.text.toString(),
        textEditContact.text.toString(),
        textEditEmail.text.toString(),
        textEditFacebook.text.toString(),
        textEditTwitter.text.toString()
    )

    if(viewMode){
      people.id = arguments?.getInt(getString(R.string.people_id))!!
      viewModel.updatePeople(people)
      Toast.makeText(activity, "updating person", Toast.LENGTH_SHORT).show()
    }
    else{
      viewModel.addPeople(people)
      Toast.makeText(activity, "adding new person", Toast.LENGTH_SHORT).show()
    }
    Navigation.findNavController(view!!).navigateUp()
  }




  private fun populatePeopleDetails(people: People?) {
    textEditName.setText(people?.name)
    textEditMetAt.setText(people?.metAt)
    textEditContact.setText(people?.contact)
    textEditEmail.setText(people?.email)
    textEditFacebook.setText(people?.facebook)
    textEditTwitter.setText(people?.twitter)
  }

  private fun lockFields() {
    textEditName.isEnabled = false
    textEditMetAt.isEnabled = false
    textEditContact.isEnabled = false
    textEditEmail.isEnabled = false
    textEditFacebook.isEnabled = false
    textEditTwitter.isEnabled = false
    Toast.makeText(activity, "View mode...", Toast.LENGTH_SHORT).show()
  }

  private fun unlockFields() {
    textEditName.isEnabled = true
    textEditMetAt.isEnabled = true
    textEditContact.isEnabled = true
    textEditEmail.isEnabled = true
    textEditFacebook.isEnabled = true
    textEditTwitter.isEnabled = true
    Toast.makeText(activity, "Edit mode...", Toast.LENGTH_SHORT).show()
  }

  private fun setTitle(title:String){
    (activity as AppCompatActivity).supportActionBar?.title = title
  }

}
