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

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.ObservableBoolean
import com.raywenderlich.android.imet.IMetApp
import com.raywenderlich.android.imet.data.model.People

class AddPeopleViewModel(application: Application) : AndroidViewModel(application){

  private val peopleRepository = getApplication<IMetApp>().getPeopleRepository()
  private val peopleId = MutableLiveData<Int>()
  var name = MutableLiveData<String>()
  var metAt = MutableLiveData<String>()
  var contact = MutableLiveData<String>()
  var email = MutableLiveData<String>()
  var facebook = MutableLiveData<String>()
  val twitter = MutableLiveData<String>()
  var areFieldsEnabled = ObservableBoolean()




  fun addPeople(people: People) {
    peopleRepository.insertPeople(people)
  }

  fun updatePeople(people:People){
    peopleRepository.updatePeople(people)
  }

  // Maps people id to people details
  fun getPeopleDetails(id: Int): LiveData<People> {
    peopleId.value = id
    return Transformations.switchMap(peopleId) {
      peopleRepository.findPeople(id)
    }
  }

  // call this method to bind all fields to views
  fun getPeopleDetailsBind(id: Int){
    peopleRepository.findPeople(id).observeForever{
      name.value=it?.name
      metAt.value=it?.metAt
      contact.value=it?.contact
      email.value=it?.email
      facebook.value=it?.facebook
      twitter.value=it?.twitter
    }
  }

  fun enableDisableFields(viewMode:Boolean) {
    //control logic for lock, unlock and save fields when in View/Edit and save when in Add mode
    if (viewMode) {
      if (areFieldsEnabled.get()) {
        areFieldsEnabled.set(false)
      } else {
        areFieldsEnabled.set(true)
      }
    }
    else{
      areFieldsEnabled.set(true)
    }
  }


  fun savePeopleInfo(viewMode: Boolean, peopleId:Int) {
    val people = People(
            name.value!!,
            metAt.value!!,
            contact.value!!,
            email.value!!,
            facebook.value!!,
            twitter.value!!
    )

    if (viewMode) {
      people.id = peopleId
      updatePeople(people)
    } else {
      addPeople(people)
    }
  }

}