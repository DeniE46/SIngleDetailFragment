package com.raywenderlich.android.imet

import com.raywenderlich.android.imet.data.PeopleRepository
import com.raywenderlich.android.imet.ui.list.PeoplesListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val peopleListModule = module {

    single { PeopleRepository(get()) }

    viewModel { PeoplesListViewModel(get()) }
}