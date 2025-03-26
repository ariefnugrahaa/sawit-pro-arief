package com.example.sawitproarief.domain.usecase.base

abstract class UseCase<in P, R> {
    abstract suspend fun doWork(params: P): R
}

abstract class NoParamsUseCase<R> {
    abstract suspend fun doWork(): R
}