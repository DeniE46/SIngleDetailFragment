package com.raywenderlich.android.imet

import com.raywenderlich.android.imet.ui.add.DetailsPeopleViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsPeopleModule = module{

    // this singleton will not be needed as PeopleListModel already creates one
    //single { PeopleRepository(get()) }

    viewModel { DetailsPeopleViewModel(get()) }
}