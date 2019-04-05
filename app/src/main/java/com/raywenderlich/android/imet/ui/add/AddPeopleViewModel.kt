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
import android.databinding.ObservableField
import com.raywenderlich.android.imet.IMetApp
import com.raywenderlich.android.imet.data.model.People

class AddPeopleViewModel(application: Application) : AndroidViewModel(application) {

  private val peopleRepository = getApplication<IMetApp>().getPeopleRepository()
  private val peopleId = MutableLiveData<Int>()
  var name = ObservableField<String>()
  var metAt = ObservableField<String>()
  var contact = ObservableField<String>()
  var email = ObservableField<String>()
  var facebook = ObservableField<String>()
  val twitter = ObservableField<String>()




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
      name.set(it?.name)
      metAt.set(it?.metAt)
      contact.set(it?.contact)
      email.set(it?.email)
      facebook.set(it?.facebook)
      twitter.set(it?.twitter)
    }
  }



}