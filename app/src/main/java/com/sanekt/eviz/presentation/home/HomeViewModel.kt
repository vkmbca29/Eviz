package com.sanekt.eviz.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanekt.eviz.repository.ItemsRepository

class HomeViewModel : ViewModel() {

    val items: LiveData<List<String>> =
        MutableLiveData<List<String>>().apply { value = ItemsRepository().getItemsPage() }
}