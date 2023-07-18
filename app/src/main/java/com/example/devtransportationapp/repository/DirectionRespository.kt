package com.example.devtransportationapp.repository

import com.example.devtransportationapp.api.ApiInterface
import com.example.devtransportationapp.model.directions.DirectionsResponse
import com.example.devtransportationapp.remote.NetworkResult
import com.example.devtransportationapp.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException

class DirectionRespository  {


      suspend fun getDirections(origin : String,
                                 destination : String,
                                 mode : String,
                                 key : String ) : Flow<NetworkResult<DirectionsResponse>>{
          return flow {
              val api = RetrofitInstance.api.create(ApiInterface::class.java)
              try {
                  emit(NetworkResult.Loading(true))
                  val response = api.getDirections(origin , destination , mode, key)
                  emit(NetworkResult.Success(response))


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

//      {
//          val api = RetrofitInstance.api.create(ApiInterface::class.java)
//           val response = api.getDirections(origin , destination , mode, key)
//          return response
//      }


}