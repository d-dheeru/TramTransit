package com.gotranspo.tramtransit.remote

import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

abstract class BaseRepository {
    suspend fun <T> safeapicall(apiCall: suspend () -> T) = flow {
        try {
            emit(NetworkResult.Loading(true))
            emit(NetworkResult.Success(apiCall.invoke()))
        } catch (e: HttpException) {
            emit(
                NetworkResult.Failure(
                    "Error in not recognize"
                )
            )

        } catch (e: IOException) {
            emit(NetworkResult.Failure(e.localizedMessage ?: "Check your internet connection"))
        } catch (e: Exception) {
            emit(NetworkResult.Failure(e.localizedMessage ?: "Error"))
        }
    }
}