package com.m2f.sliidetest.SliideTest.presentation

sealed class ViewModelState<out T> {
    class Loading(val show: Boolean) : ViewModelState<Nothing>()
    object Empty : ViewModelState<Nothing>()
    object Default : ViewModelState<Nothing>()
    data class Success<T>(val data: T) : ViewModelState<T>()
    data class Error(val failureType: FailureType) : ViewModelState<Nothing>()
}