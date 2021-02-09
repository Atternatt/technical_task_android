package com.m2f.sliidetest.SliideTest.presentation

sealed class ViewModelState<out T> {
    object Loading : ViewModelState<Nothing>()
    object Empty : ViewModelState<Nothing>()
    data class Success<T>(val data: T) : ViewModelState<T>()
    data class Error(val failureType: FailureType) : ViewModelState<Nothing>()
}